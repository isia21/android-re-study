/****************************************************************************
 * File: toolChecker.cpp
 * 
 * 
 * Description: 
 * 		- RootBeer, с поправкой на детект рута по списку
 * 
 * 		- Внедряемся по win32 re опыту hijacking`а/враппера/прокси dll
 * 		- libtool-checker.so - удачно лежит в открытом репо
 * 		- 1. Подбор ПИН-кода
 * 		- 2. Дамп памяти с солью (и, потенциально, с key)
 * 		
 ****************************************************************************/

#include <dlfcn.h>
#include <stdio.h>
#include <fstream>
#include <string>
#include <set>
#include <link.h>
#include <cstring>
#include <cstdint>
#include <chrono>
#include <atomic>
#include <mutex>
#include <pthread.h>
#include <unistd.h>

// Android headers
#include <jni.h>
#include <android/log.h>

// User includes
#include "toolChecker.h"

// ============================================================
// Logging
// ============================================================
#define LOG_TAG "som33ly-log"
#define LOGD(...)  if (DEBUG) __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__);
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__);
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,  LOG_TAG, __VA_ARGS__);

static int DEBUG = 1;

// ============================================================
// Типы и глобалы брутфорса
// ============================================================
using DoStafFn = jbyteArray(*)(JNIEnv*, jclass, jbyteArray, jbyte);

static DoStafFn		g_fnTargetFn		= nullptr;
static jclass		g_pNativeLibClass	= nullptr;
static JavaVM*		g_pJVM				= nullptr;

static std::atomic<bool>	g_running		{false};
static std::atomic<bool>	g_found			{false};
static std::atomic<int>		g_totalDone		{0};
static std::atomic<int>		g_activeWorkers	{0};
static std::mutex			g_mtxLogs;

//Общее кол-во ПИН-кодов для перебора
static constexpr int	g_lMaxPIN		= 10000;   // 0000..9999
//Максимальное кол-во потоков/воркеров
static constexpr int	g_lWorkersNum	= 4;

//Флаг валидности/доверенной среды приложения (не рут, не Frida)
static constexpr jbyte	g_bSecFlag		= (jbyte)0xF0;
//фиксированная "cost", чтоб можно было сверить входные и выходные данные синтетики с устройством
static constexpr int	g_lInput_Cost	= 1;

//Успешно подошедший ПИН-код (REAL_PIN = 5971)
static int REAL_PIN = 0xffffffff;

//Указатель на базовы адрес модуля native-lib.os
static uintptr_t g_pModuleBase;

// ============================================================
// Поиск base модуля через dl_iterate_phdr
// ============================================================
struct FindModuleCtx {
	const char* name;
	uintptr_t   base;
};

static int FindModuleCallback(struct dl_phdr_info* info, size_t, void* data) {
	auto* ctx = static_cast<FindModuleCtx*>(data);
	if (!info->dlpi_name || info->dlpi_name[0] == '\0') return 0;

	const char* slash = strrchr(info->dlpi_name, '/');
	const char* name  = slash ? slash + 1 : info->dlpi_name;

	if (strcmp(name, ctx->name) == 0) {
		ctx->base = (uintptr_t)info->dlpi_addr;
		return 1;
	}
	return 0;
}

static uintptr_t FindModuleBase(const char* name) {
	FindModuleCtx ctx{name, 0};
	dl_iterate_phdr(FindModuleCallback, &ctx);
	return ctx.base;
}

// ============================================================
// (из оригинального toolChecker)
// ============================================================
void Java_com_scottyab_rootbeer_RootBeerNative_setLogDebugMessages(
		JNIEnv* env, jobject thiz, jboolean debug) {
	DEBUG = debug ? 1 : 0;
}

int exists(const char *fname) {
	FILE *file = fopen(fname, "r");
	if (file) {
		LOGD("LOOKING FOR BINARY: %s PRESENT!!!", fname);
		fclose(file);
		return 1;
	}
	LOGD("LOOKING FOR BINARY: %s Absent :(", fname);
	return 0;
}

static int lib_callback(struct dl_phdr_info* info, size_t, void*) {
	if (info->dlpi_name && info->dlpi_name[0] != '\0') {
		if (strstr(info->dlpi_name, "re.pwnme") != nullptr) {
			__android_log_print(ANDROID_LOG_INFO, "som33ly-log",
				"so/elf base=%p name=%s",
				(void*)info->dlpi_addr, info->dlpi_name);
		}
	}
	return 0;
}

void log_loaded_libraries() {
	__android_log_print(ANDROID_LOG_INFO, "som33ly-log", "--- Loaded Libraries ---");
	dl_iterate_phdr(lib_callback, nullptr);
}

// ============================================================
// Резолв цели (один раз)
// ============================================================
static bool ResolveTarget(JNIEnv* env, jobject thiz) {
	if (g_fnTargetFn && g_pNativeLibClass) return true;

	uintptr_t base = FindModuleBase("libnative-lib.so");
	if (base == 0) {
		LOGE("libnative-lib.so not found");
		return false;
	}
	g_pModuleBase = base;
	uintptr_t addr = base + 0x1780F0;
	LOGI("base=%p, DoStaf addr=%p", (void*)base, (void*)addr);
	g_fnTargetFn = reinterpret_cast<DoStafFn>(addr);

	jclass localCls = env->GetObjectClass(thiz);
	if (!localCls) {
		LOGE("GetObjectClass failed");
		return false;
	}
	g_pNativeLibClass = (jclass)env->NewGlobalRef(localCls);
	env->DeleteLocalRef(localCls);

	LOGI("resolved: fn=%p class=%p", (void*)g_fnTargetFn, g_pNativeLibClass);
	return true;
}

// ============================================================
// Один вызов DoStaf
// ============================================================
struct DoStafResult {
	bool    ok;
	bool    success;
	jsize   len;
	uint8_t first[17];
};

static DoStafResult CallDoStaf(JNIEnv* env, int lPin) {
	DoStafResult r{};

	char szBuff[32];
	snprintf(szBuff, sizeof(szBuff), "%08d%08d", lPin, g_lInput_Cost);

	jbyte buf[16];
	for (int i = 0; i < 16; ++i)
		buf[i] = (jbyte)(unsigned char)szBuff[i];

	jbyteArray input = env->NewByteArray(16);
	if (!input) return r;

	env->SetByteArrayRegion(input, 0, 16, buf);

	jbyteArray result = g_fnTargetFn(env, g_pNativeLibClass, input, g_bSecFlag);

	if (result) {
		jsize rlen = env->GetArrayLength(result);
		jbyte* rdata = env->GetByteArrayElements(result, nullptr);
		if (rdata) {
			r.len = rlen;
			//int n = rlen < 16 ? rlen : 16;
			for (int i = 0; i < rlen; ++i)
				r.first[i] = (uint8_t)rdata[i];

			r.ok      = true;
			r.success = (r.first[0] != 0x51);

			env->ReleaseByteArrayElements(result, rdata, JNI_ABORT);
		}
		env->DeleteLocalRef(result);
	}

	env->DeleteLocalRef(input);
	return r;
}

// ============================================================
// Воркер
// ============================================================
struct WorkerArg {
	int workerId;
	int start;
	int end;
};

static void* WorkerThread(void* arg) {
	auto* wa = static_cast<WorkerArg*>(arg);
	int wid = wa->workerId;

	LOGI("[W%d] start, range %d..%d", wid, wa->start, wa->end);

	JNIEnv* env = nullptr;
	if (!g_pJVM || g_pJVM->AttachCurrentThreadAsDaemon(&env, nullptr) != JNI_OK) {
		LOGE("[W%d] Attach failed", wid);
		g_activeWorkers.fetch_sub(1);
		delete wa;
		return nullptr;
	}

	int done = 0;
	auto tStart = std::chrono::steady_clock::now();

	for (int pin = wa->start; pin < wa->end; ++pin) {
		if (!g_running.load() || g_found.load())
			break;

		DoStafResult r = CallDoStaf(env, pin);
		++done;
		g_totalDone.fetch_add(1);

		if (!r.ok) {
			LOGE("[W%d] call failed at pin=%d", wid, pin);
			continue;
		}

		if (r.success) {
			bool expected = false;
			if (g_found.compare_exchange_strong(expected, true)) {
				std::lock_guard<std::mutex> lk(g_mtxLogs);
				LOGI("=== FOUND === pin=%08d bValue=0x%02X", pin, g_bSecFlag);
				LOGI("=== result[0..%d]: %02X %02X %02X %02X ...",
					 r.len, r.first[0], r.first[1], r.first[2], r.first[3]);
				REAL_PIN = pin;
			}
			break;
		}

		if ((done % 100) == 0) {
			auto tNow = std::chrono::steady_clock::now();
			auto ms = std::chrono::duration_cast<std::chrono::milliseconds>(
						  tNow - tStart).count();
			LOGI("[W%d] done=%d last_pin=%d avg=%.1f ms/call",
				 wid, done, pin, ms / (double)done);
		}
	}

	auto tEnd = std::chrono::steady_clock::now();
	auto totalMs = std::chrono::duration_cast<std::chrono::milliseconds>(
					   tEnd - tStart).count();
	LOGI("[W%d] exit, done=%d in %lld ms", wid, done, (long long)totalMs);

	g_pJVM->DetachCurrentThread();
	g_activeWorkers.fetch_sub(1);
	delete wa;
	return nullptr;
}

// ============================================================
// Запуск брутфорса
// ============================================================
static void StartPinBruteForce(JNIEnv* env, jobject thiz) {
	if (g_running.load()) {
		LOGI("Brute-force already running");
		return;
	}
	if (!ResolveTarget(env, thiz)) {
		LOGE("Cannot resolve target");
		return;
	}

	g_running.store(true);
	g_found.store(false);
	g_totalDone.store(0);
	g_activeWorkers.store(g_lWorkersNum);

	int chunk = g_lMaxPIN / g_lWorkersNum;

	for (int i = 0; i < g_lWorkersNum; ++i) {
		auto* wa = new WorkerArg{};
		wa->workerId = i;
		wa->start = i * chunk;
		wa->end   = (i == g_lWorkersNum - 1) ? g_lMaxPIN : (i + 1) * chunk;

		pthread_t tid;
		if (pthread_create(&tid, nullptr, WorkerThread, wa) != 0) {
			LOGE("pthread_create failed for W%d", i);
			g_activeWorkers.fetch_sub(1);
			delete wa;
		} else {
			pthread_detach(tid);
		}
	}

	LOGI("Started %d workers, total pins = %d", g_lWorkersNum, g_lMaxPIN);
}

// ============================================================
// JNI_OnLoad — запоминаем JavaVM
// ============================================================
extern "C" JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
	g_pJVM = vm;
	LOGI("JNI_OnLoad vm=%p", vm);
	return JNI_VERSION_1_6;
}

// ============================================================
// Точка входа — checkForRoot
// ============================================================
extern "C" int Java_com_scottyab_rootbeer_RootBeerNative_checkForRoot(
		JNIEnv* env, jobject thiz, jobjectArray pathsArray)
{

	//REAL_PIN = 5971;

	if(REAL_PIN == 0xffffffff) {		
		// Резолвим и запускаем брутфорс в фоне — UI не блокируется
		StartPinBruteForce(env, thiz);
	}
	//		else {
	//			if (!ResolveTarget(env, thiz)) {
	//				LOGE("Cannot resolve target");
	//			}
	//			else {
			
			// 1. Вызываем метод с корректным пином.
			//const byte VALID_ROOT_FLAG = 0xF0;
			DoStafResult r = CallDoStaf(env, REAL_PIN);

			if (!r.ok) {
				LOGE("Call failed at pin=%d", REAL_PIN);
			}
			else {		
				if (r.success) {
					LOGI("=== result[0..%d]: %02X : `r2c-%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x%02x`",
						r.len, 
						r.first[0], 
						r.first[1], r.first[2], r.first[3], r.first[4],
						r.first[5], r.first[6], r.first[7], r.first[8],
						r.first[9], r.first[10], r.first[11], r.first[12],
						r.first[13], r.first[14], r.first[15], r.first[16]);
				}

				//char szBuf[256];
				uint8_t * pData = (uint8_t*)g_pModuleBase;

				for(int i = 0x288160; i < 0x288220;) {
					//sprintf(szBuf, "%08X :\t%02X %02X %02X %02X %02X %02X %02X %02X\t%02X %02X %02X %02X %02X %02X %02X %02X\t\t\n"
					LOGI("=== data dump %08X :\t%02X %02X %02X %02X %02X %02X %02X %02X\t%02X %02X %02X %02X %02X %02X %02X %02X\t\t",
						i, 
						pData[i+0], pData[i+1], pData[i+2], pData[i+3], pData[i+4], pData[i+5], pData[i+6], pData[i+7], 
						pData[i+8], pData[i+9], pData[i+10], pData[i+11], pData[i+12], pData[i+13], pData[i+14], pData[i+15]
					);
					i+=0x10;
				}
			}
	//			}
	//		}


	LOGI("[*] bypass");
	return false;

	//		// Оригинальная логика checkForRoot (не трогаем)
	//		int binariesFound = 0;
	//		int stringCount = env->GetArrayLength(pathsArray);
	//		
	//		for (int i = 0; i < stringCount; i++) {
	//			jstring string = (jstring)env->GetObjectArrayElement(pathsArray, i);
	//			const char *pathString = env->GetStringUTFChars(string, 0);
	//			
	//			binariesFound += exists(pathString);
	//			
	//			env->ReleaseStringUTFChars(string, pathString);
	//		}
	//		
	//		return binariesFound > 0;
	
}
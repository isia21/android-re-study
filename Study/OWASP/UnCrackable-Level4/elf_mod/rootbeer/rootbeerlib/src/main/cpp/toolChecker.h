/****************************************************************************
 * File:   toolChecker.h
 * Author: Matthew Rollings
 * Date:   19/06/2015
 *
 * Description : Root checking JNI NDK code
 *
 ****************************************************************************/

extern "C" {

#include <jni.h>

void Java_com_scottyab_rootbeer_RootBeerNative_setLogDebugMessages( JNIEnv* env, jobject thiz, jboolean debug);

int Java_com_scottyab_rootbeer_RootBeerNative_checkForRoot( JNIEnv* env, jobject thiz , jobjectArray pathsArray );

}

namespace HookManager {
	static uintptr_t g_targetBase = 0;

    // Найти base-адрес модуля через dl_iterate_phdr
    uintptr_t FindModuleBase(const char* name);

    // Установить хук на libThemLib.so::native_DoStaf
    bool InstallThemHook(uintptr_t pBase);

    // Остановить воркер (вызывается из destructor)
    void Shutdown();
}

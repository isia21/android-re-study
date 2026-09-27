/****************************************************************************
 * File:   toolChecker.cpp
 * Author: Matthew Rollings
 * Date:   19/06/2015
 *
 * Description : Root checking JNI NDK code
 *
 ****************************************************************************/

/****************************************************************************
 *>>>>>>>>>>>>>>>>>>>>>>>>> System Includes <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*
 ****************************************************************************/

/*
 * som33ly data
 */
//#define _GNU_SOURCE
#include <dlfcn.h>
#include <stdio.h>
#include <fstream>   // std::ifstream, std::getline
#include <string>    // std::string
#include <set>       // std::set
#include <link.h>
#include <cstring>

// Android headers
#include <jni.h>
#include <android/log.h>

// String / file headers
#include <string.h>
#include <stdio.h>

/****************************************************************************
 *>>>>>>>>>>>>>>>>>>>>>>>>>> User Includes <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*
 ****************************************************************************/
#include "toolChecker.h"

/****************************************************************************
 *>>>>>>>>>>>>>>>>>>>>>>>>>> Constant Macros <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*
 ****************************************************************************/

// LOGCAT
#define  LOG_TAG    "RootBeer"
#define  LOGD(...)  if (DEBUG) __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__);
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__);
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

/* Set to 1 to enable debug log traces. */
static int DEBUG = 1;

/*****************************************************************************
 * Description: Sets if we should log debug messages
 *
 * Parameters: env - Java environment pointer
 *      thiz - javaobject
 * 	bool - true to log debug messages
 *
 *****************************************************************************/
void Java_com_scottyab_rootbeer_RootBeerNative_setLogDebugMessages( JNIEnv* env, jobject thiz, jboolean debug)
{
  if (debug){
    DEBUG = 1;
  }
  else{
    DEBUG = 0;
  }
}


/*****************************************************************************
 * Description: Checks if a file exists
 *
 * Parameters: fname - filename to check
 *
 * Return value: 0 - non-existant / not visible, 1 - exists
 *
 *****************************************************************************/
int exists(const char *fname)
{
    FILE *file;
    if ((file = fopen(fname, "r")))
    {
        LOGD("LOOKING FOR BINARY: %s PRESENT!!!",fname);
        fclose(file);
        return 1;
    }
    LOGD("LOOKING FOR BINARY: %s Absent :(",fname);
    return 0;
}




static int lib_callback(struct dl_phdr_info* info, size_t size, void* data) {
    // info->dlpi_name — путь к модулю (для главного exe может быть пустым)
    // info->dlpi_addr — базовый адрес загрузки
    if (info->dlpi_name && info->dlpi_name[0] != '\0') {
        // фильтр по вашему приложению, если нужно
        if (strstr(info->dlpi_name, "re.pwnme") != nullptr) {
            __android_log_print(
                ANDROID_LOG_INFO, "som33ly-log",
                "so/elf base=%p name=%s",
                (void*)info->dlpi_addr,
                info->dlpi_name
            );
        }
    }
    return 0; // продолжаем перебор
}

void log_loaded_libraries() {

    __android_log_print(ANDROID_LOG_INFO, "som33ly-log", "--- Loaded Libraries ---");
    dl_iterate_phdr(lib_callback, nullptr);
}

/*****************************************************************************
 * Description: Checks for root binaries
 *
 * Parameters: env - Java environment pointer
 *      thiz - javaobject
 *
 * Return value: int number of su binaries found
 *
 *****************************************************************************/
int Java_com_scottyab_rootbeer_RootBeerNative_checkForRoot( JNIEnv* env, jobject thiz, jobjectArray pathsArray )
{

    __android_log_print(ANDROID_LOG_INFO, "[som33ly-log] ", "[*] bypass");

    Dl_info info;
    if (dladdr((void*)&Java_com_scottyab_rootbeer_RootBeerNative_checkForRoot, &info)) {
        LOGI("so/elf name: %s", info.dli_fname);
        LOGI("so/elf base: %p", info.dli_fbase);
    } else {
        LOGI("so/elf name: can't access (%s)", dlerror());
    }


    log_loaded_libraries(); 
    


    int binariesFound = 0;

    int stringCount = (env)->GetArrayLength(pathsArray);

    for (int i=0; i<stringCount; i++) {
        jstring string = (jstring) (env)->GetObjectArrayElement(pathsArray, i);
        const char *pathString = (env)->GetStringUTFChars(string, 0);

		binariesFound+=exists(pathString);

		(env)->ReleaseStringUTFChars(string, pathString);
    }

    return binariesFound>0;
  }

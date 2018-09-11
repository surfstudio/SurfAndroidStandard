#include <jni.h>
#include <iostream>
#include <fstream>

jclass Throwable;

static const char *const message = "Root has been detected!";


void initJavaClasses(JNIEnv *env);

void checkRooted(JNIEnv *env);

using namespace std;

const string folders[] = {"/system/app/Superuser.apk", "/system/app/KingUser.apk", "/sbin/su",
                          "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
                          "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su",
                          "/data/local/su", "/su/bin/su"};

extern "C"
JNIEXPORT void JNICALL
Java_ru_surfstudio_android_security_jni_Natives_init(JNIEnv *env) {
    initJavaClasses(env);
    checkRooted(env);
}

inline bool file_exists(const std::string &name) {
    ifstream f(name.c_str());
    return f.rdstate() == 0;
}

bool rooted() {
    for (int i = 0; i < folders->length(); ++i) {
        if (file_exists(folders[i])) {
            return true;
        }
    }
    return false;
}

void checkRooted(JNIEnv *env) {
    if (rooted()) {
        env->ThrowNew(Throwable, message);
        return;
    }
}

void initJavaClasses(JNIEnv *env) {
    jclass throwableLink = env->FindClass("ru/surfstudio/android/security/root/error/RootDetectedException");

    if (throwableLink == NULL) {
        return;
    }
    Throwable = (jclass) env->NewGlobalRef(throwableLink);
}
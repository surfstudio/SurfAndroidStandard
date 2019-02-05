/*
  Copyright (c) 2018-present, SurfStudio LLC. Margarita Volodina, Oleg Zhilo.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
#include <jni.h>
#include <iostream>
#include <fstream>

using namespace std;

bool rooted();

const string folders[] = {"/system/app/Superuser.apk", "/system/app/KingUser.apk", "/sbin/su",
                          "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su",
                          "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su",
                          "/data/local/su", "/su/bin/su"};

extern "C"
JNIEXPORT jboolean JNICALL Java_ru_surfstudio_android_security_jni_NativeRootChecker_check(JNIEnv *env, jclass type) {
    auto isRooted = static_cast<jboolean>(rooted());
    return isRooted;
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
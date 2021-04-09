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
package ru.surfstudio.android.security.crypto.security

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

fun Cipher.initEncryptMode(secretKey: Key?): Cipher {
    init(Cipher.ENCRYPT_MODE, secretKey)
    return this
}

fun Cipher.initDecryptMode(secretKey: Key?, iv: ByteArray?): Cipher {
    init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
    return this
}
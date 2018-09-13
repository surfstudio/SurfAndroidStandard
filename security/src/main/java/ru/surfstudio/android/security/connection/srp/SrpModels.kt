/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.security.connection.srp

import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import java.io.Serializable

/**
 * Доменная модель данных для шифрования пароля при регистарции
 */
data class Srp6ConfigRegistration(val H: String = EMPTY_STRING,
                                  val g: Int = 0,
                                  val N: String = EMPTY_STRING) : Serializable

/**
 * Доменная модель данных, приходящих от сервера для дальнейшего шифрования
 */
data class SrpChallenge(val b: String = EMPTY_STRING,
                        val salt: String = EMPTY_STRING)
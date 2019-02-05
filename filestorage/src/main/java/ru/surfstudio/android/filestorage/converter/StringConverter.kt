/*
  Copyright (c) 2018-present, SurfStudio LLC. Margarita Volodina.

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
package ru.surfstudio.android.filestorage.converter

/**
 * Конвертер для прямого и обратного преобразовния String в массив байтов
 */
class StringConverter : ObjectConverter<String> {

    override fun encode(value: String): ByteArray = value.toByteArray()

    override fun decode(rawValue: ByteArray): String = String(rawValue)
}
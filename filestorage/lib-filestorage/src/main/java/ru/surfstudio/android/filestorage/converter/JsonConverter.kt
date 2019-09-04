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

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Конвертер для прямого и обратного преобразования json-объекта в тип Т
 */
class JsonConverter<T>(
        private val classType: Class<T>,
        private val gson: Gson = GsonBuilder().create()
): ObjectConverter<T> {

    override fun encode(value: T): ByteArray = gson.toJson(value).toByteArray()

    override fun decode(rawValue: ByteArray): T = gson.fromJson(String(rawValue), classType)
}
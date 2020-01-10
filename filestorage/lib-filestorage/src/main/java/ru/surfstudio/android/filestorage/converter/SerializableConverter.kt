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

import ru.surfstudio.android.logger.Logger
import java.io.*

/**
 * Конвертер для прямого и обратного преобразования массива байтов в Serializable-тип Т
 */
class SerializableConverter<T : Serializable> : ObjectConverter<T> {

    override fun encode(value: T): ByteArray? {
        try {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                ObjectOutputStream(byteArrayOutputStream).use { objectOutputStream ->
                    objectOutputStream.writeObject(value)
                    return byteArrayOutputStream.toByteArray()
                }
            }
        } catch (e: IOException) {
            Logger.e(e)
            return null
        }
    }

    override fun decode(rawValue: ByteArray): T? {
        try {
            ByteArrayInputStream(rawValue).use { byteArrayInputStream ->
                ObjectInputStream(byteArrayInputStream).use { objectInputStream ->
                    return objectInputStream.readObject() as T
                }
            }
        } catch (e: IOException) {
            Logger.e(e)
            return null
        } catch (e: ClassNotFoundException) {
            Logger.e(e)
            return null
        }
    }
}
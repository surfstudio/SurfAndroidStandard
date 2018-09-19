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
package ru.surfstudio.android.filestorage

import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.FileProcessor
import ru.surfstudio.android.logger.Logger
import java.io.*

/**
 * Базовый класс для кэширования Serializable данных
 */
open class BaseSerializableFileStorage<T : Serializable> : BaseFileStorage<T> {

    constructor(fileProcessor: FileProcessor,
                namingProcessor: NamingProcessor)
            : super(fileProcessor, namingProcessor, SerializableConverter<T>())

    constructor(fileProcessor: FileProcessor,
                namingProcessor: NamingProcessor,
                encryptor: Encryptor)
            : super(fileProcessor, namingProcessor, SerializableConverter<T>(), encryptor)

    private class SerializableConverter<T : Serializable> : ObjectConverter<T> {

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
}
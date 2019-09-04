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
package ru.surfstudio.android.filestorage.storage

import ru.surfstudio.android.filestorage.converter.JsonConverter
import ru.surfstudio.android.filestorage.encryptor.Encryptor
import ru.surfstudio.android.filestorage.naming.NamingProcessor
import ru.surfstudio.android.filestorage.processor.FileProcessor

/**
 * Базовый класс для кэширования моделей в формате json
 */
open class BaseJsonFileStorage<T> : BaseFileStorage<T> {

    constructor(fileProcessor: FileProcessor,
                namingProcessor: NamingProcessor,
                classType: Class<T>)
            : super(fileProcessor, namingProcessor, JsonConverter<T>(classType))

    constructor(fileProcessor: FileProcessor,
                namingProcessor: NamingProcessor,
                classType: Class<T>,
                encryptor: Encryptor)
            : super(fileProcessor, namingProcessor, JsonConverter<T>(classType), encryptor)
}
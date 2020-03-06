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
package ru.surfstudio.standard.i_network.network.cache;


import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import ru.surfstudio.android.filestorage.encryptor.Encryptor;
import ru.surfstudio.android.filestorage.naming.NamingProcessor;
import ru.surfstudio.android.filestorage.naming.Sha256NamingProcessor;
import ru.surfstudio.android.filestorage.naming.SimpleNamingProcessor;
import ru.surfstudio.android.filestorage.processor.FileProcessor;
import ru.surfstudio.android.filestorage.storage.BaseTextFileStorage;

/**
 * простой кеш, кеширует сырой ответ сервера
 * если размер кеша равен 1, данные содержатся в файле с именем cacheDirName внутри папки cacheDirName
 */
public class SimpleCache extends BaseTextFileStorage {

    private static final NamingProcessor simpleNamingProcessor = new SimpleNamingProcessor();
    private static final NamingProcessor sha256NamingProcessor = new Sha256NamingProcessor();

    public SimpleCache(String cacheDir,
                       String cacheDirName,
                       int maxSize) {
        super(new FileProcessor(
                        cacheDir,
                        simpleNamingProcessor.getNameFrom(cacheDirName),
                        maxSize),
                maxSize == 1
                        ? new SingleFileNamingProcessor(cacheDirName)
                        : sha256NamingProcessor);
    }

    public SimpleCache(String cacheDir,
                       String cacheDirName,
                       int maxSize,
                       Encryptor encryptor) {
        super(new FileProcessor(
                        cacheDir,
                        simpleNamingProcessor.getNameFrom(cacheDirName),
                        maxSize),
                maxSize == 1
                        ? new SingleFileNamingProcessor(cacheDirName)
                        : sha256NamingProcessor,
                encryptor);
    }
    private static class SingleFileNamingProcessor implements NamingProcessor {
        private String singleFileName;

        public SingleFileNamingProcessor(String singleFileName) {
            this.singleFileName = singleFileName;
        }

        @Nullable
        @Override
        public String getNameFrom(@NotNull String rawName) {
            //любая запись перезаписывает единственный файл
            return singleFileName;
        }
    }
}

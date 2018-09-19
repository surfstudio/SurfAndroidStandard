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
package ru.surfstudio.android.network.etag.storage;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;

import ru.surfstudio.android.dagger.scope.PerApplication;
import ru.surfstudio.android.filestorage.BaseTextFileStorage;
import ru.surfstudio.android.filestorage.CacheConstant;
import ru.surfstudio.android.filestorage.encryptor.Encryptor;
import ru.surfstudio.android.filestorage.naming.Sha256NamingProcessor;
import ru.surfstudio.android.filestorage.processor.FileProcessor;

@PerApplication
final class EtagCache extends BaseTextFileStorage {

    private static final int CACHE_SIZE = 5000;

    @Inject
    public EtagCache(@Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME) final String cacheDir) {
        super(
                new FileProcessor(cacheDir, "etag", CACHE_SIZE),
                new Sha256NamingProcessor(),
                new Encryptor() {
                    @NotNull
                    @Override
                    public byte[] encrypt(@NotNull byte[] decryptedBytes) {
                        return decryptedBytes;
                    }

                    @NotNull
                    @Override
                    public byte[] decrypt(@NotNull byte[] rawBytes) {
                        return rawBytes;
                    }
                });
    }
}

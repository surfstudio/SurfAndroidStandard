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
package ru.surfstudio.standard.i_network.network.etag.storage;

import javax.inject.Named;

import ru.surfstudio.android.filestorage.CacheConstant;
import ru.surfstudio.android.filestorage.naming.Sha256NamingProcessor;
import ru.surfstudio.android.filestorage.processor.FileProcessor;
import ru.surfstudio.android.filestorage.storage.BaseTextFileStorage;

final public class EtagCache extends BaseTextFileStorage {

    private static final int CACHE_SIZE = 5000;

    public EtagCache(@Named(CacheConstant.INTERNAL_CACHE_DIR_DAGGER_NAME) final String cacheDir) {
        super(new FileProcessor(cacheDir, "etag", CACHE_SIZE), new Sha256NamingProcessor());
    }
}

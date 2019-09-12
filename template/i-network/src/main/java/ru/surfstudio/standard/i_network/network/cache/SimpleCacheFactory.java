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

import com.annimon.stream.Stream;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import ru.surfstudio.android.filestorage.CacheConstant;
import ru.surfstudio.android.filestorage.encryptor.EmptyEncryptor;
import ru.surfstudio.android.filestorage.encryptor.Encryptor;

/**
 * фабрика простых кешей
 */
@Deprecated
public class SimpleCacheFactory {

    private final String cacheDir;
    private final SimpleCacheUrlConnector cacheUrlConnector;
    private final Encryptor encryptor;
    private Map<SimpleCacheInfo, SimpleCache> caches = new HashMap<>();

    public SimpleCacheFactory(@Named(CacheConstant.EXTERNAL_CACHE_DIR_DAGGER_NAME) final String cacheDir,
                              SimpleCacheUrlConnector cacheUrlConnector) {
        this.cacheDir = cacheDir;
        this.cacheUrlConnector = cacheUrlConnector;
        this.encryptor = new EmptyEncryptor();
    }

    public SimpleCacheFactory(@Named(CacheConstant.EXTERNAL_CACHE_DIR_DAGGER_NAME) final String cacheDir,
                              SimpleCacheUrlConnector cacheUrlConnector,
                              Encryptor encryptor) {
        this.cacheDir = cacheDir;
        this.cacheUrlConnector = cacheUrlConnector;
        this.encryptor = encryptor;
    }

    public SimpleCache getSimpleCache(SimpleCacheInfo simpleCacheInfo) {
        SimpleCache cache = caches.get(simpleCacheInfo);
        if (cache == null) {
            cache = new SimpleCache(
                            cacheDir,
                            simpleCacheInfo.getCacheName(),
                            simpleCacheInfo.getMaxSize(),
                            encryptor);
            caches.put(simpleCacheInfo, cache);
        }
        return cache;
    }

    public void clearAllCache() {
        Stream.of(cacheUrlConnector.getSimpleCacheInfo())
                .map(this::getSimpleCache)
                .forEach(SimpleCache::clear);
    }
}

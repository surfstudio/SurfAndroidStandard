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

public final class EtagStorage {

    private EtagCache etagCache;

    public EtagStorage(final EtagCache etagCache) {
        this.etagCache = etagCache;
    }

    /**
     * Метод, возвращающий e-tag по необходимому url или пустую строку в случае, если e-tag не найден
     *
     * @param url - ключ
     * @return - уникальный e-tag для конкретного {@param url}
     */
    public String getEtag(final String url) {
        final String etag = etagCache.get(url);
        return etag != null ? etag : "";
    }

    /**
     * Метод, сохраняющий e-tag по конкретному ключу или перезаписывает текущий e-tag
     *
     * @param url  - ключ
     * @param etag - уникальный e-tag для конкретного {@param url}
     */
    public void putEtag(final String url, final String etag) {
        etagCache.put(url, etag);
    }
}

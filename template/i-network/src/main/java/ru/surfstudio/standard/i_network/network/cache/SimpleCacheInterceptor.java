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

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.standard.i_network.network.error.CacheEmptyException;
import ru.surfstudio.standard.i_network.network.error.HttpCodes;
import ru.surfstudio.standard.i_network.network.BaseServerConstants;

/**
 * Интерцептор с логикой работы простого кеша
 * если в запросе есть header: queryMode: ServerConstant.QUERY_MODE_FROM_SIMPLE_CACHE
 */
public class SimpleCacheInterceptor implements Interceptor {
    public static final String VERSION_CHAR = "v";
    public static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";
    public static final String MESSAGE_OK = "OK";
    private static final String SLASH = "/";
    private SimpleCacheFactory simpleCacheFactory;
    private SimpleCacheUrlConnector simpleCacheUrlConnector;

    public SimpleCacheInterceptor(SimpleCacheFactory simpleCacheFactory,
                                  SimpleCacheUrlConnector simpleCacheUrlConnector) {
        this.simpleCacheFactory = simpleCacheFactory;
        this.simpleCacheUrlConnector = simpleCacheUrlConnector;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originalRequest = chain.request();
        final HttpUrl url = originalRequest.url();
        final String headerQueryMode = originalRequest.header(BaseServerConstants.HEADER_QUERY_MODE);
        boolean supportQueryMode = headerQueryMode != null;

        if (supportQueryMode) {
            //этот запрос поддерживает кеширование
            SimpleCacheInfo simpleCacheInfo = simpleCacheUrlConnector.getByUrl(url, originalRequest.method());
            boolean supportSimpleCache = simpleCacheInfo != null;
            boolean fromCache = Integer.parseInt(headerQueryMode) == BaseServerConstants.QUERY_MODE_FROM_SIMPLE_CACHE;

            if (supportSimpleCache) {
                return processRequestWithSimpleCache(chain, originalRequest, url, simpleCacheInfo, fromCache);

            } else if (fromCache) {
                //простой кеш не поддерживается для этого url, но попытались его использовать
                throw new IllegalStateException(String.format("Simple cache not supported for url: %s", url.toString()));
            }
        }
        //простой кеш не поддерживается, просто производим запрос на сервер
        return chain.proceed(originalRequest);
    }

    private Response processRequestWithSimpleCache(Chain chain, Request originalRequest, HttpUrl url, SimpleCacheInfo simpleCacheInfo, boolean fromCache) throws IOException {
        //этот запрос поддерживает простой кеш
        SimpleCache simpleCache = simpleCacheFactory.getSimpleCache(simpleCacheInfo);
        String cacheKey = getCacheKeyFromUrl(url);
        if (fromCache) {
            //возвращаем данные из кеша
            String cachedResponseBody;
            cachedResponseBody = simpleCache.get(cacheKey);
            Logger.d("cached data exist=" + (cachedResponseBody != null) + " for url " + url);
            if (cachedResponseBody != null) {
                return createCachedResponse(cachedResponseBody, originalRequest);
            } else {
                throw new CacheEmptyException();
            }
        } else {
            //производим запрос на сервер и кешируем результат
            Response response = chain.proceed(originalRequest);
            if (response.isSuccessful() && response.body() != null) {
                MediaType mediaType = response.body().contentType();
                String stringBody = response.body().string();
                simpleCache.put(cacheKey, stringBody);
                response = response.newBuilder()
                        .body(ResponseBody.create(mediaType, stringBody))
                        .build();
            }
            return response;
        }
    }

    private Response createCachedResponse(String cachedResponseBody, Request originalRequest) {
        return new Response.Builder()
                .code(HttpCodes.CODE_200)
                .message(MESSAGE_OK)
                .body(ResponseBody.create(MediaType.parse(MEDIA_TYPE_APPLICATION_JSON), cachedResponseBody))
                .request(originalRequest)
                .protocol(Protocol.HTTP_1_1)
                .build();
    }

    private String getCacheKeyFromUrl(HttpUrl url) {
        String rawUrl = url.toString();
        int versionPathSegmentIndex = rawUrl.indexOf(VERSION_CHAR);
        int startKeyIndex = rawUrl.indexOf(SLASH, versionPathSegmentIndex) + 1;
        rawUrl = rawUrl.substring(startKeyIndex); //remove base path segment : https://api.labirint.ru/vXXX/
        return rawUrl;
    }
}

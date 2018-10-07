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
package ru.surfstudio.android.network.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.annimon.stream.Stream;

import java.util.Collection;
import java.util.List;

import okhttp3.HttpUrl;
import ru.surfstudio.android.logger.Logger;
import ru.surfstudio.android.network.BaseUrl;

/**
 * ищет {@link SimpleCacheInfo} для url запроса
 * Базовый класс для хранения информации об Url простого кеша.
 */
public class SimpleCacheUrlConnector {
    private static final String URL_SPLIT_REGEX = "[/?&]";
    private static final String BRACE = "{";

    @NonNull
    private final BaseUrl baseUrl;

    @NonNull
    private final Collection<SimpleCacheInfo> simpleCacheInfo;

    public SimpleCacheUrlConnector(@NonNull BaseUrl baseUrl, @NonNull Collection<SimpleCacheInfo> simpleCacheInfo) {
        this.baseUrl = baseUrl;
        this.simpleCacheInfo = simpleCacheInfo;
        Logger.d("AAA SimpleCacheUrlConnector init");
        for (SimpleCacheInfo cacheInfo : simpleCacheInfo) {
            Logger.d("AAA %s", cacheInfo.getCacheName());
        }
        Logger.d("AAA SimpleCacheUrlConnector init finish");
    }

    @SuppressWarnings("squid:S134")
    @Nullable
    public SimpleCacheInfo getByUrl(HttpUrl url, String method) {
        List<String> networkUrlSegments = getNetworkUrlSegments(url.toString());
        Logger.d("AAA SimpleCacheUrlConnector getByUrl %s", url);
        for (SimpleCacheInfo cacheInfo : simpleCacheInfo) {
            Logger.d("AAA cacheName %s", cacheInfo.getCacheName());
            Logger.d("AAA getBaseApiMethod %s", cacheInfo.getBaseApiMethod().getUrl());
            boolean baseCacheMethodCorresponds = checkApiMethod(method,
                    networkUrlSegments,
                    cacheInfo.getBaseApiMethod());

            if (baseCacheMethodCorresponds) {
                Logger.d("AAA return %s", cacheInfo.getCacheName());
                return cacheInfo;
            } else {
                for (SimpleCacheInfo.ApiMethod extraCacheMethod : cacheInfo.getExtraMethods()) {
                    boolean extraCacheMethodCorresponds = checkApiMethod(
                            method, networkUrlSegments, extraCacheMethod);
                    if (extraCacheMethodCorresponds) {
                        Logger.d("AAA return %s", cacheInfo.getCacheName());
                        return cacheInfo;
                    }
                }
            }
        }
        Logger.d("AAA return null");
        return null;
    }

    @SuppressWarnings("squid:S135")
    private boolean checkApiMethod(String networkMethod,
                                   List<String> networkUrlSegments,
                                   SimpleCacheInfo.ApiMethod cacheApiMethod) {
        Logger.d("AAA checkApiMethod");
        Logger.d("AAA ===============================");
        Logger.d("AAA networkUrlSegments");
        Logger.d("AAA ===============================");
        for (String segment: networkUrlSegments) {
            Logger.d("AAA %s", segment);
        }
        Logger.d("AAA ===============================");
        if (!networkMethod.equals(cacheApiMethod.getMethod())) {
            return false;
        }
        String[] cacheUrlSegments = cacheApiMethod.getUrl().split(URL_SPLIT_REGEX);
        Logger.d("AAA cacheUrlSegments");
        Logger.d("AAA ===============================");
        for (String segment : cacheUrlSegments) {
            Logger.d("AAA %s", segment);
        }
        Logger.d("AAA ===============================");
        if (cacheUrlSegments.length > networkUrlSegments.size()) {
            return false;
        }
        boolean urlsIdentical = true;
        for (int i = 0; i < cacheUrlSegments.length; i++) {
            String cacheUrlSegment = cacheUrlSegments[i];
            Logger.d("AAA cacheUrlSegment %s", cacheUrlSegment);
            if (isCacheUrlSegmentParameter(cacheUrlSegment)) {
                //в url адреса кэша идут скобки {}, в таком случае в результирующем сегменте не должно быть параметра
                if (isNetworkUrlSegmentParameter(networkUrlSegments.get(i))) {
                    continue;
                } else {
                    return false;
                }
            }
            String networkUrlSegment = networkUrlSegments.get(i);
            Logger.d("AAA networkUrlSegment %s", networkUrlSegment);
            urlsIdentical = cacheUrlSegment.equals(networkUrlSegment);
            if (!urlsIdentical) {
                break;
            }
        }
        return urlsIdentical;
    }

    @NonNull
    private List<String> getNetworkUrlSegments(String httpUrl) {
        String path = httpUrl.replaceAll(baseUrl.getBase(), "");
        int skip = baseUrl.getApiVersion() == null ? 0 : 1; //пропускаем сегмент версии
        return Stream.of(path.split(URL_SPLIT_REGEX))
                .filterNot(TextUtils::isEmpty)
                .skip(skip)
                .toList();
    }

    private boolean isCacheUrlSegmentParameter(String cacheUrlPathSegment) {
        return cacheUrlPathSegment.startsWith(BRACE);
    }

    /**
     * Считаем часть сегментом, если она не содержит параметра
     */
    private boolean isNetworkUrlSegmentParameter(String cacheUrlPathSegment) {
        return !cacheUrlPathSegment.contains("=");
    }

    @NonNull
    public Collection<SimpleCacheInfo> getSimpleCacheInfo() {
        return simpleCacheInfo;
    }
}

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

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import okhttp3.HttpUrl;
import ru.surfstudio.android.network.BaseUrl;

/**
 * ищет {@link SimpleCacheInfo} для url запроса
 * Базовый класс для хранения информации об Url простого кеша.
 */
@Deprecated
public class SimpleCacheUrlConnector {
    private static final String URL_SPLIT_REGEX = "[/?&]";
    private static final String BRACE = "{";

    @NonNull
    private final BaseUrl baseUrl;

    @NonNull
    private final List<SimpleCacheInfo> simpleCacheInfo;

    public SimpleCacheUrlConnector(@NonNull BaseUrl baseUrl, @NonNull Collection<SimpleCacheInfo> simpleCacheInfo) {
        this.baseUrl = baseUrl;
        this.simpleCacheInfo = new ArrayList<>(simpleCacheInfo);
        sortSimpleCacheInfo(this.simpleCacheInfo);
    }

    @SuppressWarnings("squid:S134")
    @Nullable
    public SimpleCacheInfo getByUrl(HttpUrl url, String method) {
        List<String> networkUrlSegments = getNetworkUrlSegments(url.toString());
        for (SimpleCacheInfo cacheInfo : simpleCacheInfo) {
            boolean baseCacheMethodCorresponds = checkApiMethod(method,
                    networkUrlSegments,
                    cacheInfo.getBaseApiMethod());

            if (baseCacheMethodCorresponds) {
                return cacheInfo;
            } else {
                for (SimpleCacheInfo.ApiMethod extraCacheMethod : cacheInfo.getExtraMethods()) {
                    boolean extraCacheMethodCorresponds = checkApiMethod(
                            method, networkUrlSegments, extraCacheMethod);
                    if (extraCacheMethodCorresponds) {
                        return cacheInfo;
                    }
                }
            }
        }
        return null;
    }

    @NonNull
    public Collection<SimpleCacheInfo> getSimpleCacheInfo() {
        return simpleCacheInfo;
    }

    @SuppressWarnings("squid:S135")
    private boolean checkApiMethod(String networkMethod,
                                   List<String> networkUrlSegments,
                                   SimpleCacheInfo.ApiMethod cacheApiMethod) {
        if (!networkMethod.equals(cacheApiMethod.getMethod())) {
            return false;
        }
        String[] cacheUrlSegments = cacheApiMethod.getUrl().split(URL_SPLIT_REGEX);
        if (cacheUrlSegments.length > networkUrlSegments.size()) {
            return false;
        }
        boolean urlsIdentical = true;
        for (int i = 0; i < cacheUrlSegments.length; i++) {
            String cacheUrlSegment = cacheUrlSegments[i];
            if (isCacheUrlSegmentParameter(cacheUrlSegment)) {
                //в url адреса кэша идут скобки {}, в таком случае в результирующем сегменте не должно быть параметра
                if (isNetworkUrlSegmentParameter(networkUrlSegments.get(i))) {
                    continue;
                } else {
                    return false;
                }
            }
            String networkUrlSegment = networkUrlSegments.get(i);
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

    private static boolean isCacheUrlSegmentParameter(String cacheUrlPathSegment) {
        return cacheUrlPathSegment.startsWith(BRACE);
    }

    /**
     * Считаем часть сегментом, если она не содержит параметра
     */
    private boolean isNetworkUrlSegmentParameter(String cacheUrlPathSegment) {
        return !cacheUrlPathSegment.contains("=");
    }

    private static void sortSimpleCacheInfo(List<SimpleCacheInfo> simpleCacheInfo) {
        Collections.sort(simpleCacheInfo, (first, second) -> {
            String[] firstSegments = first.getBaseApiMethod().getUrl().split(URL_SPLIT_REGEX);
            String[] secondSegments = second.getBaseApiMethod().getUrl().split(URL_SPLIT_REGEX);
            int count = firstSegments.length > secondSegments.length ? secondSegments.length : firstSegments.length;
            for (int i = 0; i < count; i++) {
                if (isCacheUrlSegmentParameter(firstSegments[i]) && !isCacheUrlSegmentParameter(secondSegments[i])) {
                    return 1;
                }

                if (!isCacheUrlSegmentParameter(firstSegments[i]) && isCacheUrlSegmentParameter(secondSegments[i])) {
                    return -1;
                }
            }
            return 0;
        });
    }
}

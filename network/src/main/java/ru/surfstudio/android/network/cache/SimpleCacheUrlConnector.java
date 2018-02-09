package ru.surfstudio.android.network.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.annimon.stream.Stream;

import java.util.Collection;
import java.util.List;

import okhttp3.HttpUrl;
import ru.surfstudio.android.core.domain.network.url.BaseUrl;

/**
 * ищет {@link SimpleCacheInfo} для url запроса
 * Базовый класс для хранения информации об Url простого кеша.
 * Имплементация провайдится через модуль со скоупом {@link ru.surfstudio.android.core.app.dagger.scope.PerApplication}
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
    }

    @SuppressWarnings("squid:S134")
    @Nullable
    public SimpleCacheInfo getByUrl(HttpUrl url, String method) {
        List<String> networkUrlSegments = getNetworkUrlSegments(url.toString());
        for (SimpleCacheInfo cacheInfo : simpleCacheInfo) {
            boolean baseCacheMethodCorresponds = checkApiMethod(
                    method, networkUrlSegments, cacheInfo.getBaseApiMethod());
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

    @SuppressWarnings("squid:S135")
    private boolean checkApiMethod(String networkMethod,
                                   List<String> networkUrlSegments,
                                   SimpleCacheInfo.ApiMethod cacheApiMethod) {
        if (!networkMethod.equals(cacheApiMethod.getMethod())) {
            return false;
        }
        List<String> cacheUrlSegments = getNetworkUrlSegments(cacheApiMethod.getUrl());
        if (cacheUrlSegments.size() > networkUrlSegments.size()) {
            return false;
        }
        boolean urlsIdentical = true;
        for (int i = 0; i < cacheUrlSegments.size(); i++) {
            String cacheUrlSegment = cacheUrlSegments.get(i);
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

package ru.surfstudio.android.network.cache;

/**
 * информация о запросах, поддерживающих простое кеширование
 */
public class SimpleCacheInfo {

    private ApiMethod baseMethod;
    private String cacheName;
    private int maxSize;
    private ApiMethod[] extraMethods;

    SimpleCacheInfo(String method, String baseUrl, String cacheName, int maxSize) {
        this(new ApiMethod(method, baseUrl), cacheName, maxSize);
    }

    SimpleCacheInfo(String method, String baseUrl, String cacheName, ApiMethod... extraMethods) {
        this(new ApiMethod(method, baseUrl), cacheName, 1, extraMethods);
    }

    /**
     * @param baseMethod   метод апи на который будет возвращаться кеш, и ответ которого будет кешироваться
     * @param cacheName    имя кеша
     * @param maxSize      размер кеша
     * @param extraMethods методы апи, ответ которых будет кешироваться в тот же файл что и ответ baseMethod
     *                     эти методы поддерживаются только кешем с размером 1
     */
    SimpleCacheInfo(ApiMethod baseMethod, String cacheName, int maxSize, ApiMethod... extraMethods) {
        if (extraMethods.length > 0 && maxSize > 1) {
            throw new IllegalArgumentException("ExtraMethods supported only for cache with one file");
        }
        this.cacheName = cacheName;
        this.baseMethod = baseMethod;
        this.extraMethods = extraMethods;
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public String getCacheName() {
        return cacheName;
    }

    public ApiMethod getBaseApiMethod() {
        return baseMethod;
    }

    public ApiMethod[] getExtraMethods() {
        return extraMethods;
    }

    public static class ApiMethod {
        private String method;
        private String url;

        public ApiMethod(String method, String url) {
            this.method = method;
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }
    }
}

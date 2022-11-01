# URL-кеш

**Url-кеш** - при успешном запросе на сервер кешируется сырой ответ
сервера (только для запросов, зарегистрированных в `SimpleCacheInfo`).
Для получения данных из кеша необходимо в метод Api Retrofit'а передать header
ключ: `BaseServerConstants#HEADER_QUERY_MODE` значение: `BaseServerConstants#QUERY_MODE_FROM_CACHE`.
Если кеш пустой, то будет возвращен `null`.

Логика простого кеша содержится в `SimpleCacheInterceptor`. Для удаления
записей кеша следует получить кеш для соответствующего запроса через
`SimpleCacheFactory` и произвести очистку.

Для конкатенирования данных из кеша и с сервера предусмотрен метод
`BaseNetworkInteractor#hybridQueryWithSimpleCache`. Предусмотрена также
возможность иметь один кеш для нескольких запросов.

Для использования неоходимо создать [`SimpleCacheInterceptor`](../src/main/java/ru/surfstudio/android/network/cache/SimpleCacheInterceptor.java)
и добавить его в конструктор при создании инстанса `OkHttpClient`.

Кешированные данные получают через тот же интерфейс Api, который используется
Retrofit'ом. Для поддержки кеширования метод в интерфейсе должен выглядеть так:
```
@GET(SOME_URL)
Observable<SomeResponse> getSomeContent(
            @Header(HEADER_QUERY_MODE) @ServerConstants.QueryMode int queryMode,
            @Path("id") int id);
```

Кроме того необходимо иметь инстанс класса `SimpleCacheInfo` для этого запроса.
Этот инстанс необходимо зарегистрировать в `SimpleCacheStorage`.
В зависимости от queryMode данные будут запрошены либо с сервера, либо из кеша.

Если очистка кеша необходима при обновлении приложения на новую версию,
то соответствующий код необходимо разместить в AppMigrationStorage
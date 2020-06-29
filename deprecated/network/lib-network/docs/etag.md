# Механизм ETag

- [Что такое ETag](#что-такое-etag)
- [Использование](#использование)

### Что такое ETag

Etag - это метка, которая отправляется в Headers запроса и приходит в
Headers ответа. Используется для получения информации о том, изменились
ли данные с последнего такого же запроса. Изменение может служить признаком
необходимости обновления кеша, ил наоборот, его актуальности.

Если передать в запрос etag, ранее сохраненный из ответа на такой же запрос,
то возможны два исхода:

* Данные с предыдущего запроса не менялись - **в ответе http код 304**

* Данные с предыдущего запроса изменились - **в ответе свежие данные и новый etag**.

Для осуществления запроса с Etag следует добавить *header* `If-None-Match` c
соответствующим значением. В ответе в случае изменения данных вместе с
ответом придет *header* `ETag` с новым значением Etag.

### Использование

Для хранения etag используется [`EtagStorage`][storage].

Основные классы :
 * [EtagInterceptor][interceptor]
 * [EtagStorage][storage]
 * [EtagCache][cache]
 * [EtagConstants][const]

Для использования неоходимо [`EtagInterceptor`][interceptor] добавить в
конструктор при создании инстанса `OkHttpClient`.
В `Interceptor` посредством даггера инжектиться `EtagStorage`.
В свою очередь, в хранилище попадает реализация кеша , для хранения
тега.

[interceptor]: ../src/main/java/ru/surfstudio/android/network/etag/EtagInterceptor.java
[const]: ../src/main/java/ru/surfstudio/android/network/etag/EtagConstants.java
[storage]: ../src/main/java/ru/surfstudio/android/network/etag/storage/EtagStorage.java
[cache]: ../src/main/java/ru/surfstudio/android/network/etag/storage/EtagCache.java
[network]: usage.md
# Использование

- [Cache](#cache)
- [CallAdapter](#calladapter)
- [Error](#error)
- [Etag](#etag)
- [Response](#response)
- [Маппинг сущностей](#маппинг-сущностей)
- [Прочее](#прочее)

### Cache

Предусмотрена возможность использовать [URL-кеш][url].
Также  есть возможность реализовать кеширование объектов с
сохранением в [файловое хранилище][filestorage].

### CallAdapter
Конвертирует ответы сервера в `Observable` а также оборачивает ошибки
в соответветствующий инстанс иерархии `NetworkException`

[BaseCallAdapterFactory][call] - кроме конвертирования запроса в Observable,
выполняет следующие функции:
    - Конвертирует IOException в NoConnectionException
    - Конвертирует EOFException в NoContentException
    - Конвертирует HttpException в HttpError

Для использования в проекте необходимо создать класс-наследник.
И передать его в `Retrofit.Builder`.

Для пробрасывания своих типов исключений можно переопределить `onHttpException()`.

### Error
Предоставляет набор исключений для часто возникабщих ошибок при работе
с сетью. Все ошибки имеют общего предка - `NetworkException`
- [ConversionException](../src/main/java/ru/surfstudio/android/network/error/ConversionException.java)
- [CacheEmptyException](../src/main/java/ru/surfstudio/android/network/error/CacheEmptyException.java)
- [HttpError](../src/main/java/ru/surfstudio/android/network/error/HttpError.java)
    - [NotModifiedException](../src/main/java/ru/surfstudio/android/network/error/NotModifiedException.java)
- [NoInternetException](../src/main/java/ru/surfstudio/android/network/error/NoInternetException.java)

### Etag
Предоставляет поддержку [механизма Etag][etag].  Служит признаком того,
что данные на сервере изменились.

### Response
[`BaseResponse`](../src/main/java/ru/surfstudio/android/network/response/BaseResponse.java)
маркерный интерфейс который обозначает классы, которые используются для
парсинга корневой сущности ответа.


### Маппинг сущностей

[`Transformable`][t1] и [`TransformableUtil`][t2], [ObservableExtension][obExt] -
Интерфейс и набор утилит для конвертации объекта/потока одного класса в другой.
Основное применение [маппинг серверных ответов.][network_main]

### Прочее
- [`BaseNetworkInteractor`][bnint] - Интерактор для работы с сетью. Основная задача
упростить взаимодействие с закешированными ответами и ответами сервера, путем
создания [гибридных запросов][hybrid].

[bnint]: ../src/main/java/ru/surfstudio/android/network/BaseNetworkInteractor.java
[t1]: ../src/main/java/ru/surfstudio/android/network/Transformable.java
[t2]: ../src/main/java/ru/surfstudio/android/network/TransformUtil.java
[obExt]: ../src/main/java/ru/surfstudio/android/network/ObservableExtension.kt
[call]: ../src/main/java/ru/surfstudio/android/network/calladapter/BaseCallAdapterFactory.java
[hybrid]: hybrid.md
[etag]: etag.md
[network_main]: ../../../../docs/interactor/network.md
[filestorage]: ../../../../filestorage/README.md
[url]: url_cache.md
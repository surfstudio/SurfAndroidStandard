[Главная](../../../../docs/main.md)

# Гибридные запросы

В классе [`BaseNetworkInteractor`][base] реализован механизм гибридных запросов.
Такие запросы предназначены для конкатенации данных из кэша и сервера.

Гибридные запросы представлены методами типа `hybridQuery..`.

Они поддерживают различные стратегии того, что первым отдавать - ответ сервера
или кэш. Стратегии представлены классом [`DataStrategy`][strategy]. Стратегия
передается непосредственно в сам метод.

*Совет*: если надо загрузить данные в фоне - используйте стратегию `DataStrategy.ONLY_ACTUAL`.

Гибридные методы кроме приоритета(стратегии) принимают также запрос к кэшу
и запрос к сети. В случае выбора стратегии `DataStrategy.AUTO` проверяется еще
и скорость соединения с сетью.

Для простого кэша предназначены специализированные методы -
`hybridQueryWithSimpleCache()`, которые принимают только сам запрос на сервер,
так как за возврат из кеша в этом случае будет ответсвенен [`SimpleCacheInterceptor`][simple].

[base]: ../src/main/java/ru/surfstudio/android/network/BaseNetworkInteractor.java
[strategy]: ../src/main/java/ru/surfstudio/android/network/DataStrategy.java
[simple]: ../src/main/java/ru/surfstudio/android/network/cache/SimpleCacheInterceptor.java
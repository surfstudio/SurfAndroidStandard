# Слой Interactor

### Работа с Сервером

Работа с сервером происходит с помощью библиотек retrofit2, okhttp, gson, rxjava.
Для работы с сетью предусмотрен модуль [network](../network/README.md)

В связи со спецификой сервера, клиенты конфигурируются следующими обьектами:
- HttpLoggingInterceptor - логгирует взаимодействие с сервером.
- SimpleCacheInterceptor - содержит логику Простого кеширования.
- ServiceInterceptor - добавляет необходимые для каждого запроса параметры, такие как token.
- ResponseTypeAdapterFactory - кроме парсинга ответа выполняет 3 дополнительные функции:
   - Логгирование ошибок парсинга в RemoteLogger
   - Конвертирование JsonSyntaxException -> ConversionException
   - Выбрасывание ApiErrorException - ошибки сервиса, приходящие в теле ответа
-CallAdapterFactory - кроме конвертирования запроса в Observable, выполняет следующие функции:
    - Конвертирует IOException в NoConnectionException
    - Конвертирует EOFException в NoContentException
    - Конвертирует JsonSyntaxException в ConversationException
    - Конвертирует HttpException в HttpError

**TODO**: открытие экранов по спец ошибкам
Открытие соответствующих экранов из CallAdapterFactory происходит через GlobalNavigator.

### Механизм ошибок

Во время запросов к серверу возможны следующие исключения:
- NoConnectionException - отсутствует подключение к интернету
- ConversionException - ошибка парсинга ответа
- HttpError - получен ответ не 2xx (например 500), может содержать BaseResponse
c описанием ошибки
- ApiErrorException - ошибки сервиса (приходят в теле ответа)
остальные исключения

Для обработки ошибок предусмотрен ErrorHandler,
стандартная его имплементация находится в базовой вью экрана и
используется в методе `BasePresenter`.

### Кеширование
cм. [здесь](../network/README.md)

[Ридми модуля](../README.md)

# Использование

Для логгирования предусмотрен синглтон Logger. Он может логгировать
с разным приоритетом:

* .v - уровень VERBOSE

* .d - DEBUG

* .i - INFO

* .w - WARN

* .e - ERROR

Logger поддерживает стратегии логгирования. Стандартная стратегия
предусматривает запись логов через Timber.

Для логгирования в Crashlytics предусмотрен объект [RemoteLogger](../../../template/base/src/main/java/ru/surfstudio/standard/base/logger/RemoteLogger.kt).

При подключении в Logger стратегии [RemoteLoggerLoggingStrategy](../../../template/base_feature/src/main/java/ru/surfstudio/standard/application/logger/strategies/remote/RemoteLoggerLoggingStrategy.kt)
в RemoteLogger отправляются:

* Все логи Logger’a выше уровня `VERBOSE`

* Exceptions, которые логгируется через `Logger#e()`,
отправляются как NonFatalExceptions(в том числе ошибки парсинга ответа сервера).

* События onPause и onResume экранов


__Чтобы не загрязнять Crashlytics используем `Logger#w()` для ожидаемых
ошибок__

Кроме того в RemoteLogger есть возможность установить id, имя и email
пользователя при входе в аккаунт и очистить при выходе.
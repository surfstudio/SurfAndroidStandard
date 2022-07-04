[Главная страница репозитория](/docs/main.md)

- [Broadcast-extension](#broadcast-extension)
- [Подключение](#подключение)
- [Использование](#использование)

# Broadcast-extension
Модуль содержит расширения для Broadcast Receiver.

# Подключение

Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо,
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано
[здесь](https://gitlab.com/surfstudio/projects/standard/android-standard/-/blob/HEAD/README.md).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.android:broadcast-extension:X.X.X"
```

# Использование

## 1. [RxBroadcastReceiver](lib-broadcast-extension/src/main/java/ru/surfstudio/android/broadcast/extension/RxBroadcastReceiver.kt)
Реактивная обертка над BroadcastReceiver, преобразующая поступающую информацию в Observable.
## 2. [BaseSmsRxBroadcastReceiver](lib-broadcast-extension/src/main/java/ru/surfstudio/android/broadcast/extension/BaseSmsRxBroadcastReceiver.kt)
Дополнение для RxBroadcastReceiver, предназначенное для перехвата и обработки получаемых SMS-сообщений.

**Для использования потребуются разрешения для доступа к смс.
Google play отслеживает использование этого разрешения и может заблокировать приложение
в случае не правомерного использования.
Подробнее прочитать об этом, а также об альтернативных способах реализации юскейсов можно здесь:
https://support.google.com/googleplay/android-developer/answer/9047303**


[Пример использования](sample)
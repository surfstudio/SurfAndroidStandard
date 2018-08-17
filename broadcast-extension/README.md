#Broadcast-extension
Модуль содержит расширения для Broadcast Receiver.

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru) необходимо,
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.android:broadcast-extension:X.X.X"
```
#Использование
##1. RxBroadcastReceiver
Реактивная обертка над BroadcastReceiver, преобразующая поступающую информацию в Observable.
##2. BaseSmsRxBroadcastReceiver
Дополнение для RxBroadcastReceiver, предназначенное для перехвата и обработки получаемых SMS-сообщений.

[Пример использования](https://bitbucket.org/surfstudio/android-standard/src/snapshot-0.3.0/broadcast-extension-sample/)

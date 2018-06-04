#Broadcast-extension
Модуль содержит расширения для broadcast-rx.
#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо,
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:broadcast-extension:X.X.X"
```
#Зависимости
Broadcast-extension зависит от модуля:

* :broadcast-rx - реактивный модуль-обёртка для BroadcastReceiver
##1. BaseSmsBroadcastReceiver
###Использование
* Добавить в Manifest: 
<uses-permission android:name="android.permission.RECEIVE_SMS" />
* Создать класс и унаследоваться от [BaseSmsBroadcastReceiver](src/main/java/ru/surfstudio/android/broadcastextension/BaseSmsBroadcastReceiver.kt )
* Переопределить метод parseSmsMessage и реализовать функционал парсинга СМС сообщения
####Пример:
```kotlin
class ExampleSmsBroadcastReceiver(context: Context) : BaseSmsBroadcastReceiver<String>(context) {
    
    override fun parseSmsMessage(smsMessage: SmsMessage?, fullBody: String): String? {
        //TODO реализовать функционал парсинга СМС сообщения
    }
}
```
* Перед подпиской обязательно запросить права на чтение и получение смс: READ_SMS, RECEIVE_SMS
* Заинжектить с помощью даггера созданный класс
* Подписаться на событие отправки широковещательного сообщения
####Пример:
```kotlin
        subscribe(exampleSmsBroadcastReceiver.observeBroadcast(), {
            //TODO обработка полученного результата
        }
```

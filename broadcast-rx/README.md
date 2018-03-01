#RxBroadcastReceiver
Реактивный модуль-обёртка для BroadcastReceiver.
##Зависимости
Broadcast-extension зависит от модуля:

* :logger - модуль для логирования

##Использование
* Необходимо создать класс и унаследоваться от [RxBroadcastReceiver](src/main/java/ru/surfstudio/android/broadcastrx/RxBroadcastReceiver.kt)
* Переопределить метод parseBroadcastIntent и реализовать парсинг пришедшего интента
* Переопределить метод intentFilter
###Пример:
```kotlin
class ExampleBroadcastReceiver(context: Context, intentFilter: IntentFilter) : RxBroadcastReceiver<String>(context, intentFilter) {

    override fun parseBroadcastIntent(intent: Intent): String? {
        //TODO Реализовать парсинг пришедшего интента
    }
    
    override fun intentFilter() = IntentFilter("ru.surfstudio.android.example")

}
```
* Заинжектить через даггер созданный класс
* Подписаться на событие отправки широковещательного сообщения
###Пример:
```kotlin
        subscribe(exampleBroadcastReceiver.observeBroadcast(), {
            //TODO обработка полученного результата
        }
```
##Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо,
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).

Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:broadcast-rx:X.X.X"
```
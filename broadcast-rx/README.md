#RxBroadcastReceiver
Реактивный модуль-обёртка для BroadcastReceiver.
##Использование
1. Необходимо создать класс и унаследоваться от [RxBroadcastReceiver](src/main/java/ru/surfstudio/android/broadcastrx/RxBroadcastReceiver.kt)
2. Переопределить метод parseBroadcastIntent и реализовать парсинг пришедшего интента
###Пример:
```kotlin
class ExampleBroadcastReceiver(context: Context, intentFilter: IntentFilter) : RxBroadcastReceiver<String>(context, intentFilter) {

    override fun parseBroadcastIntent(intent: Intent): String? {
        //TODO Реализовать парсинг пришедшего интента
    }
}
```
4. Подписаться на событие отправки широковещательного сообщения
###Пример:
```kotlin
        ExampleBroadcastReceiver(context, IntentFilter("ru.surfstudio.android.example")).observeBroadcast().subscribe {
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
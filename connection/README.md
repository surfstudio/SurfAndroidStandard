#Connection
Предоставляет набор классов для работы с состоянитем сети.

#Использование
Класс `ConnectionProvider`, позволяет синхронно или асинхронно получать состояние интернет соединения
Необходимо добавить разрешение `ACCESS_NETWORK_STATE` в манифест

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:connection:X.X.X"
```
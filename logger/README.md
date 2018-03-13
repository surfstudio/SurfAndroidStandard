#Logger
Модуль для логирования в logcat и на сервер. Основано на [timber](https://github.com/JakeWharton/timber)
Передоставлет статические методы для логирования на уровнях
+ verbose
+ debug
+ info
+ warning
+ error

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:logger:X.X.X"
```
#Core app
Основано на [ferro](https://github.com/MaksTuev/ferro).

Содержит базовую часть ядра для построения приложения.

#Использование
Наследуемся от CoreApp.

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:core-app:X.X.X"
```
#Core ui
Основано на [ferro](https://github.com/MaksTuev/ferro).

Модуль для построения графическго интерфейса

Содержит в себе классы для 
+ работы с Activity и Fragment
+ Навигации между View
+ базовые модели экранов
+ состояния экранов

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:core-ui:X.X.X"
```
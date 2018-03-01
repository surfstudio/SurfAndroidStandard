#Firebase analytics 
Реализует логику работы с [аналитикой](../analytics) с использованием firebase

#Использование
Передаем `FirebaseApi` в [AnalyticsStore](../analytics/src/main/java/ru/surfstudio/android/analytics/store/AnalyticsStore.java)

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:firebase-analytics:X.X.X"
```
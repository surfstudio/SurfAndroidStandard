#Gson converter
Модуль для парсинга json ответов сервера

#Использование
1. Добавить ResponseTypeAdapterFactory в билдер gson`a
    ```java
    GsonBuilder()
    .registerTypeAdapterFactory(responseTypeAdapterFactory)
    .create()
    ```
1. Добавить `gson` в билдер ретрофита
    ```java
    Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(apiUrl.toString())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build()
    ```

#Подключение
Для подключения данного модуля из [Artifactory Surf](http://artifactory.surfstudio.ru), необходимо, 
чтобы корневой `build.gradle` файл проекта был сконфигурирован так, как описано 
[здесь](https://bitbucket.org/surfstudio/android-standard/overview).
  
Для подключения модуля через Gradle:
```
    implementation "ru.surfstudio.standard:converter-gson:X.X.X"
```
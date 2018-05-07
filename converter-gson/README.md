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
Gradle:
```
    implementation "ru.surfstudio.android:converter-gson:X.X.X"
```
# Gson converter
Модуль для парсинга json ответов сервера

# Использование
1. Добавить [`ResponseTypeAdapterFactory`][rtaf] в билдер gson`a
    ```
    GsonBuilder()
        .registerTypeAdapterFactory(responseTypeAdapterFactory)
        .create()
    ```
2. Добавить `gson` в билдер ретрофита
    ```
    Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(apiUrl.toString())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(callAdapterFactory)
        .build()
    ```

[Пример использования в приложении](/deprecated/network/sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:converter-gson:X.X.X"
```

[rtaf]: lib-converter-gson/src/main/java/ru/surfstudio/android/converter/gson/ResponseTypeAdapterFactory.java
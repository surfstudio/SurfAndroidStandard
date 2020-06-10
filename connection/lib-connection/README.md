# Connection
Предоставляет набор классов для работы с состоянитем сети.

# Использование
Класс [`ConnectionProvider`][cp], позволяет синхронно или асинхронно получать
состояние интернет соединения.
Необходимо добавить разрешение `ACCESS_NETWORK_STATE` в манифест.

[Пример использования в приложении](../network-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:connection:X.X.X"
```

[cp]: src/main/java/ru/surfstudio/android/connection/ConnectionProvider.java
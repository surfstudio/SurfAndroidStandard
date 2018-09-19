#Security
Используется для обеспечения безопасности приложения.

#Использование
Основные классы:

1. AppDebuggableChecker - класс, проверяющий debuggable-флаги приложения при его запуске.
1. RootChecker - проверяет наличие рут-прав на устройстве.
1. SecureStorage - защищенное хранилище с AES-шифрованием.
1. CertificatePinnerCreator - класс, создающий CertificatePinner для OkHttpClient для реализации ssl-pinning.

[Пример использования](../security-sample)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:security:X.X.X"
```
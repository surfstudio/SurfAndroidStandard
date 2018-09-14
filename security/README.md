#Security
Используется для обеспечения безопасности приложения.

#Использование
Основные классы:

1. RootChecker - проверяет наличие рут-прав на устройстве.
1. SecureStorage - защищенное хранилище с AES-шифрованием.
1. SrpChallengeInteractor - класс, реализующий SRP-протокол.
1. CertificatePinnerCreator - класс, реализующий ssl-pinning.
1. ConnectionSpecModule - модуль, поставляющий ConnectionSpec для использования TLS на Android ниже 5.0.
1. SessionManager - класс, отвечающий за сброс сессии.

[Пример использования](../security-sample)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:security:X.X.X"
```
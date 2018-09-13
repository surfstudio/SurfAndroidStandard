#Security
Используется для обеспечения безопасности приложения.

#Использование
Основные классы:

1. RootChecker - проверяет наличие рут-прав на устройстве.
2. WrongEnterAttemptStorage - хранилище попыток неудачного входа в приложение.
3. SecureStorage - защищенное хранилище с AES-шифрованием.
4. SrpChallengeInteractor - класс, реализующий SRP-протокол.
5. CertificatePinnerCreator - класс, реализующий ssl-pinning.
6. ConnectionSpecModule - модуль, поставляющий ConnectionSpec для использования TLS на Android ниже 5.0.
7. SessionManager - класс, отвечающий за сброс сессии.

[Пример использования](../security-sample)

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:security:X.X.X"
```
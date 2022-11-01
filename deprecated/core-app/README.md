[Главная](/docs/main.md)

# Core app
Этот модуль устарел, не используйте.

Используется для конфигурации всего приложения и предоставляет полезные
классы для реализации

# Использование
Основные классы:

1. [`ActiveActivityHolder`][aah] - содержит ссылку на текущую активити.
Можно получит из CoreApp
2. [`StringProvider`][sp] - провайдер строковых ресурсов. Позволяет скрыть
использование контекста для доступа к ресурсам.

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:core-app:X.X.X"
```

[aah]: lib-core-app/src/main/java/ru/surfstudio/android/core/app/ActiveActivityHolder.java
[sp]: lib-core-app/src/main/java/ru/surfstudio/android/core/app/StringsProvider.kt
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

[aah]: src/main/java/ru/surfstudio/android/core/app/ActiveActivityHolder.java
[sp]: ../../../template/base/src/main/java/ru/surfstudio/standard/base/util/StringsProvider.kt
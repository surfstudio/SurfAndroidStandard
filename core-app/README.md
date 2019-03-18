[Главная](../docs/main.md)

[TOC]

# Core app
Этот модуль устарел, не исползуйте.

Используется для конфигурации всего приложения и предоставляет полезные
классы для реализации

# Использование
Основные классы:

1. [`CoreApp`][ca] - базовый класс приложения, скрывает часто используемую
настройку окружения. **Не является необходимым для других модулей**
2. [`ActiveActivityHolder`][aah] - содержит ссылку на текущую активити.
Можно получит из CoreApp
3. [`StringProvider`][sp] - провайдер строковых ресурсов. Позволяет скрыть
использование контекста для доступа к ресурсам.

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:core-app:X.X.X"
```

[ca]: src/main/java/ru/surfstudio/android/core/app/CoreApp.java
[aah]: src/main/java/ru/surfstudio/android/core/app/ActiveActivityHolder.java
[sp]: ../template/base/src/main/java/ru/surfstudio/standard/base/app/StringsProvider.kt
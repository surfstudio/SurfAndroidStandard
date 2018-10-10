[Главная](../docs/main.md)

[TOC]

# Analytics
Содержат в себе набор классов для регистрации событий в приложении для последующей аналитики.
Имеет только обобщенные интерфейсы без конкретной реализации. Поддерживает работу сразу с несколькими реализациями.

# Использование
[Пример использования](../firebase-sample)

Основные классы:
* [`Analytics`][a]
* [`AnalyticsStore`][as]
* [`Event`][e]
* [`EventData`][ed]
* [`BaseAnalyticsService`][bas]

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:analytics:X.X.X"
```

[a]: src/main/java/ru/surfstudio/android/analytics/Analytics.java
[as]: src/main/java/ru/surfstudio/android/analytics/store/AnalyticsStore.java
[e]: src/main/java/ru/surfstudio/android/analytics/event/Event.java
[ed]: src/main/java/ru/surfstudio/android/analytics/event/EventData.java
[bas]: src/main/java/ru/surfstudio/android/analytics/BaseAnalyticsService.java
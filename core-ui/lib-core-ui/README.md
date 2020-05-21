[Главная страница репозитория](/docs/main.md)

[TOC]

# Core ui
Создан в результате развития идей проекта [ferro](https://github.com/MaksTuev/ferro).

Модуль расширяющий возможности ui части Android Framework.
Может быть использован для создания базовых классов, необходимых для реализации паттернов MVP, MVVM

### Основные сущности:

1. [`PersistentScope`][ps] - хранилище для остальных сущностей,
   переживает смену конфигурации.
2. [`PersistentScopeStorage`][pss] - хранилище всех PersistentScope в контексте приложения
3. [`ScreenState`][ss] - текущее состояние экрана (пр. был ли он пересоздан в
результате смены конфигурации или восстановлен с диска)
4. `Configurator` - используется для настройки DI
    * [`BaseActivityConfigurator`][bac]
    * [`BaseFragmentConfigurator`][bfc]
    * [`Configurator`][c]
5. [`ScreenEventDelegateManager`][sedm] - позволяет подписываться на системные события
экрана, такие как onActivityResult, onCompletelyDestroy, onNewIntent и др.

Все эти сущности, переживают смену конфигурации, таким образом решается
большая часть проблем, связанных с этим свойством фреймворка.

### Дополнительные сущности:

1. [`ActivityProvider`][ap], [`FragmentProvider`][fp] - предоставляют "живые" Activity
или Fragment даже после смены конфигурации

Механизм делегирования событий экрана (см ScreenEventDelegateManager)
позволяет создавать законченные сущности, которые необходимы для "чистой"
архитектуры.

Большинство асинхронных взаимодействий осуществляются через rxJava. 

# Использование
[Пример использования](../sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:core-ui:X.X.X"
```

# Ссылки
[Архитектура приложений Surf](/docs/common/architect.md)

[bac]: src/main/java/ru/surfstudio/android/core/ui/configurator/BaseActivityConfigurator.java
[bfc]: src/main/java/ru/surfstudio/android/core/ui/configurator/BaseFragmentConfigurator.java
[c]: src/main/java/ru/surfstudio/android/core/ui/configurator/Configurator.java
[ps]: src/main/java/ru/surfstudio/android/core/ui/scope/PersistentScope.java
[pss]: src/main/java/ru/surfstudio/android/core/ui/scope/PersistentScopeStorage.java
[ss]: src/main/java/ru/surfstudio/android/core/ui/state/ScreenState.java
[sedm]: src/main/java/ru/surfstudio/android/core/ui/event/ScreenEventDelegateManager.java
[nav]: docs/navigation.md
[pm]: src/main/java/ru/surfstudio/android/core/ui/permission/PermissionManager.kt
[ap]: src/main/java/ru/surfstudio/android/core/ui/provider/ActivityProvider.java
[fp]: src/main/java/ru/surfstudio/android/core/ui/provider/FragmentProvider.java
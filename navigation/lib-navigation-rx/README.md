[Главная страница репозитория](/docs/main.md)

# Navigation-Rx

Модуль с обертками из библиотеки RxJava 2 над различными слушателями из модулей [navigation](../lib-navigation), [navigation-observer](../lib-navigation-observer).

Основные сущности:

- [ActiveTabReopenedObservable](active-tab-obs) - observable, являющийся оберткой над событием повторного открытия
таба через TabFragmentNavigator.

- [FragmentBackStackChangedObservable](backstack-obs) - observable, являющийся оберткой над событием изменения
бекстека фрагмента.

- [ListenForResultObservable](result-obs) - observable, являющийся оберткой над оповещением о результате работы экрана.

Также модуль содержит удобные экстеншны для подписки прямо на сущности экранов:

- `FragmentNavigatorInterface.observeBackStackChanged()` - подписка на изменение бекстека
- `TabFragmentNavigatorInterface.observeActiveTabReopened()` - подписка на повторные открытия уже открытого таба.
- `ScreenResultObserver.observeScreenResult(targetRoute)` - подписка на результат работы route.


## Использование
[Пример использования без архитектурных подходов](../sample/)
[Пример использования Surf MVP + Dagger](../sample-standard/)

## Подключение

Gradle:
```
    implementation "ru.surfstudio.android:navigation-rx:X.X.X"
```

[active-tab-obs]: /src/main/java/ru/surfstudio/android/navigation/rx/ActiveTabReopenedObservable.kt
[backstack-obs]: /src/main/java/ru/surfstudio/android/navigation/rx/FragmentBackStackChangedObservable.kt
[result-obs]: /src/main/java/ru/surfstudio/android/navigation/rx/ListenForResultObservable.kt

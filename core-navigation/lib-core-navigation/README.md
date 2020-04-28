[TOC]

# Core navigation
Является расширением [`core-ui`][core-ui]-модуля для работы с навигацией между экранами приложения.

# Основные маршруты и навигаторы

Общая информация о том, что такое Route - [здесь][nav].

Базовый интерфейс [`Route`][i_route] также предоставляет константы для обозначения
ключей в Intent или Bundle.

Предусмотрены следующие навигаторы:

- [`GlobalNavigator`][global];

- [`ActivityNavigator`][act];

    - [`ActivityNavigatorForActivity`][act_for_act]

    - [`ActivityNavigatorForFragment`][act_for_fr]

- [`FragmentNavigator`][f-nav];

    - [`TabFragmentNavigator`][tab];

    - [`ChildFragmentBavigator`][child];

Навигаторы работают с определенными `Route`(Activity, Fragment, Dialog).
**Но `Route` может быть использован отдельно от навигатора!**

* Активити

    * [`ActivityRoute`][ar]
    * [`ActivityWithParamsRoute`][awpr]
    * [`ActivityWithResultRoute`][awrr]
    * [`ActivityWithParamsAndResultRoute`][awparr]
    * [`NewIntentRoute`][nir]

* Фрагменты
    * [`FragmentRoute`][fr]
    * [`FragmentWithParamsRoute`][fwpr]
    * [`RootFragmentRoute`][rfr]

Навигация для диалогов предоставляется модулем [mvp-dialog][dial]

Если необходимо стартовать экран с последующим получением результата,
необходимо зарегистрировать обработчик этого события через
`АctivityNavigator#observeActivityResult` в презентере. При этом подписаться
следует один раз за время жизни презентера (например в `onFirstLoad()`).

### Подключение

Gradle:
```
    implementation "ru.surfstudio.android:core-navigation:X.X.X"
```

[core-ui]: ../../core-ui/lib-core-ui/README.md
[act]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigator.java
[f-nav]: /src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/FragmentNavigator.java
[tab]:  /src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/tabfragment/TabFragmentNavigator.kt
[child]: /src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/ChildFragmentNavigator.java
[global]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/GlobalNavigator.java
[act_for_act]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigatorForActivity.java
[act_for_fr]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigatorForFragment.java
[ar]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityRoute.java
[awpr]:  /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithParamsRoute.java
[awrr]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithResultRoute.java
[awparr]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithParamsAndResultRoute.java
[nir]: /src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/NewIntentRoute.java
[fr]: /src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/FragmentRoute.java
[fwpr]: /src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/FragmentWithParamsRoute.java
[rfr]: /src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/RootFragmentRoute.kt
[nav]: ../../docs/ui/navigation.md
[dial]: ../../mvp/lib-mvp-dialog/README.md
[i_route]: /src/main/java/ru/surfstudio/android/core/ui/navigation/Route.java
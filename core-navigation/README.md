# Core navigation

- [Основные маршруты и навигаторы](#основные-маршруты-и-навигаторы)
- [Использование](#использование)
  - [Подключение](#подключение)

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

# Использование
[Пример использования](sample)

### Подключение

Gradle:
```
    implementation "ru.surfstudio.android:core-navigation:X.X.X"
```

[core-ui]: /core-ui/README.md
[act]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigator.java
[f-nav]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/FragmentNavigator.java
[tab]:  lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/tabfragment/TabFragmentNavigator.kt
[child]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/ChildFragmentNavigator.java
[global]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/GlobalNavigator.java
[act_for_act]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigatorForActivity.java
[act_for_fr]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigatorForFragment.java
[ar]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityRoute.java
[awpr]:  lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithParamsRoute.java
[awrr]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithResultRoute.java
[awparr]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithParamsAndResultRoute.java
[nir]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/NewIntentRoute.java
[fr]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/FragmentRoute.java
[fwpr]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/FragmentWithParamsRoute.java
[rfr]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/RootFragmentRoute.kt
[nav]: /docs/ui/navigation.md
[dial]: /mvp/lib-mvp-dialog/README.md
[i_route]: lib-core-navigation/src/main/java/ru/surfstudio/android/core/ui/navigation/Route.java
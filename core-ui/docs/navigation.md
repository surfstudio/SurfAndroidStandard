[TOC]

# Основные маршруты и навигаторы

Общая информация о том, что такое Route - [здесь][nav].

Предусмотрены следующие навигаторы:

- [`GlobalNavigator`][global];

- [`ActivityNavigator`][act];

    - [`ActivityNavigatorForActivity`][act_for_act]

    - [`ActivityNavigatorForFragment`][act_for_fr]

- [`FragmentNavigator`][f-nav];

    - [`TabFragmentNavigator`][tab];

    - [`ChildFragmentBavigator`][child];

Каждый навигатор имеет набор базовых классов `Route`.

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

Передача параметров при старте экрана происходит через `Route`
и эти параметры получает презентер в конструктор также обернутые в `Route`.
Для упрощения этой передачи следует создать базовый класс для модуля экрана
`CustomScreenModule`.

``` kotlin
@Module
abstract class CustomScreenModule<out R : Route>(private val route: R) {

    @Provides
    @PerScreen
    fun provideRoute(): R {
        return route
    }
}
```

Если необходимо стартовать экран с последующим получением результата,
необходимо зарегистрировать обработчик этого события через
`АctivityNavigator#observeActivityResult` в презентере.


[core-ui]: ../README.md
[dial]: ../../mvp-dialogs/README.md
[act]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigator.java
[f-nav]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/FragmentNavigator.java
[tab]:  ../src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/tabfragment/TabFragmentNavigator.kt
[child]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/ChildFragmentNavigator.java
[global]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/GlobalNavigator.java
[act_for_act]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigatorForActivity.java
[act_for_fr]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/navigator/ActivityNavigatorForFragment.java
[ar]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityRoute.java
[awpr]:  ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithParamsRoute.java
[awrr]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithResultRoute.java
[awparr]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/ActivityWithParamsAndResultRoute.java
[nir]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/activity/route/NewIntentRoute.java
[fr]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/FragmentRoute.java
[fwpr]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/FragmentWithParamsRoute.java
[rfr]: ../src/main/java/ru/surfstudio/android/core/ui/navigation/fragment/route/RootFragmentRoute.kt
[nav]: ../../docs/ui/navigation.md
[dial]: ../../mvp-dialog/README.md
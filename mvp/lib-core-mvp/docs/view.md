[Главная](../../docs/main.md)

[Ридми модуля](../README.md)

[TOC]

#### View

Слой View является максимально пассивным и выполняет 2 задачи:
- Показывает информацию пользователю;
- Передает пользовательские события в Presenter.

Под View подразумевается не только android.View, но и Activity, Fragment,
Dialog или [Widget][widget].

Для создания View экрана следует наследоваться от одного из классов:

- [`CoreActivityView`][core]

- [`BaseRenderableActivityView`][render] - поддерживает отрисовку и обработку ошибок из презентера

- [`BaseLdsActivityView`][lds] - поддерживает состояния загрузки данных на экране

- [`BaseLdsSwrActivityView`][swr] - поддерживает состояние обновления экрана

аналогично для FragmentView.

см [типы базовых классов вью](#базовые-классы-и-интерфейсы-вью)

Взаимодействие частей View (например Adapter, кастомная android.View)
напрямую с презентером запрещено.
Только View может взаимодействовать с презентером.

Если экран не содержит логики, то следует наследоваться от `CoreActivityView`
или `CoreFragmentView`.

Api вью может иметь только методы вида `void someMethod(params...)`,
это нужно для соответствия принципу [Unidirectional data flow][presenter].

##### Базовые классы и интерфейсы вью

###### Классы Activity
Классы указаны в порядке наследования.

* [`CoreActivityView`][core] - Класс с корневой логикой вью

* [`BaseRenderableActivityView`][render] - базовый класс для ActivityView,
поддерживающий отрисовку ScreenModel и обработку ошибок

* [`BaseLdsActivityView`][lds]- базовый класс ActivityView c поддержкой
состояния загрузки [`LoadState`][ls]
Используется вместе с [PlaceHolderView][placeholder].

* [`BaseLdsSwrActivityView`][swr] - базовый класс ActivityView c поддержкой:

    * состояния загрузки [`LoadState`][ls]

    * состояния SwipeRefresh [`SwipeRefreshState`][sws]

Этот базовый класс можно **не использовать**, если с большой гарантией можно
предвидеть, что пользователю **не потребуется** обновлять контент.

###### Классы Fragment
Для фрагментов предусмотрены аналогичные базовые классы.

* [`CoreFragmentView`][core_f]

* [`BaseRenderableFragmentView`][render_f]

* [`BaseLdsFragmentView`][lds_f]

* [`BaseLdsSwrFragmentView`][swr_f]


**Замечание**:
при создании экрана:
* `#getScreenName()` - имя экрана для логирования(не использовать имя класса
через рефлексию из-за обфускации)

###### Кастомные вью

Лучшие практики по созданию кастомных виджетов - [здесь][custom].

Интерфейсы

   * [`CoreView`][core_v] - базовый интерфейс для всех кастомных вью

   * [`PresenterHolderCoreView`][presenter_holder] - может быть использован
   для кастомных android.View c поддержкой презентера.

   * [`RenderableView`][render_v] - для кастомных вью с поддержкой
   отрисовки модели экрана


Также есть возможность использовать презентеры в кастомных android.View,
для существует два способа:

1. заинжектить этот презентер в базовую вью
(Activity или Fragment, реализующие интерфейс `PresenterHolderCoreView`)
и переопределить метод `bindPresenters`,
в котором связать кастомную android.View с [презентером][presenter]
(вызвать у презентера метод `attachView()`).

1. использовать [виджеты][widget] - кастомные вью с презентером, которые создаются
аналогично экрану.

[core]: ../src/main/java/ru/surfstudio/android/core/mvp/activity/CoreActivityView.java
[render]: ../src/main/java/ru/surfstudio/android/core/mvp/activity/BaseRenderableActivityView.java
[lds]: ../src/main/java/ru/surfstudio/android/core/mvp/activity/BaseLdsActivityView.java
[swr]: ../src/main/java/ru/surfstudio/android/core/mvp/activity/BaseLdsSwrActivityView.java
[core_f]: ../src/main/java/ru/surfstudio/android/core/mvp/fragment/CoreFragmentView.java
[render_f]: ../src/main/java/ru/surfstudio/android/core/mvp/fragment/BaseRenderableFragmentView.java
[lds_f]: ../src/main/java/ru/surfstudio/android/core/mvp/fragment/BaseLdsFragmentView.java
[swr_f]: ../src/main/java/ru/surfstudio/android/core/mvp/fragment/BaseLdsSwrFragmentView.java
[core_v]: ../src/main/java/ru/surfstudio/android/core/mvp/view/CoreView.java
[render_v]: ../src/main/java/ru/surfstudio/android/core/mvp/view/RenderableView.java
[presenter_holder]: ../src/main/java/ru/surfstudio/android/core/mvp/view/PresenterHolderCoreView.java
[presenter]: presenter.md
[custom]: ../../../docs/ui/custom_views.md
[ls]: ../../../template/base_feature/src/main/java/ru/surfstudio/standard/ui/placeholder/LoadState.kt
[sws]: ../src/main/java/ru/surfstudio/android/core/mvp/model/state/SwipeRefreshState.java
[placeholder]: ../../../custom-view/lib-custom-view/README.md
[widget]: ../../../mvp/lib-mvp-widget/README.md
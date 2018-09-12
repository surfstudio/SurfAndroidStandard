[TOC]

#### View

Слой View является максимально пассивным и выполняет 2 задачи:
- Показывает информацию пользователю;
- Передает пользовательские события в Presenter.

Для создания View экрана следует наследоваться от одного из классов:
- [`CoreActivityView`][core]
- [`BaseRenderableActivityView`][render] - поддерживает отрисовку и обработку ошибок из презентера
- [`BaseLdsActivityView`][lds] - поддерживает состояния загрузки данных на экране
- [`BaseLdsSwrActivityView`][swr] - поддерживает состояние обновления экрана

аналогично для FragmentView.

см [типы базовых классов вью](#базовые-классы-и-интерфейсы-вью)

Если какая-то область экрана (View) является логически связанной,
то следует обернуть ее в кастомную android.ViewGroup,
даже если она не будет переиспользоваться на других экранах.
Взаимодействие частей View (например Adapter, кастомная android.View)
напрямую с презентером запрещено.
Только View может взаимодействовать с презентером.

Также есть возможность использовать презентеры в кастомных android.View,
для этого необходимо:
 * заинжектить этот презентер в базовую вью
(Activity или Fragment реализующие интерфейс PresenterHolderCoreView)
и переопределить метод bindPresenters,
в котором связать кастомную android.View с [презентером][presenter]

Если экран не содержит логики, то следует наследоваться от CoreActivityView
или CoreFragmentView.

Api вью может иметь только методы вида void someMethod(params...),
это нужно для соответствия принципу [Unidirectional data flow][presenter].

##### Базовые классы и интерфейсы вью

###### Классы Activity
Классы указаны в порядке наследования.
* [`CoreActivityView`][core] - Класс с корневой логикой вью
* [`BaseRenderableActivityView`][render] - базовый класс для ActivityView,
поддерживающий отрисовку ScreenModel и обработку ошибок
* [`BaseLdsActivityView`][lds]- базовый класс ActivityView c поддержкой
состояния загрузки {@link LoadState}
Используется вместе с [PlaceHolderView].
* [`BaseLdsSwrActivityView`][swr] - базовый класс ActivityView c поддержкой:
   * состояния загрузки {@link LoadState}
   * состояния SwipeRefresh {@link SwipeRefreshState}
Этот базовый класс можно **не использовать**, если с большой гарантией можно
предвидеть, что пользователю **не потребуется** обновлять контент

###### Классы Fragment
Для фрагментов предусмотрены аналогичные базовые классы.

* [`CoreFragmentView`][core_f]
* [`BaseRenderableFragmentView`][render_f]
* [`BaseLdsFragmentView`][lds_f]
* [`BaseLdsSwrFragmentView`][swr_f]

###### Кастомные вью

Лучшие практики по созданию кастомных виджетов - [здесь][custom].

Интерфейсы
   * [`CoreView`][core_v] - базовый интерфейс для всех кастомных вью
   * [`PresenterHolderCoreView`][presenter_holder] - может быть использован
   для кастомных android.View c поддержкой презентера.
   * [`RenderableView`][render_v] - для кастомных вью с поддержкой
   отрисовки модели экрана

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
[custom]: ../../docs/ui/custom_views.md

[Главная](../../../docs/main.md)

[Ридми модуля](../README.md)

#### ScreenModel
Для хранения состояния экрана используются модели экрана - ScreenModel.
Они переживают смену ориентации.

Модель при отрисовке должна *полностью* переводить вью в *необходимое состояние*,
любой другой механизм для изменения состояния вью *использовать **запрещено***.
**Разовое отображение SnackBar и другие разовые действия не относятся к состоянию.**

Предусмотрены базовые классы ScreenModel,
поддерживающие различные состояния, например `LoadState` (состояние плейсхолдера).
Работают в связке с соответствующим [базовым классом Вью][view].

Если какое либо конкретная часть состояния вью выводится из данных `ScreenModel`,
то метод вывода этого состояния должен быть в `ScreenModel`,
например: `ScreenModel` содержит модель корзины, которая в свою очередь
содержит информацию об акциях.
Если в зависимости от наличия акций необходимо показывать на UI блок с акциями,
то `ScreenModel` должна содержать метод `boolean shouldRenderActions()`,
в котором будет проверка акций.

Или если другими словами, **оставляем во вью как можно меньше логики,
чтобы потом можно было протестировать ее Unit тестами**.


Основные классы:
* [`ScreenModel`][sm] - базовая модель экрана
* [`LdsScreenModel`][lsm] - модель экрана с поддержкой состояния загрузки
* [`LdsSwrScreenModel`][lssm] - поддерживает pull-to-refresh(ПТР)
* [`LdsPgnScreenModel`][lpsm] - поддерживает состояние пагинации
* [`LdsSwrPgnScreenModel`][lspsm] - поддерживает и ПТР и пагинацию

Состояния:
* [`LoadState`][ls]
* [`SwipeRefreshState`][sws]

Для использования на конкретном экране необходимо наследоваться от одного
из этих классов, учитывая их специфику.

[sm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/ScreenModel.java
[lsm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsScreenModel.java
[lssm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsSwrScreenModel.java
[lpsm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsPgnScreenModel.java
[lspsm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsSwrPgnScreenModel.java
[ls]: ../../../template/base_feature/src/main/java/ru/surfstudio/standard/ui/placeholder/LoadState.kt

[sws]:../src/main/java/ru/surfstudio/android/core/mvp/model/state/SwipeRefreshState.java

[view]: view.md
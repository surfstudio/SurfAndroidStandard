[Главная](../../docs/main.md)

[Ридми модуля](../README.md)

[TOC]

#### ScreenModel
Для хранения состояния экрана используются модели экрана - ScreenModel.
Они переживают смену ориентации.

Основные классы:
* [`ScreenModel`][sm] - базовая модель экрана
* [`LdsScreenModel`][lsm] - модель экрана с поддержкой состояния загрузки
* [`LdsSwrScreenModel`][lssm] - поддерживает pull-to-refresh(ПТР)
* [`LdsPgnScreenModel`][lpsm] - поддерживает состояние пагинации
* [`LdsSwrPgnScreenModel`][lspsm] - поддерживает и ПТР и пагинацию

Состояния:
* [`LoadState`][ls]
* [`SwipeRefreshState`][sws]

Для использования наконкретном экране необходимо наследоваться от одного
из этих классов, учитывая их специфику.

[sm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/ScreenModel.java
[lsm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsScreenModel.java
[lssm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsSwrScreenModel.java
[lpsm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsPgnScreenModel.java
[lspsm]: ../src/main/java/ru/surfstudio/android/core/mvp/model/LdsSwrPgnScreenModel.java

[ls]: ../src/main/java/ru/surfstudio/android/core/mvp/model/state/LoadState.java
[sws]:../src/main/java/ru/surfstudio/android/core/mvp/model/state/SwipeRefreshState.java
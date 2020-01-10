[Главная страница репозитория](../docs/main.md)

[TOC]

# MVP widget

Расширение модуля [core-mvp][core-mvp] для работы со view
(в терминах android фреймвока) как со View(в терминах MVP).

Виджет(Widget) - это кастомная вью со своим Presenter. Для них справедливы
все те [правила, что и для обычных кастомных вью][custom].

Плюс к этому виджеты могут выручить в ситуации, когда экран перегружен
фрагментами. Они вполне могут их заменить и при этом виджеты "легче",
чем фрагменты.

[Отличная презентация на тему][pres]

Основные классы:

1. [`CoreFrameLayoutView`][frame]
2. [`CoreLinearLayoutView`][linear]
3. [`CoreRelativeLayoutView`][relative]
4. [`CoreConstraintLayoutView`][contraint]

Конфигураторы:

* [`BaseWidgetViewConfigurator`][conf]

### Подключение

Gradle:
```
    implementation "ru.surfstudio.android:mvp-widget:X.X.X"
```

### Использование

Для создания виджета необходимо сделать шаги, аналогичные созданию экрана:

1. Наследоваться от одного из базовых виджетов
1. Убедиться в уникальности идентификатора виджета: 
    1. Если виджет используется внутри верстки, необходимо явно задать ему `android:id` в XML
    1. Если виджет добавляется динамически, необходимо либо явно задать `id` программно,
    либо переопределить метод `getWidgetId`
    1. При добавлении виджета в `RecyclerView` **строго рекомендуется**
    переопределить метод `getWidgetId` на основе уникальных данных,
    получаемых внутри метода `ViewHolder.bind`.
1. Расширить базовый презентер
1. Расширить `BaseWidgetConfigurator`
1. Опционально: создать ScreenModel, и реализовать один из [базовых интерфейсов](src/main/java/ru/surfstudio/android/mvp/widget/base)

[custom]: ../../docs/ui/custom_views.md
[frame]: src/main/java/ru/surfstudio/android/mvp/widget/view/CoreFrameLayoutView.java
[linear]: src/main/java/ru/surfstudio/android/mvp/widget/view/CoreLinearLayoutView.java
[relative]: src/main/java/ru/surfstudio/android/mvp/widget/view/CoreRelativeLayoutView.java
[contraint]: src/main/java/ru/surfstudio/android/mvp/widget/view/CoreConstraintLayoutView.java
[conf]: src/main/java/ru/surfstudio/android/mvp/widget/configurator/BaseWidgetViewConfigurator.java
[pres]: https://docs.google.com/presentation/d/184of9d-fYnNXu9IHegDddK9lT5i7KU4II-rgijKQIh4/edit#slide=id.p
[core-mvp]: ../../mvp/lib-core-mvp/


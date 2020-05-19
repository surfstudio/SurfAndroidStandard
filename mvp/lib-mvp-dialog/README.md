[Главная страница репозитория](/docs/main.md)

[TOC]

# MVP dialog

Расширение модуля [core-mvp][core_mvp] для работы с диалогами.

В наших приложениях мы не используем напрямую диалоги из Android Framework.

Предоставляет 2 парадигмы работы с диалогами:

1. Диалог как часть родительского вью, события диалога в этом случае
получает презентер родительского вью (см [`CoreSimpleDialogFragment`](#coresimpledialogfragment))
2. Диалог с собственным презентером, родительский презентер в этом случае
может получить событие с диалога через RxBus (см [`CoreDialogFragmentView`](#coredialogfragmentview))

# Использование
[Пример использования](../sample-mvp-dialog/)


# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:mvp-dialog:X.X.X"
```

# Описание классов

Для диалогов предусмотрен базовые классы

- [`CoreSimpleDialogFragment`][simple];

- [`CoreDialogFragmentView`][core];

- [`CoreBottomSheetDialogFragmentView`][bottom]

- [`CoreSimpleBottomSheetDialogFragment`][bottom_simple]

#### CoreSimpleDialogFragment

Базовый класс простого диалога который может возвращать результат
У этого диалога презентер не предусмотрен.
Простой диалог рассматривается как часть родителького View и оповещает презентер о событиях
пользователя прямым вызовом метода презентера.
Для получения презентера в диалоге предусмотрен метод #getScreenComponent(Class),
который возвращает компонент родительского экрана.

Этот диалог следует расширять если не требуется реализация сложной логики в диалоге и обращение
к слою Interactor.

#### CoreSimpleBottomSheetDialogFragment

Простой BottomSheetDialog без презентера, аналогичен `CoreSimpleDialogFragment`.

#### CoreDialogFragmentView
Диалог с собственным презентером.
Следует использовать если в диалоге есть сложная логика или обращение к слою данных.
Для оповещения о результате выполнения диалога следует использовать шину (RxBus)

#### CoreBottomSheetDialogFragmentView

BottomSheetDialog с собственным презентером.

# Навигация
Общая информация по навигации - [здесь](/docs/ui/navigation.md).

Открытие диалогов осуществляется из презентера через [**DialogNavigator**][nav].

Предусмотрены базовые маршруты:

 - [DialogRoute][dr]

 - [DialogWithParamsRoute][dwpr]

[simple]: src/main/java/ru/surfstudio/android/mvp/dialog/simple/CoreSimpleDialogFragment.java
[core]: src/main/java/ru/surfstudio/android/mvp/dialog/complex/CoreDialogFragmentView.java
[bottom]: src/main/java/ru/surfstudio/android/mvp/dialog/complex/CoreBottomSheetDialogFragmentView.java
[bottom_simple]: src/main/java/ru/surfstudio/android/mvp/dialog/simple/bottomsheet/CoreSimpleBottomSheetDialogFragment.kt
[nav]: src/main/java/ru/surfstudio/android/mvp/dialog/navigation/navigator/DialogNavigator.java
[dr]: src/main/java/ru/surfstudio/android/mvp/dialog/navigation/route/DialogRoute.java
[dwpr]: src/main/java/ru/surfstudio/android/mvp/dialog/navigation/route/DialogWithParamsRoute.java
[core_mvp]: ../../mvp/lib-core-mvp/

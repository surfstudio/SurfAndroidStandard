#StandardPlaceholderView

Стандартный полноэкранный плейсхолдер с поддержкой смены состояний. Ключевой особенностью виджета 
является механизм отложенного изменения состояния, позволяющий избегать промаргиваний при слишком 
частом изменении состояния плейсхолдера.  

![StandardPlaceholderView](/custom-view/doc/standard-placeholder-view.png)

##ИСПОЛЬЗОВАНИЕ

Для того, чтобы использовать `StandardPlaceholderView` на экране, нужно добавить виджет в 
разметку своего экрана:

```
<ru.surfstudio.android.custom.view.placeholder.StandardPlaceHolderView
    android:id="@+id/placeholder"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
   
            
Смена состояний `StandardPlaceholderView` осуществляется одним методом из набора:

+ `setNoneState()` - рекомендуется вызывать по окончанию асинхронного процесса. Плейсхолдер 
скрывается;

+ `setMainLoadingState()` - рекомендуется вызывать при старте асинхронного процесса в том 
случае, если на экране отсутствуют ранее загруженные данные. При установке этого состояния 
плейсхолдер блокирует UI и полностью скрывает его;

+ `setTransparentLoadingState()` - рекомендуется вызывать при старте асинхронного процесса в 
том случае, если на экране отображаются ранее загруженные данные. При установке этого 
состояния плейсхолдер блокирует UI, но остаётся полупрозрачным;

+ `setErrorState()` - рекомендуется вызывать при возникновении ошибки в процессе работы асинхронного 
процесса. При установке этого состояния плейсхолдер отображается в конфигурации для отображения 
ошибки, если оно она определена. В противном случае, плейсхолдер отображается в конфигурации по 
умолчанию;

+ `setEmptyState()` - рекомендуется вызывать при получении пустого результата по итогам работы 
асинхронного процесса. При установке этого состояния плейсхолдер отображается в конфигурации 
empty-state. В противном случае, плейсхолдер отображается в конфигурации по умолчанию;

+ `setNotFoundState()` - рекомендуется вызывать при получении пустого результата по итогам 
фильтрации данных. При установке этого состояния плейсхолдер отображается в конфигурации 
empty-state для фильтрации. В противном случае, плейсхолдер отображается в конфигурации по умолчанию.

##ИНТЕГРАЦИЯ С CORE-MVP

Если среди зависимостей вашего проекта есть модуль [`Core-MVP`](../core-mvp), в коде вашего 
app-модуля рекомендуется реализовать расширение для `StandardPlaceHolderView` для интеграции 
автоматической реконфигурации при изменении `LoadState` модели экрана с поддержкой обработки 
изменения состояний (все классы моделей экрана именованные по схеме `Lds***ScreenModel`).

Чтобы сделать это, нужно проделать четыре шага:

1) Создать новый класс, реализующий интерфейс `PlaceHolderView` и расширяющий класс 
`StandardPlaceHolderView`;

2) Реализовать в нём метод `PlaceHolderView#render(loadState: LoadState)`, который будет 
своеобразным "переходником" между `LoadState` и публичным интерфейсом `StandardPlaceHolderView`. Код
этого метода может выглядеть примерно или в точности так:

```
override fun render(loadState: LoadState) {
    when (loadState) {
        LoadState.NONE -> setNoneState()
        LoadState.MAIN_LOADING -> setMainLoadingState()
        LoadState.TRANSPARENT_LOADING -> setTransparentLoadingState()
        LoadState.EMPTY -> setEmptyState()
        LoadState.ERROR -> setErrorState()
        LoadState.NOT_FOUND -> setNotFoundState()
    }
```

3) В разметку экрана добавить плейсхолдер расширенной реализации:

```
<ru.appname.android.ProjectNamePlaceHolderView
    android:id="@+id/placeholder"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

4) В классе View с поддержкой обработки изменения состояний переопределить метод 
getPlaceHolderView() и вернуть из него экземпляр `ProjectNamePlaceHolderView`.

##НАСТРОЙКА И СТИЛИЗАЦИЯ

`StandardPlaceHolderView` поддерживает большое количество декорирующих атрибутов, а также 
предоставляет возможность оформления внешнего вида виджета при помощи стилей.
  
###СОЗДАНИЕ КАСТОМНОГО СТИЛЯ

Чтобы создать особый стиль для `StandardPlaceHolderView` в модуле ваше приложения следует создать 
новый стиль, унаследованный от базового стиля `StandardPlaceHolderView` по типу:
  
```
<style name="StandardPlaceHolderView.Default">
    <item name="progressBarColor">@color/progressbar_color</item>
    <item name="title">Заголовок</item>
    <item name="subtitle">Подзаголовок</item>
</style>
```

###ПРИМЕНЕНИЯ СТИЛЯ

Есть два способа применения кастомных стилей к виджету:
* непосредственно в разметке `layout` экрана к конкретному экземпляру;
* ко всем аналогичным виджетам приложения при помощи атрибута `standardPlaceHolderStyle` явно 
определённого в теме приложения.

###СТИЛЕВЫЕ АТРИБУТЫ

Наименование атрибута | Тип | Предназначение
------------ | ------ | -------------
opaqueBackgroundColor | color | Цвет заливки непрозрачного фона
transparentBackgroundColor | color | Цвет полупрозрачной маски
progressBarColor | color | Цвет ProgressBar (по умолчанию ?colorAccent)
title | string | Текст заголовка по умолчанию (отображается, если активированное состояние не задано)
subtitle | string | Текст подзаголовка по умолчанию (отображается, если активированное состояние не задано)
buttonText | string | Текст кнопки по умолчанию (отображается, если активированное состояние не задано)
secondButtonText | string | Текст второй кнопки по умолчанию (отображается, если активированное состояние не задано)
image | drawable | Изображение по умолчанию (отображается, если активированное состояние не задано)
emptyTitle | string | Текст заголовка для empty-state
emptySubtitle | string | Текст подзаголовка для empty-state
emptyButtonText | string | Текст кнопки для empty-state
emptySecondButtonText | string | Текст второй кнопки для empty-state
emptyImage | drawable | Изображение для empty-state
notFoundTitle | string | Текст заголовка для empty-state при фильтрации
notFoundSubtitle | string | Текст подзаголовка для empty-state при фильтрации
notFoundButtonText | string | Текст кнопки для empty-state при фильтрации
notFoundSecondButtonText | string | Текст второй кнопки для empty-state при фильтрации
notFoundImage | drawable | Изображение для empty-state при фильтрации
errorTitle | string | Текст заголовка для ошибки
errorSubtitle | string | Текст подзаголовка для ошибки
errorButtonText | string | Текст кнопки для ошибки
errorSecondButtonText | string | Текст второй кнопки для ошибки
errorImage | drawable | Изображение для ошибки
titleBottomMargin | dimen | Отступ от низ заголовка
titleTopMargin | dimen | Отступ от верха заголовка
subtitleBottomMargin | dimen | Отступ от низа подзаголовка
subtitleTopMargin | dimen | Отступ от верха подзаголовка
buttonBottomMargin | dimen | Отступ от низа кнопки
buttonTopMargin | dimen | Отступ от верха кнопки
secondButtonBottomMargin | dimen | Отступ от низа второй кнопки
secondButtonTopMargin | dimen | Отступ от верха второй кнопки
imageBottomMargin | dimen | Отступ от низа изображения
imageTopMargin | dimen | Отступ от верха изображения
titleTextAppearance | text appearance | Стиль заголовка
subtitleTextAppearance | text appearance | Стиль подзаголовка
buttonTextAppearance | text appearance | Стиль текста на кнопке
secondButtonTextAppearance | text appearance | Стиль текста на второй кнопке
buttonStyle | style | Стиль кнопки
secondButtonStyle | style | Стиль второй кнопки
imageStyle | style | Стиль изображения
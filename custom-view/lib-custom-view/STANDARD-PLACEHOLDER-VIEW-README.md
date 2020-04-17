[Ридми модуля](README.md)

[TOC]

# StandardPlaceholderView

**Данный функционал перестал поддерживаться. Следует использовать [PlaceHolderViewContainer](src/main/java/ru/surfstudio/android/custom/view/placeholder/PlaceHolderViewContainer.kt)**

Стандартный полноэкранный плейсхолдер с поддержкой смены состояний. Ключевой особенностью виджета 
является механизм отложенного изменения состояния, позволяющий избегать промаргиваний при слишком 
частом изменении состояния плейсхолдера.  

[StandardPlaceholderView](doc/standard-placeholder-view.png)

## ОТВЕТСТВЕННОЕ ЛИЦО

**Евгений Сатуров** (saturov@surfstudio.ru)

## ЧТО НОВОГО

* Добавлены атрибуты `pvTitleLineSpacingExtra` и `pvSubtitleLineSpacingExtra` для изменения высоты строки заголвоков;
* Фикс бага с задержкой отрисовки состояния на старте экрана;
* Добавлено новое состояние "No Internet" для возможности кастомной обработки ошибки интернет-соединения;
* Добавлены атрибуты `pvProgressBarWidth` и `pvProgressBarHeight` - через них можно задавать ширину и высоту прогресс-индикатора соответственно;
* Добавлены атрибуты `pvOpaqueBackground` и `pvTransparentBackground` - через них можно задавать drawable-ресурсы в качестве фона; 
* Появилась поддержка 28-и кастомных прогресс-индикаторов `pvProgressBarType`;
* Атрибут `pvProgressBarColor` теперь принимает не только ссылки на цветовые ресурсы, но и коды вида `#00FFAA`;

* Изменены имена всех стилевых атрибутов. В именование всех атрибутов добавлен префикс "pv";
* Исправлен баг, при котором не применялись стилевые атрибуты к текстовым меткам;  
* Исправлен баг приводящий к промаргиванию виджета при первом запуске экрана;
* Удалено состояние LoadState.UNSPECIFIED. Состоянием по умолчанию стало LoadState.NONE.

## ИСПОЛЬЗОВАНИЕ

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
ошибки, если она определена. В противном случае, плейсхолдер отображается в конфигурации по 
умолчанию;

+ `setEmptyState()` - рекомендуется вызывать при получении пустого результата по итогам работы 
асинхронного процесса. При установке этого состояния плейсхолдер отображается в конфигурации 
empty-state. В противном случае, плейсхолдер отображается в конфигурации по умолчанию;

+ `setNotFoundState()` - рекомендуется вызывать при получении пустого результата по итогам 
фильтрации данных. При установке этого состояния плейсхолдер отображается в конфигурации 
empty-state для фильтрации. В противном случае, плейсхолдер отображается в конфигурации по умолчанию;

+ `setNoInternetState()` - рекомендуется вызывать при возникновении ошибки интернет-соединения, если 
требуется кастомная обработка ошибки. В противном случае, плейсхолдер отображается в конфигурации по умолчанию.

## ИНТЕГРАЦИЯ С CORE-MVP

Если среди зависимостей вашего проекта есть модуль [`Core-MVP`](../../mvp/lib-core-mvp), в коде вашего
app-модуля рекомендуется реализовать расширение для `StandardPlaceHolderView` для интеграции 
автоматической реконфигурации при изменении `LoadState` модели экрана с поддержкой обработки 
изменения состояний (все классы моделей экрана именованные по схеме `Lds***ScreenModel`).

Чтобы сделать это, нужно проделать четыре шага:

1) Создать новый класс, реализующий интерфейс `LoadStateInterface` и расширяющий класс
`StandardPlaceHolderView`;

2) Реализовать в нём метод `LoadStateInterface#render(loadState: LoadStateInterface)`, который будет
своеобразным "переходником" между `LoadState` и публичным интерфейсом `StandardPlaceHolderView`. Код
этого метода может выглядеть примерно или в точности так:

```
override fun render(loadState: LoadStateInterface) {
    when (loadState) {
        LoadState.NONE -> setNoneState()
        LoadState.MAIN_LOADING -> setMainLoadingState()
        LoadState.TRANSPARENT_LOADING -> setTransparentLoadingState()
        LoadState.EMPTY -> setEmptyState()
        LoadState.ERROR -> setErrorState()
        LoadState.NOT_FOUND -> setNotFoundState()
        LoadState.NO_INTERNET -> setNoInternetState()
    }
```

3) В разметку экрана добавить плейсхолдер расширенной реализации:

```
<ru.appname.android.PlaceHolderView
    android:id="@+id/placeholder"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

4) В классе View с поддержкой обработки изменения состояний переопределить метод 
createLoadStateRenderer() и вернуть из него экземпляр `PlaceHolderView`.

## НАСТРОЙКА И СТИЛИЗАЦИЯ

`StandardPlaceHolderView` поддерживает большое количество декорирующих атрибутов, а также 
предоставляет возможность оформления внешнего вида виджета при помощи стилей.
  
### СОЗДАНИЕ КАСТОМНОГО СТИЛЯ

Чтобы создать особый стиль для `StandardPlaceHolderView` в модуле вашего приложения следует создать 
новый стиль, унаследованный от базового стиля `StandardPlaceHolderView` по типу:
  
```
<style name="StandardPlaceHolderView.Default">
    <item name="progressBarColor">@color/progressbar_color</item>
    <item name="title">Заголовок</item>
    <item name="subtitle">Подзаголовок</item>
</style>
```

### ПРИМЕНЕНИЯ СТИЛЯ

Есть два способа применения кастомных стилей к виджету:

* непосредственно в разметке `layout` экрана к конкретному экземпляру;

* ко всем аналогичным виджетам приложения при помощи атрибута `standardPlaceHolderStyle` явно 
определённого в теме приложения.

### СТИЛЕВЫЕ АТРИБУТЫ

Наименование атрибута | Тип | Предназначение
------------ | ------ | -------------
pvOpaqueBackgroundColor | color | Цвет заливки непрозрачного фона
pvOpaqueBackground | drawable | Фон заливки непрозрачного фона (приоритетнее `pvOpaqueBackgroundColor`)
pvTransparentBackgroundColor | color | Цвет полупрозрачной маски
pvTransparentBackground | drawable | Цвет полупрозрачной маски (приоритетнее `pvTransparentBackgroundColor`)
pvProgressBarColor | color | Цвет ProgressBar (по умолчанию ?colorAccent)
pvProgressBarType | enum | Тип прогресс-индикатора
pvProgressBarWidth | dimen | Ширина прогресс-индикатора
pvProgressBarHeight | dimen | Высота прогресс-индикатора
pvTitle | string | Текст заголовка по умолчанию (отображается, если активированное состояние не задано)
pvSubtitle | string | Текст подзаголовка по умолчанию (отображается, если активированное состояние не задано)
pvButtonText | string | Текст кнопки по умолчанию (отображается, если активированное состояние не задано)
pvSecondButtonText | string | Текст второй кнопки по умолчанию (отображается, если активированное состояние не задано)
pvImage | drawable | Изображение по умолчанию (отображается, если активированное состояние не задано)
pvEmptyTitle | string | Текст заголовка для empty-state
pvEmptySubtitle | string | Текст подзаголовка для empty-state
pvEmptyButtonText | string | Текст кнопки для empty-state
pvEmptySecondButtonText | string | Текст второй кнопки для empty-state
pvEmptyImage | drawable | Изображение для empty-state
pvNotFoundTitle | string | Текст заголовка для empty-state при фильтрации
pvNotFoundSubtitle | string | Текст подзаголовка для empty-state при фильтрации
pvNotFoundButtonText | string | Текст кнопки для empty-state при фильтрации
pvNotFoundSecondButtonText | string | Текст второй кнопки для empty-state при фильтрации
pvNotFoundImage | drawable | Изображение для empty-state при фильтрации
pvErrorTitle | string | Текст заголовка для ошибки
pvErrorSubtitle | string | Текст подзаголовка для ошибки
pvErrorButtonText | string | Текст кнопки для ошибки
pvErrorSecondButtonText | string | Текст второй кнопки для ошибки
pvErrorImage | drawable | Изображение для ошибки
pvNoInternetTitle | string | Текст заголовка для ошибки интернет-соединения
pvNoInternetSubtitle | string | Текст подзаголовка для ошибки интернет-соединения
pvNoInternetButtonText | string | Текст кнопки для ошибки интернет-соединения
pvNoInternetSecondButtonText | string | Текст второй кнопки для ошибки интернет-соединения
pvNoInternetImage | drawable | Изображение для ошибки интернет-соединения
pvTitleBottomMargin | dimen | Отступ от низ заголовка
pvTitleTopMargin | dimen | Отступ от верха заголовка
pvSubtitleBottomMargin | dimen | Отступ от низа подзаголовка
pvSubtitleTopMargin | dimen | Отступ от верха подзаголовка
pvButtonBottomMargin | dimen | Отступ от низа кнопки
pvButtonTopMargin | dimen | Отступ от верха кнопки
pvSecondButtonBottomMargin | dimen | Отступ от низа второй кнопки
pvSecondButtonTopMargin | dimen | Отступ от верха второй кнопки
pvImageBottomMargin | dimen | Отступ от низа изображения
pvImageTopMargin | dimen | Отступ от верха изображения
pvTitleTextAppearance | text appearance | Стиль заголовка
pvSubtitleTextAppearance | text appearance | Стиль подзаголовка
pvButtonTextAppearance | text appearance | Стиль текста на кнопке
pvSecondButtonTextAppearance | text appearance | Стиль текста на второй кнопке
pvTitleLineSpacingExtra | dimen | Высота строки заголовка
pvSubtitleLineSpacingExtra | dimen | Высота строки подзаголовка
pvButtonStyle | style | Стиль кнопки
pvSecondButtonStyle | style | Стиль второй кнопки
pvImageStyle | style | Стиль изображения

## ПОДДЕРЖИВАЕМЫЕ ПРОГРЕСС-ИНДИКАТОРЫ

![PROGRESS-INDICATORS](doc/progress-indicators.gif)

Через атрибут `pvProgressBarType` можно задавать один из указанных прогресс-индикаторов, а также 
стандартный круглый, который является индикатором по умолчанию. Для указания конкретного индикатора
следует указать его название в качестве значения атрибута `pvProgressBarType`.

Название стандартного круглого индикатора: **StandardCircleIndicator**.

Названия индикаторов по рядам на картинке выше:

Ряд 1
 
* BallPulseIndicator
* BallGridPulseIndicator
* BallClipRotateIndicator
* BallClipRotatePulseIndicator

Ряд 2
 
* SquareSpinIndicator
* BallClipRotateMultipleIndicator
* BallPulseRiseIndicator
* BallRotateIndicator

Ряд 3
 
* CubeTransitionIndicator
* BallZigZagIndicator
* BallZigZagDeflectIndicator
* BallTrianglePathIndicator

Ряд 4
 
* BallScaleIndicator
* LineScaleIndicator
* LineScalePartyIndicator
* BallScaleMultipleIndicator

Ряд 5
 
* BallPulseSyncIndicator
* BallBeatIndicator
* LineScalePulseOutIndicator
* LineScalePulseOutRapidIndicator

Ряд 6
 
* BallScaleRippleIndicator
* BallScaleRippleMultipleIndicator
* BallSpinFadeLoaderIndicator
* LineSpinFadeLoaderIndicator

Ряд 7
 
* TriangleSkewSpinIndicator
* PacmanIndicator
* BallGridBeatIndicator
* SemiCircleSpinIndicator

## РАСПРОСТРАНЁННЫЕ ПРОБЛЕМЫ

**Проблема:** К моему плейсхолдеру не применяется кастомный стиль.э

**Решение:** Скорее всего при переопределении класса StandardPlaceHolderView в приложении вы 
  переназначили значение атрибута defStyle, тем самым отменив действие стиля по умолчанию.
     
  Неверно:
     
        PlaceHolderView(context: Context, attrs: AttributeSet?, defStyle: Int = 0)
          
  Верно:
  
        PlaceHolderView(context: Context, attrs: AttributeSet?)
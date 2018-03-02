#StandardPlaceholderView

Стандартный полноэкранный плейсхолдер с поддержкой смены состояний.

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

##ИНТЕГРАЦИЯ С `CORE-MVP`

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
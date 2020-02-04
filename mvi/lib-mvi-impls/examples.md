[Главная страница репозитория](../docs/main.md)

[TOC]

# Примеры использования
Здесь описаны типичные примеры использования mvi, 
на основе которых можно будет лучше понять ответственности сущностей, и поток данных.

## Загрузка данных
**В:** Допустим, нам нужно загрузить данные по нажатию на кнопку refresh_btn. Какие сущности будут задействованы, 
и как будет выглядеть стек вызовов? 

**О:** Из View (Activity/Fragment) эмитится в EventHub событие RefreshClicked:

    refresh_btn.setOnClickListener { hub.emit(RefreshClicked) } //стандартная запись
    refresh_btn.clicks().emit(RefreshClicked) //упрощенная запись с экстеншнами
 
 После того, как оно попало в EventHub, на него могут среагировать Reactor и Middleware.
 
 Reactor в функции react может сразу дать понять view, чтобы она показала данные, и таким образом сменить ее стейт: 
 
    when (event) { 
        is RetryClicked -> stateHolder.isLoading.accept(true)
    } 
    
 
 Middleware же в этом случае отвечает за загрузку данных, и в функции transform 
 трансформирует событие RetryClicked в Observable<Data>, а затем в Observable<DataLoaded>, где 
 
 * `Data` - данные, которые приходят из сети
 * `DataLoaded` - событие успешной загрузки данных
 
 Метод трансформации в Middleware будет выглядеть так:    
    
    transform(eventStream: Observable<...>): Observable<...> { 
        return eventStream
            .ofType<RetryClicked>() //реагируем только на нужное нам событие
            .flatMap { loadData().io() } //Загружаем данные в фоновом потоке
            .map(::DataLoaded) //Маппим данные в событие
    } 
    
 Когда данные загружены, и маппинг произведен, middleware направляет событие DataLoaded в EventHub, где на него может прореагировать Reactor:
 
    when (event) { 
        is RetryClicked -> stateHolder.isLoading.accept(true)
        is DataLoaded -> { 
            stateHolder.isLoading.accept(false)
            stateHolder.data.accept(event.data)
        } 
    } 

StateHolder же, в свою очередь, служит только оповещения View о смене своего состояния.
При каждом вызове метода `State.accept(...)` View будет перерисовывать подписанные на этот State части.
Сами поля в StateHolder выглядят так:

    class StateHolder { 
        val data = State<Data>()
        val isLoading = State<Boolean>(false)
    } 

Подписка на State обычно располагается в onCreate, рядом с установкой лиснеров. 
Конфигурация View для отображения данных в нашем кейсе будет выглядеть так:

    sh.data.bindTo { newData -> setItems(newData) }
    sh.isLoading.bindTo { isLoading -> loader_view.isVisible = isLoading }  
    
    refresh_btn.setOnClickListener { hub.emit(RefreshClicked) } 
    
    
Схематично, упрощенный стек вызовов при загрузке данных от нажатия на кнопку, до отображения данных, будет выглядеть так: 

![Load data example]( https://i.imgur.com/jQNRR2z.png )

В данном кейсе мы не рассматривали случай с обработкой ошибок, RequestState и продвинутым DSL. 
На реалньных проектах используются именно они, поэтому полезно будет с ними ознакомиться: 

Обычно, асинхронную загрузку данных (например, из сети) мы мапим в событие RequestEvent и отображаем это событие с помощью RequestState. 

Синтаксис будет следующим: View остается практически без изменений, она так же эмитит событие RetryClicked в EventHub. 

А вот Middleware затрагивают серьезные изменения: Во-первых, там больше не нужно добавлять явную фильтрацию к observable, так как за это отвечает DSL-синтаксис:

    transform(eventStream: Observable<...>): Observable<...> = transformations(eventStream) { 
        addAll(
            RetryClicked::class eventMapTo { loadData().io().asRequestEvent(LoadData()) }
        )
    } 

Во-вторых, мы задействуем экстеншн asRequestEvent, который делает следующее:

1. Сразу же пушит в чейн событие LoadData со статусом Loading, т.е. уведомляет EventHub о том, что загрузка началась

1. Маппит успешную загрузку данных в событие LoadData со статусом Success, т.е. уведомляет EventHub, что данные получены.

1. Проглатывает ошибку и маппит ее в событие LoadData со статусом Error, т.е. уведомляет EventHub о том, что при загрузке возникла ошибка. 

Далее, изменения происходят и в Reactor: там вместо того, чтобы явно реагировать на событие RetryClicked, мы будем реагировать только на событие LoadData: 

    when (event) { 
        is LoadData -> stateHolder.data.modify { 
            val hasData = data?.isNotEmpty() ?: false
            copy(
                    data = mapDataList(event.type, data, hasData), //маппинг данных в ui-сущность
                    load = mapLoading(event.type, hasData, event.isSwr), //маппинг загрузки данных в ui-сущность
                    error = mapError(event.type, hasData) //маппинг ошибки
            )
        }   
 
 Синтаксис выглядит слека перегруженным, однако это необходимо понимать, 
 что именно здесь происходит маппинг сущности с сервисного слоя в слой представления (тип загрузки, свайп рефреш, отображать или нет ошибку, и так далее). 
 Эту запись можно упростить, и использовать модификацию по-умолчанию: 
        
    when(event) {     
        is LoadData -> stateHolder.data.modifyDefault(event) //делает то же самое, только быстрее. 
    }
 
 Что касается, StateHolder - он хранит только одно состояние, RequestState с данными.
 
    class StateHolder {
        val data = RequestState<Data>() 
    } 

 View, соответственно, будет подписываться на data, и отображать данные: 
 
    sh.data.observeData().bindTo { newData -> setItems(newData) } 
    sh.data.observeHasLoading().bindTo { isLoading -> loader_view.isVisible = isLoading }  
    sh.data.observeHasError().bindTo { hasError -> error_container.isVisible = hasError } 
    
    refresh_btn.setOnClickListener { hub.emit(RefreshClicked) } 
    
Схематично, загрузка с помощью RequestState выглядит так:
    
![RequestState load example]( https://i.imgur.com/59klnGr.jpg )
    
    
## Навигация

**В:** Нам нужно открыть экран по нажатию на кнопку во View.

**О:** Из View (Activity/Fragment) эмитится в EventHub событие OpenMainClicked. Это происходит следующим образом:

    refresh_btn.setOnClickListener { hub.emit(RefreshClicked) } //стандартная запись
 
 После того, как оно попало в EventHub, и на него могут среагировать Reactor и Middleware.
 
 Middleware в функции `transform` маппит событие OpenMainClicked в стандартное событие Navigation, вызывает у него метод open. 
 Дальше происходит декомпозиция события Navigation, о которой можно почитать в [документе по композиции][compreadme].
 
     transform(eventStream: Observable<...>): Observable<...> = transformations(eventStream) { 
        addAll(
            Navigation::class decomposeTo navigationMiddleware
            OpenMainClicked::class mapTo { Navigation().open(MainActivityRoute()) }
        )
    } 

Reactor и StateHolder в этом процессе не участвуют.

## Шаблонные события

**В:** Какие событие необходимо добавлять в класс событий экрана?

**O:** При создании экрана, кроме создания самого sealed-класса событий экрана, необходимо сразу добавлять в него 2 события: Lifecycle и Navigation: 

1. Lifecycle нужен для того, чтобы EventHub мог автоматически оповещать Middleware и Reactor о сменах ЖЦ View. 
 
1. Navigation нужен для реализации композиции событий навигации. Более подробно можно почитать здесь: [navigation][navreadme], [composition][compreadme]

Результирующий класс в итоге выглядит следующим образом: 

    sealed class MainEvent: Event { 
    
        data class Lifecycle(override var stage: LifecycleStage) : LifecycleEvent, MainEvent()
        data class Navigation(override var events: List<NavigationEvent> = listOf()) : NavigationComposition, MainEvent()
    }    
    
    
[compreadme]: composition.md
[navreadme]: navigation.md
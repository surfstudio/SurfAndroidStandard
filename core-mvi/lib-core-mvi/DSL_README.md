[Главная страница репозитория](../docs/main.md)

[TOC]

# MVI DSL
Для упрощения работы с MVI-модулем и уменьшения количества шаблонного кода, было решено добавить DSL-методы для трансформации потока потока событий, проходящего через middleware. 

## Начало работы

Для того, чтобы начать работать с DSL, необходимо унаследовать Middleware, использующийся в проекте, от [DslRxMiddleware][rxdslmw], и переопределить метод `transform(eventStream)` следующим образом:

    override fun transform(eventStream: Observable<ComplexListEvent>): Observable<out ComplexListEvent> {
        return transformations(eventStream) {
            //Здесь будут перечислены возможные трансформации
        }

Трансформация потока (для `Middleware<T>`) - это `Observable<out T>`, то есть, Observable с обновленным событием. 

Для того, чтобы добавить эти трансформации, нужно вызвать либо метод `add(Observable<T>)` либо унарный плюс: `+Observable<T>` 
   
## Возможные трансформации

  * `react`: Реакция на событие типа *T*. Следует использовать, когда нам не нужно трансформировать *T* в поток данных, а нужно просто выполнить какое-то действие (например, для открытия экрана или отправки аналитики).
  
    Использование: 
            
            +react<TextChangedEvent> { event -> 
                analyticsService.sendEvent(TEXT_CHANGED, event.text) 
            } 
    
  * `map`: Маппинг события типа *T* в другой тип. 
     Используется, когда нам нужно запустить событие при получении другого события, например, когда при получении события ButtonClicked нужно эмитить событие OpenSelectPhotoDialog.
     
     Использование:
     
            +map<ButtonClicked> { OpenSelectPhotoDialog() } 
  
  * `eventMap`: Маппинг события типа *T* в *Observable<R>*, где R - подтип эвента, используемого на экране. Используется, когда нам нужно трансформировать событие в поток (аналог flatMap), например, когда при получении события ButtonClicked нужно начать загрузку данных с i-слоя.
     
     Использование:
     
            +eventMap<ReloadBtnClicked> { 
                interactor.loadData()
                    .io()
                    .handleError()
                    .asRequest(LoadDataEvent())
            }
  
  * `streamMap`: Маппинг *Observable<T>* в *Observable<R>*, где R - подтип эвента, используемого на экране. Используется, когда `eventMap` недостаточно, и на цепочку нужно навесить модификаторы от Observable. Например, при получении события TextChanged добавить debounce и distinctUntilChanged перед отправкой запроса.
     Использование:
     
            +streamMap<TextChanged> { textChangedObservable -> 
                textChangedObservable
                    .map { it.text } 
                    .debounce(300L, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .map { TextChangedDebounced(it) } 
                }
               
 Middleware cо списком трансформаций может выглядеть следующим образом:
   
       override fun transform(eventStream: Observable<ComplexListEvent>): Observable<out ComplexListEvent> {
        return transformations(eventStream) {
            //Здесь будут перечислены возможные трансформации
        }
  
## Middleware Builders
Так же в модуле доступны интерфейсы для Middleware, позволяющие добавлять новую логику через их наследование:

  * [FlatMapMiddleware][flmpmw]: Middleware, которое поддерживает простейшее преобразование событий (похожее на flatMap из RxJava) - преобразовывает event типа T в Observable<out T>, и содержит единственный метод:
  
        fun flatMap(event: T): Observable<out T>
  
  * [LifecycleMiddleware][lcmw]: Middleware, реагирующий на события жизненного цикла родительского экрана. Содержит функции `onCreate()`...`onDestroy()` : `Observable<T>`, которые вызываются в момент получения коллбеков ЖЦ. Для того, чтобы класс получал эти события, необходимо, чтобы связанный с ним EventHub реализовывал интерфейс [LifecycleEventHub][lchub]. 
  
### Пример реального метода с трансформациями

В реальной разработке, метод с трансформациями потока событий будет выглядеть следующим образом:

    override fun transform(eventStream: Observable<ComplexListEvent>): Observable<out ComplexListEvent> {
        return transformations(eventStream) {
            +onCreate().eventMap { loadData() }
            +eventMap<SwipeRefresh> { loadData(isSwr = true) }
            +eventMap<LoadNextPage> { loadData(sh.list.data.nextPage) }
            +mapPagination(FilterNumbers()) { sh.list.canGetMore() }
            +map<QueryChangedDebounced> { FilterNumbers() }
            +eventMap<Reload> { loadData() }
            +eventStream.ofType<QueryChanged>().streamMap(::debounceQuery)
        }
    }

   
[rxdslmw]: src/main/java/ru/surfstudio/android/core/mvi/ui/middleware/dsl/DslRxMiddleware.kt
[lcmw]: src/main/java/ru/surfstudio/android/core/mvi/ui/middleware/builders/LifecycleMiddleware.kt
[flmpmw]: src/main/java/ru/surfstudio/android/core/mvi/ui/middleware/builders/FlatMapMiddleware.kt
[lchub]: src/main/java/ru/surfstudio/android/core/mvi/event/hub/lifecycle/LifecycleEventHub.kt
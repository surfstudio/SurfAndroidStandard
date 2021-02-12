[Главная страница репозитория](../../docs/main.md)

# MVI DSL

- [Начало работы](#начало-работы)
- [Добавление трансформаций](#добавление-трансформаций)
- [Фильтрация по классу](#фильтрация-по-классу)
- [Типы трансформаций](#типы-трансформаций)
- [Добавление новых типов трансформаций](#добавление-новых-типов-трансформаций)
- [Middleware Builders](#middleware-builders)
  - [Пример реального метода с трансформациями](#пример-реального-метода-с-трансформациями)

Для упрощения работы с MVI-модулем и уменьшения количества шаблонного кода, было решено добавить DSL-методы для трансформации потока потока событий, проходящего через middleware. 

## Начало работы

Для того, чтобы начать работать с DSL, необходимо унаследовать Middleware, использующийся в проекте, от [BaseDslRxMiddleware][rxdslmw], и переопределить метод `transform(eventStream)` следующим образом:

    override fun transform(eventStream: Observable<ComplexListEvent>): Observable<out ComplexListEvent> {
        return transformations(eventStream) {
            //Здесь будут перечислены возможные трансформации
        }

Трансформация потока (для `Middleware<T>`) - это `Observable<out T>`, то есть, Observable с обновленным событием. 

## Добавление трансформаций

Для того, чтобы добавить эти трансформации, нужно вызвать либо метод `addAll(vararg transformations: Observable<T>)` либо унарный плюс: `+Observable<T>` для добавления по очереди. 

## Фильтрация по классу

Трансформации можно применять только к событиям определенных классов со следующим синтаксисом:

`<Класс события> <тип трансформации> <трансформация>`
                
Например, с помощью записи ниже, мы обновляем данные, как только получаем событие Reload: 
                
`Reload::class eventMapTo { loadData() }`
   
## Типы трансформаций

  * `react`: Реакция на событие типа *T*. Следует использовать, когда нам не нужно трансформировать *T* в поток данных, а нужно просто выполнить какое-то действие (например, для открытия экрана или отправки аналитики).
  
    Использование: 
            
            ButtonClicked::class reactTo { event -> 
                analyticsService.sendEvent(TEXT_CHANGED, event.text) 
            } 
    
  * `map`: Маппинг события типа *T* в другой тип. 
     Используется, когда нам нужно запустить событие при получении другого события, например, когда при получении события ButtonClicked нужно эмитить событие OpenSelectPhotoDialog.
     
     Использование:
     
        ButtonClicked::class mapTo { OpenSelectPhotoDialog() } 
  
  * `eventMap`: Маппинг события типа *T* в *Observable<R>*, где R - подтип эвента, используемого на экране. Используется, когда нам нужно трансформировать событие в поток (аналог flatMap), например, когда при получении события ButtonClicked нужно начать загрузку данных с i-слоя.
     
     Использование:
     
            ReloadBtnClicked::class eventMapTo { 
                interactor.loadData()
                    .io()
                    .asRequest(LoadDataEvent())
            }
  
  * `streamMap`: Маппинг *Observable<T>* в *Observable<R>*, где R - подтип эвента, используемого на экране. Используется, когда `eventMap` недостаточно, и на цепочку нужно навесить модификаторы от Observable. Например, при получении события TextChanged добавить debounce и distinctUntilChanged перед отправкой запроса.
     Использование:
     
            TextChanged::class streamMapTo { textChangedObservable -> 
                textChangedObservable
                    .map { it.text } 
                    .debounce(300L, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .map { TextChangedDebounced(it) } 
                }
      
      Либо
               
 Middleware cо списком трансформаций может выглядеть следующим образом:
   
       override fun transform(eventStream: Observable<ComplexListEvent>): Observable<out ComplexListEvent> {
        return transformations(eventStream) {
            //Здесь будут перечислены возможные трансформации
        }
  
## Добавление новых типов трансформаций

Если вам не хватает типов трансформаций, расположенных в базовом классе [EventTransformerList][trlist], вы можете добавить новые. 

Для этого, нужно расширить базовый класс трансформации - [StreamTransformer][strtr], и описать там нужную логику, расширить класс [EventTransformerList][trlist] для добавления методов обработки новой трансформации, и расширить класс [DslRxMiddleware][rxdslmw] с указанием в параметре типа новый класса.
  
## Middleware Builders
Так же в модуле доступны интерфейсы для Middleware, позволяющие добавлять новую логику через их наследование:

  * [FlatMapMiddleware][flmpmw]: Middleware, которое поддерживает простейшее преобразование событий (похожее на flatMap из RxJava) - преобразовывает event типа T в Observable<out T>, и содержит единственный метод:
  
        fun flatMap(event: T): Observable<out T>
  
  * [LifecycleMiddleware][lcmw]: Middleware, реагирующий на события жизненного цикла родительского экрана. Содержит функции `onCreate()`...`onDestroy()` : `Observable<T>`, которые вызываются в момент получения коллбеков ЖЦ. Для того, чтобы класс получал эти события, необходимо, чтобы связанный с ним EventHub реализовывал интерфейс [LifecycleEventHub][lchub]. 
  
### Пример реального метода с трансформациями

В реальной разработке, метод с трансформациями потока событий будет выглядеть следующим образом:

    override fun transform(eventStream: Observable<ComplexListEvent>): Observable<out ComplexListEvent> {
        return transformations(eventStream) {
            addAll(
                    onCreate() eventMap { loadData() },
                    mapPagination(FilterNumbers()) { sh.list.canGetMore() },
                    eventMap<SwipeRefresh> { loadData(isSwr = true) },
                    SwipeRefresh::class eventMapTo { loadData(isSwr = true) },
                    LoadNextPage::class eventMapTo { loadData(sh.list.data.nextPage) },
                    Reload::class eventMapTo { loadData() },
                    QueryChanged::class streamMapTo (::debounceQuery),
                    QueryChangedDebounced::class mapTo { FilterNumbers() }
            )
        }

   
[baserxdslmw]: src/main/java/ru/surfstudio/android/core/mvi/impls/ui/middleware/dsl/BaseDslRxMiddleware.kt
[rxdslmw]: ../lib-mvi-core/src/main/java/ru/surfstudio/android/core/mvi/ui/middleware/dsl/DslRxMiddleware.kt
[strtr]: src/main/java/ru/surfstudio/android/core/mvi/impls/ui/middleware/dsl/transformers/rx/StreamMapTransformer.kt
[trlist]: src/main/java/ru/surfstudio/android/core/mvi/impls/ui/middleware/dsl/EventTransformerList.kt
[lcmw]: src/main/java/ru/surfstudio/android/core/mvi/impls/ui/middleware/dsl/LifecycleMiddleware.kt
[flmpmw]: ../lib-mvi-core/src/main/java/ru/surfstudio/android/core/mvi/ui/middleware/FlatMapMiddleware.kt
[lchub]: ../lib-mvi-core/src/main/java/ru/surfstudio/android/core/mvi/event/lifecycle/LifecycleEventHub.kt
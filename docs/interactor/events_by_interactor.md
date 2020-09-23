[Главная](../main.md)

### Рассылка событий через Interactor

Еще один кейс использования интеракторов: построение событийной модели общения
между частями приложения.

Например, некоторое действие на активити А должно вызывать обновление
данных на активити Б. Причем, данное действие не является результатом активити А.
Тогда можно пробросить событие через общий интерактор у данных активити.
Данный кейс, показывает, что приоритетнее использовать именно интерактор для
обновления данных, а не результат активити(например через `RouteWithParams`).
Маршрут с параметрами следует использовать там, где очевиден возврат результата, например,
некая форма, которая возвращает заполненные данные на предыдущий экран.

Реализовать проброс этого события можно через создания `Subject'а` внутри
интерактора. При этом этот Subject должен быть приватным, а наружу должен
смотреть только соответствующий `Observable`.

``` kotlin
private val someEventSubject = PublishSubject.create<SomeEvent>()
val observeSomeEvent: Observable<SomeEvent> = someEventSubject
```

*Примечание*: выбор типа сабжекта зависит от нужд приложения.

Только сам интерактор может эмитить события - презентер может лишь менять данные,
но не напрямую рассылать сообщения.

**Плохо**:

```kotlin

    //Presenter
    
    fun doSomethingWithPost() {
        postActionDisposable = subsribeIoHandleError(
            postInteractor.doSomeActionWithPost(screenModel.postId),
            {
                // do something
            },
            {
                 if (it is PostDeletedError) {
                    handleDeletedPostError()
                }
            }
        )
    }

    private fun handleDeletedPostError() {
            postInteractor.emitPostDeletedEvent(screenModel.postId)
            // some other actions
    }

```

**Хорошо**:

```kotlin
    //Presenter
    
    fun doSomethingWithPost() {
        postActionDisposable = subsribeIoHandleError(
            postInteractor.doSomeActionWithPost(screenModel.postId),
            {
                // do something
            },
            {
                //handle something error
            }
        )
    }
    
    //Interactor

    fun doSomeActionWithPost(postId: Long) =
        postRepository.doSomeWithPost()
            .doOnError {
                if (it is PostDeletedError) {
                    emitPostDeletedEvent(postId)
                }
            }

```

Может возникнуть кейс, когда экран, который вызывает изменение данных, сам
подписан на изменение. Тогда следует добавить в чейн с подпиской оператор filter.

``` kotlin

    val postActionDisposable = ...

    //...

    postActionDisposable = subsribeIoHandleError(postInteractor.doSomeActionWithPost(screenModel.postId),
        {
            // do something
        })

    //...

    postChangedEventDisposable = subscribe(interactor.observePostChangedEvent
            .filter { postActionDisposable.isDisposed },
        {
            // OnSuccess
        })
```

Таким образом можно офильтровать событие по активности запроса, который вызывает
изменение данных, чтобы оно не хендлилось на текущем экране.
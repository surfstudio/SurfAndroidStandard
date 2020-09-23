[Главная](../../../docs/main.md)

[Ридми модуля](../README.md)

# Presenter

- [Обработка ошибок](#обработка-ошибок)

Содержат всю логику соответствующего [экрана][view].
Все презентеры должны быть унаследованы от класса [**BasePresenter**][base].

**Презентеры не уничтожаются при изменении конфигурации**.
Этот механизм реализован с помощью сохранения всего [Dagger-компонента][configurator]
экрана и переиспользования его для воссозданного View.
Презентер уничтожается при полном уничтожении экрана (например после `Activity#finish()`).

Для оповещения вью об изменения состояния используется одна [модель][model]
(`extends ScreenModel`), которая передается в метод вью `#render()`.

Когда View становится готовой к работе, у презентера вызываются методы:

- `#onFirstLoad()` - первый запуск экрана - первичная загрузка данных;

- `#onLoad()` - запуск экрана - действия при каждой загрузке. В данный метод
приходит флаг `viewRecreated`, который говорит о том, открыт ли экран заново либо
пересоздан из-за смены конфигурации;

Презентер имеет методы для отслеживания жизненого цикла View. Также здесь
можно зарегистрировать делегатов в `screenEventDelegateManager` (см. [Делегаты][delegates]).

**Presenter может взаимодействовать с [асинхронными задачами только через Rx][async]**.
Для подписки на асинхронную задачу следует вызвать один из методов презентера `#subscribe()`.
Подписка, созданная таким образом, обладает следующими свойствами:

- Происходит заморозка Rx событий (`onNext`, `onError`, `onComplete`)
перед уничтожением View и разморозка после ее воссоздания.
Это свойство необходимо для предотвращении обработки событий,
когда Presenter не имеет ссылки на View.

- Происходит заморозка Rx событий на `#onPause` и разморозка на `#onResume`.
Это свойство позволяет предотвращать обработку событий невидимыми экранами
(например, когда экран находится в стеке).

- Происходит отписка всех Rx подписок при полном уничтожении экрана.

Передача в Presenter аргумента, переданного в Intent или Bundle (которые
обернуты в [`Route`][nav]),
при старте экрана должна производиться через Dagger.

Для сетевых запросов в презентере предусмотрены методы `BasePresenter#subscribeIoHandleError()`,
содержащие стандартный обработчик ошибок сетевых запросов.
При этом обработчик ошибки можно заменить, переписав метод `BasePresenter#handleError()`.

Все варианты методов `#subscribe()` перед подпиской переводят Observable
в mainThread (`.observeOn(...)`).
Кроме того, все варианты методов `#subscribeIoHandleError()`
производят подписку на Observable в рабочем потоке (`.subscribeOn(...)`).

Также к слою Presenter относятся классы с логикой UI части приложения,
такие как Navigator( см. [Навигация][nav]).
Эти классы также поставляются Dagger и не должны иметь прямую ссылку на Activity,
View и другие части, которые могут стать невалидными при смене конфигурации.
Однако для доступа к функционалу этих частей можно использовать классы FragmentProvider и
ActivityProvider, которые поставляют валидные Fragment и Activity при каждом вызове `#get()`.

Api презентера может иметь только методы вида **`void someMethod(params...)`**,
это необходимо для соответствия принципу **unidirectional data flow**

![](https://preview.ibb.co/jpCbUp/unidirect_dataflow.png)

### Обработка ошибок

Ошибки с запросов к репозиторию обрабатываются в презентере.

Для этого предусмотрен механизм, с возможностью поставить [стандартный обработчик
ошибок][handler] на все запросы. Он предоставляется модулем [core-mvp][mvp].

Для этого необходимо реализовать интерфейс [`ErrorHandler`][handler] и предоставить
ее как зависимость, например, через даггер:
```
@Module
class ErrorHandlerModule {

    @Provides
    @PerScreen
    fun provideNetworkErrorHandler(standardErrorHandler: StandardErrorHandler): ErrorHandler {
        return standardErrorHandler
    }
}
```

Конкретную реализацию можно посмотреть [здесь][ex1] и [здесь][ex2].

Данный обработчик будет автоматически перехватывать ошибки при использовании
метода `BasePresenter#subscribeIoHandleError`.

Частные случаи и установку состояния экрана при ошибке следует производить
в коллбеках метода `BasePresenter#subscribeIo` и `BasePresenter#subscribeIoHandleError`.

Например так:

```
stopCampaignDisposable = subscribeIoHandleError(postInteractor.stopPromoCampaign(screenModel.postId),
                {
                    view.showPromotionStopped()
                },
                {
                    screenModel.loadState = LoadState.ERROR
                    view.render(screenModel)
                })
```

#### Автоматическа перезагрузка данных при появлении соединения с сетью

`BasePresenter` предоставляет механизм для автоматической перезагрузки данных,
в случае, когда пропало соединение с сетью, а потом появилось.

Этот механизм представлен методами `BasePresenter#subscribe...AutoReload()`.

#### Лучшие практики

Для объединения запросов к репозиториям необходимо использовать
`ObservableUtil#combineLatestDelayError()` вместо `Observable#zip()`.
Это продиктовано особенностью получения данных.

Каждый Observable может эмитить 2 элемента: один из **кеша**, другой с **сервера**.
Observables в этом случае выполняются *параллельно*.

[base]: ../src/main/java/ru/surfstudio/android/core/mvp/presenter/BasePresenter.java
[view]: view.md
[model]: screen_model.md
[delegates]: ../../../core-ui/README.md
[nav]: ../../../docs/ui/navigation.md
[async]: ../../../docs/common/async.md
[mvp]: ../README.md
[handler]: ../src/main/java/ru/surfstudio/android/core/mvp/error/ErrorHandler.java
[ex1]: ../../../template/i-network/src/main/java/ru/surfstudio/standard/i_network/error/NetworkErrorHandler.kt
[ex2]: ../../../template/base_feature/src/main/java/ru/surfstudio/standard/ui/error/StandardErrorHandler.kt
[configurator]: configurator.md

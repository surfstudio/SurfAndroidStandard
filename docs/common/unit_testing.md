[Главная](../main.md)

# Unit-тестирование
1. [Основы](#Основы)
2. [Инструменты](#Инструменты)
3. [Тестирование сущностей различных слоёв архитектуры Surf](#Тестирование-сущностей-различных-слоёв-архитектуры-surf)
   1. [MVI](#mvi)
      1. [Middleware](#middleware)
      2. [Reducer](#reducer)
      3. [CommandHolder](#commandholder)
   2. Binding
   3. MVP
4. [Мокирование доменных моделей](#Мокирование-доменных-моделей)
5. [Best practices](#best-practices)
6. [Материалы](#Материалы)

---

### Основы

**Unit-тестирование (или модульное тестирование)** - процесс проверки корректности работы отдельных модулей программы.
Под модулем может подразумеваться класс, метод или совокупность классов.

Плюсы написания тестов:

- выявление багов и определение угловых кейсов на раннем этапе разработки;
- ускорение разработки;
- предотвращение регресса кодовой базы;
- создание дополнительной документации модуля, описывающее его поведение в различных ситуациях.

---

В android-проекте unit-тесты располагаются в каталоге `<название-модуля>/src/test/java/`.
Тесты конкретного модуля (класса, extension-методов, утилит, etc) располагаются в классе с соответствующим названием - `<название-фичи>Test`.
Члены класса упорядочены следующим образом:
- приватные поля с моками, стабами и прочими сущностями, использующимися в тестах;
- методы, выполняющиеся *единожды перед всеми* тестами;
- методы, выполняющиеся *перед каждым* тестом;
- методы, выполняющиеся *единожды после всех* тестов;
- методы, выполняющиеся *после каждого* теста;
- тесты.

Порядок расположения тестов внутри тест-класса произвольный.

Название тестов должно содержать описание конфигурации среды выполнения и ожидаемый результат.
Другими словами, название теста должно чётко описывать его содержимое.

```kotlin
@Test
fun `when a and b equals 2, sum should be equals 4`() { ... }
```

Исключение - тесты с параметрами.
```kotlin
@Test
fun `with given email and phone validation should pass`() = forAll(...) { ... }
```

Тело unit-теста в большинстве случаев состоит из следующих блоков:
- **given** - конфигурация тестовой среды;
- **when** - выполнение действия, результат которого нужно проверить;
- **then** - проверка результатов.

```kotlin
@Test
fun `when a and b equals 2, sum should be equals 4`() {
    // given 
    val a = 2
    val b = 2
    
    //when
    val result = a + b
    
    //then 
    result shouldBe 4
}
```
---

Unit-тестами должно быть покрыто публичное API модуля за исключением тривиальных геттеров-сеттеров.
Для того, чтобы определить, какие кейсы должны быть проверены тестами, можно обратиться к тест-кейсам,
написанным тестировщиком. Их можно найти в JIRA проекта во вкладке `Xray Test Repository`.

#### Инструменты

- [Kotest](https://kotest.io/docs/framework/framework.html "Kotest") -
написание unit-тестов на Kotlin;
- [Mockk](https://mockk.io/ "Mockk") -
создание моковых объектов для использования в unit-тестах
- [Kotest plugin](https://plugins.jetbrains.com/plugin/14080-kotest) -
плагин для Android Studio, позволяющий запускать тесты из IDE

Для корректной работы библиотек версия Gradle-wrapper не должна быть ниже 6.0.

#### Тестирование сущностей различных слоёв архитектуры Surf

*Основной принцип, которого придерживаемся при реализации unit-тестов для любых сущностей -
все внешние/внутренние объекты, с которыми сущность взаимодействует, должны быть заменены стабами/моками.*

*Тесты должны быть изолированы друг от друга.* После изменения состояния полей тест-класс внутри теста необходимо сбросить состояние этих полей.

*Нужно стараться избегать копипасты самих тестов и их блоков инициализации.*  
Если необходимо проверить, что на одном наборе данных метод вернет `true`, а на другом - `false`,
стоит воспользоваться параметризованными тестами.  
Если во многих тестах в блоке инициализации содержится один и тот же код конфигурирования моков и стабов,
то стоит его вынести в методы, выполняющиеся до и после тестов.

**Stub** - сущность реализует интерфейс реального объекта.
Методы возвращают либо простой результат-заглушку, либо ничего.
Как правило ничего не знает о компонентах, которые его используют.  
**Mock** - разработчик программирует результат выполнения метода с определенными параметрами - как до выполнения теста, так и во время выполнения в блоке **given**.

---

Для написания тестов для модуля в build.gradle необходимо добавить следующую строку:
```
apply from: "../unitTestConfiguration.gradle"
```

Kotest предоставляет возможность написание тест-классов в различных стилях,
в т.ч. функциональном. В качестве стиля по умолчанию предлагается использовать
обычный JUnit-стиль с аннотациями. При желании можно выбрать любой из представленных.

##### MVI

###### Middleware

Тестирование Middleware сводится к трём шагам:
1. Конфигурация входных событий и подписка на `Middleware.transform()`
2. Выполнение метода `Middleware.transform(event.toObservable())`
3. Проверка получившихся в результате трансформаций событий.

Для реализации тест-класса Middleware существует базовый класс [`BaseMiddlewareTest`][baseMiddlewareTest],
который содержит всё необходимое для создания экземпляра тестируемого Middleware.

Пример тест-класса для Middleware:
```kotlin
internal class RateChooserMiddlewareTest : BaseMiddlewareTest() {

    private val initialState = RateChooserState()
    private val sh = RateChooserStateHolder()

    private val analyticsService = mockk<DefaultAnalyticService>()
    private val route = mockk<RateChooserDialogRoute>()

    @Test
    fun `when rate button clicked and rate is good, should be mapped review composer screen open event`() {
        setRate(5)

        val middleware = createMiddleware()
        val inputEvent = RateChooserEvent.Input.RateBtnClicked
        val testObserver = TestObserver<RateChooserEvent>()

        middleware.transform(inputEvent.toObservable()).subscribe(testObserver)

        assertSoftly(testObserver.values().first()) {
            shouldBeTypeOf<RateChooserEvent.Navigation>()

            val events = actualEvent.events

            events.firstOrNull()
                .shouldBeNavigationCommand<Show>()
                .withRoute<StoreRedirectDialogRoute>()

            events.getOrNull(1)
                .shouldBeNavigationCommand<Dismiss>()
                .withRoute(route)
        }

        verify { analyticsService.performAction(any<RatingStarCountEvent>()) }
    }
    
    // остальные тесты
    
    private fun createMiddleware(): RateChooserMiddleware {
        return RateChooserMiddleware(
            baseMiddlewareDependency,
            analyticsService,
            navigationMiddleware,
            route,
            sh
        )
    }
}
```

1. Проверка команд навигации

Для проверки того, что в результате трансформаций были произведены события команд навигаций,
существует набор мэтчеров [`NavigationMatchers.kt`][navigationMatchers].
С их помощью можно проверить тип возвращаемых команд и их `route`, минуя длинные цепочки из `shouldBeTypeOf()`.
```kotlin
@Test
fun `when rate button clicked, review composer screen open event should be produced`() {
    val inputEvent = Input.RateBtnClicked
    
    val testObserver = middleware.transform(inputEvent.toObservable()).test()

    val actualEvent = testObserver.values().first()
    assertSoftly(actualEvent) {
        shouldBeTypeOf<RateChooserEvent.Navigation>()

        val events = actualEvent.events
        events.firstOrNull()
            .shouldBeNavigationCommand<Open>()
            .withRoute<ReviewActivityRoute>()

        events.getOrNull(1)
            .shouldBeNavigationCommand<Dismiss>()
            .withRoute(route)
    }
    
    testObserver.dispose()
}
```

2. Проверка запросов

Предположим, что нужно протестировать middleware, запрашивающий данные из CityInteractor:
```kotlin
// CityInteractor.kt
class CityInteractor {
  
    fun getCities(): Single<List<City>> { ... }
}

// MiddlewareToTest.kt

    override fun transform(eventStream: Observable<SomeEvent>) =
        transformations(eventStream) {
            addAll(
                onCreate() eventMap { getCities() }
            )
        }

    private fun getCities(): Observable<out SomeEvent> {
        return cityInteractor.getCities()
            .io()
            .asRequestEvent(DataLoad::GetCities)
    }
```

Для начала в тест-класс нужно добавить поле мока `CityInteractor`:
```kotlin
private val cityInteractor = mockk<CityInteractor> {
    every { cityInteractor.getCities() } returns Single.just(emptyList<City>())
}
```
Сконфигурировать мок можно либо на месте, либо в методе, выполняющемся перед тестами:
```kotlin
@BeforeAll
fun setUpAll() {
    every { cityInteractor.getCities() } returns Single.just(emptyList<City>())
}
```

Для проверки того, что в результате трансформаций были произведены события `RequestEvent`,
существует набор мэтчеров [`RequestMatchers.kt`][requestMatchers].
```kotlin
@Test
fun `when screen initialized, cities should be loaded`() {
    val inputEvent = Lifecycle(LifecycleStage.CREATED)

    val testObserver = middleware.transform(inputEvent.toObservable()).test()

    assertSoftly(testObserver.values()) {
        firstOrNull().shouldBeRequestLoading<List<Cities>>()
            
        getOrNull(1)
            .shouldBeRequestSuccess<List<Cities>>()
            .withValue<List<Cities>>()
    }
    
    testObserver.dispose()
}
```

3. Тестирование асинхронных трансформаций

Для проверки трансформаций, применяемых с задержкой, используются следующие инструменты:
- RxJavaPlugins - позволяет переопределить используемые `Scheduler`'ы;
- TestScheduler - позволяет проматывать время для `Observable`.
```kotlin
@Test
fun `when rate button clicked, RateBtnClickedDebounced should be produced after delay`() {
    val testScheduler = TestScheduler()
    RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    val inputEvent = Input.RateBtnClicked
    val scheduledMiddleware = MiddlewareToTest(baseMiddlewareDependency)
    val testObserver = scheduledMiddleware.transform(inputEvent.toObservable()).test()
    
    testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

    testObserver.values()
        .first()
        .shouldBeTypeOf<Input.RateBtnClickedDebounced>()
   
    testObserver.dispose()
}
```

###### Reducer

Обычный тест `Reducer`'а состоит из следующих шагов:
1. Конфигурация стейта `initialState` и нового события `event`
2. Выполнение метода `Reducer.reduce(initialState, event)`
3. Проверка получившегося в результате работы метода стейта.

Для реализации тест-класса `Reducer`'а существует базовый класс [`BaseReactorTest`][bestReactorTest],
который содержит всё необходимое для создания экземпляра тестируемого `Reducer`а.

Пример тест-класса:
```kotlin
internal class ServiceSearchReducerTest : BaseReactorTest() {

    @Test
    fun `when query changed, filtered list should be produced`() {
      val reducer = ServiceSearchReducer(baseReactorDependency)
        val initialState = ServiceSearchState(query = EMPTY_STRING, items = listOf("a", "b", "c"))
        val event = Input.QueryChanged(newQuery = "abc")
    
        val newState = reducer.reduce(initialState, event)
    
        newState.filteredItems.shouldBeEmpty()
    }
}
```

###### CommandHolder

При написании теста для `Reducer`'а может понадобиться проверить факт отправки значения в команду/стейт `CommandHolder`'а.
Сделать это можно двумя способами.
1. Явная подписка на команду/стейт `CommandHolder`'а
```kotlin
@Test
fun `when pin is wrong, should show error under pins`() = forAll(
    row((minErrorCount - 1).coerceAtLeast(0), minErrorCount + 1),
    row(minErrorCount, minErrorCount + 1),
    row(minErrorCount + 1, minErrorCount + 1)
) { attemptNumber, maxAttempts ->
    val initialState = PinCodeAuthState(wrongAttemptsCount = attemptNumber - 1)
    val remainingAttempts = maxAttempts - attemptNumber
    val event = PinCodeAuthEvent.WrongPinEntered(remainingAttempts)

    val testObserver = commandHolder.showError.observable.test()
    reducer.reduce(initialState, event)

    testObserver.values().firstOrNull() shouldBe event.remainingAttempts
}
```
2. Использование мока и captured value

**Тест-класс должен реализовывать интерфейс `StateEmitter`.**
```kotlin
val commandHolder = mockk<SomeCommandHolder>
val reducer = SomeReducer(errorHandler, commandHolder)

@Test
fun `when pin is wrong, should show error under pins`() = forAll(
    row((minErrorCount - 1).coerceAtLeast(0), minErrorCount + 1),
    row(minErrorCount, minErrorCount + 1),
    row(minErrorCount + 1, minErrorCount + 1)
) { attemptNumber, maxAttempts ->
    val errorMessageSlot = slot<String>()
    every { commandHolder.showError.accept(capture(errorMessageSlot)) }

    val initialState = SomeState()
    val event = RequestValidation

    reducer.reduce(initialState, event)
    
    errorMessageSlot.captured shouldBeEqualIgnoringCase "Ошибка!"
}
```

#### Мокирование доменных моделей

Для мокирования домменых моделей предусмотрен метод `ru.surfstudio.standard.small_test_utils.mock.MockModelUtilKt.parseMockModel`. 
На вход принимает json и класс маппинг-модели, возвращает доменную модель.
 
Сценарий использования:
- В файл рядом с тестами кладутся kt-строки с json-ом необходимого ответа от сервера
- В том же файле или рядом кладутся методы `getMockDomainModel`

Пример **DomainModelMock.kt**
```kotlin
fun getMockDomainModel(): DomainModel = parseMockModel(json, DomainModelObj::class)

private val json = """
    <json representation of the model>
""".trimIndent()
```

#### Best practices

- В конце теста необходимо выполнять `testObserver.dispose()`
- Для обеспечения изоляции тестов после использования `RxJavaPlugins` необходимо вызывать `RxJavaPlugins.reset()`
- Для сущностей Android SDK (`Html`, `Uri`) необходимы моки
- Для мокирования статических методов используется `mockkStatic`:
```kotlin
mockkStatic(HtmlCompat::class)
```
- Когда необходимо гарантировать выполнение определенных инструкций внутри тестируемого метода,
можно использовать `verify`:
```kotlin
    // then block
    
    // Будет выполнена проверка того, что методы вызваны с соответствующими значениями 
    // и в указанном порядке.
    verify {
        rateStorage.canShow = true
        rateStorage.boundaryScore = 1
    }
```
#### Материалы

- [Быстрый старт: гайд по автоматизированному тестированию для Android-разработчика. JVM](https://habr.com/ru/company/redmadrobot/blog/532306/ "Быстрый старт: гайд по автоматизированному тестированию для Android-разработчика. JVM")
- [Практика написания тестов. Лекция Яндекса](https://habr.com/ru/company/yandex/blog/346186/ "Практика написания тестов. Лекция Яндекса") ([видео](https://www.youtube.com/watch?v=MS7GN2Lgdas))
- [Типичные ошибки при написании юнит-тестов. Лекция Яндекса](https://habr.com/ru/company/yandex/blog/436850/ "Типичные ошибки при написании юнит-тестов. Лекция Яндекса") ([видео](https://www.youtube.com/watch?v=ZyGZjpxF9Fo))
- [RxJava 2 in unit tests](https://medium.com/@PaulinaSadowska/writing-unit-tests-on-asynchronous-events-with-rxjava-and-rxkotlin-1616a27f69aa "RxJava2 in unit tests")
- [RxJava 2 unit testing tips](https://proandroiddev.com/rxjava-2-unit-testing-tips-207887d3f15c "RxJava 2 unit testing tips")
- [Android Developers: Build local unit tests](https://developer.android.com/training/testing/unit-testing/local-unit-tests "Android Developers: Build local unit tests")
- [Android Developers: Testing from the command line](https://developer.android.com/studio/test/command-line "Android Developers: Testing from the command line")
- [Software Unit Test Smells (ENG)](https://testsmells.github.io/)
- [Test Smells (RUS)](https://github.com/kzaikin/test-smells)

[baseMiddlewareTest]: ../../template/base_feature/src/main/java/ru/surfstudio/standard/ui/test/base/BaseMiddlewareTest.kt
[bestReactorTest]: ../../template/base_feature/src/main/java/ru/surfstudio/standard/ui/test/base/BaseReactorTest.kt
[navigationMatchers]: ../../template/base_feature/src/main/java/ru/surfstudio/standard/ui/test/matcher/NavigationMatchers.kt
[requestMatchers]: ../../template/base/src/main/java/ru/surfstudio/standard/base/test/matcher/RequestMatchers.kt

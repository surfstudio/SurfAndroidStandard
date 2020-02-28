# Small-test-utils module

В модуле содержится общая конфигурация и базовые и утилитные классы для НЕ инструментальных тестов. В такие тесты в том числе входят чистые unit тесты, интеграционыые тесты на API.

### Настройка

Чтобы добавить зависимости в тестируемый модуль, нужно в build.gradle
файле модуля, прописать:
```
apply from: '../unitTestConfiguration.gradle'
```

Тесты должны находиться в `src/test/java`.

### Api-tests
Эти тесты служат для проверки работоспособности API сервера и правильности реализации интеграционного слоя с API в приложении.

Для создания api тестов
следует определить dagger-component c зависимостью на
`TestNetworkAppComponent` из small-test-utils модуля. Например:
```
@PerTest
@Component(dependencies = [TestNetworkAppComponent::class])
interface TestAuthApiComponent {
    fun inject(test: SampleApiTest)
}
```
И dagger-module с определением нужных зависимостей.

Классы api-тестов должны быть помечены аннотацией
```
@RunWith(ApiTestRunner::class)
```
и быть наследниками `BaseNetworkDaggerTest`. При наследовании нужно создать
dagger-component определяя поле `component`. Например:
```
override val component: TestAuthApiComponent = DaggerTestAuthApiComponent.builder()
            .testNetworkAppComponent(networkAppComponent)
            .build()
```

Также нужно реализовать метод `inject`. Например:
```
override fun inject() {
        component.inject(this)
}
```
###### WaitApi

Также можно определить api-тесты для нереализованного api. Они помогают
следить за тем, какие методы были имплементированны, а какие нет. Для этого
нужно пометить метод теста аннотацией
```
@WaitApiTest()
```
Также в парамметрах аннотации можно указать класс ожидаемой ошибки, если она определенна.

Такие тесты считаются пройденными если выбрасывается исключение.

### Запуск

Чтобы запустить тесты нужно выполнить команду
```
./gradlew testQaUnitTest -PtestType=api
```

Тесты можно запускать для разных buildVariant.

Параметр testType может принимать значения `unit, api, waitApi`. Или сразу
несколько `-PtestType=api,waitApi`. Если параметр не указать, запустятся все
тесты.
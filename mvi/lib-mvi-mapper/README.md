[Главная страница репозитория](/docs/main.md)

# MVI Mapper

- [Сущности](#сущности)
- [Описание](#описание)
- [Использование](#использование)

Модуль содержит один класс (и прилагающиеся к нему *typealiases*), предоставляющий мощный инструментарий для обработки запросов в MVI подходе.

## Сущности
* `RequestMapper` - класс, в который мы оборачиваем запрос и производим обработку;
* `RequestTypeMapper` - *typealias* лямбды, трансформирующей тип запроса `Request<T> -> Request<D>`;
* `RequestDataMapper` - *typealias* лямбды, которая мапит полученные от запроса данные и ui-данные.
* `RequestLoadingMapper` - *typealias* лямбды, которая мапит состояние загрузки запроса.
* `RequestErrorHandler` - *typealias* лямбды, производящей обработку ошибки выполнения запроса.
* `RequestReactor` - *typealias* лямбды, которая выполняется при изменении состояния запроса.
* `RequestDataReactor` - *typealias* лямбды, которая выполняется при успешном выполнении запроса.
* `RequestErrorReactor` - *typealias* лямбды, которая выполняется при неуспешном выполнении запроса.
* `RequestLoadingReactor` - *typealias* лямбды, которая выполняется при загрузке запроса.

## Описание
`RequestMapper` предназначен для удобного и максимально контролируемого преобразования `Request` любого типа в `RequestUi` любого типа,
а также для достижения наиболее прозрачной и понятной обработки запроса посредством использования паттерна `Builder`.

Позволяет переиспользовать логику уже созданных мапперов данных/ошибки/состояния загрузки, а также создавать свои специфичные мапперы.

Сущность следует создавать и использовать через билдеры компаньона.

## Использование
##### Самый длинный и гибкий путь:
```kotlin
// Писать мапперы самостоятельно
val request: Request<Kitten>
val requestUi: RequestUi<KittenUi> 
val newRequestUi = RequestMapper.builder(request, requestUi)
    // Приводим Request<Kitten> к Request<KittenUi>
    .mapRequest { kitten -> KittenUi(kitten) }
    // Либо берем данные из запроса, если он завершился удачно,
    // либо берем прошлые данных из requestUi
    .mapData { request, data -> request.getDataOrNull() ?: data }
    // Мапим состояние загрузки запроса: здесь мы должны вернуть инстанс объекта Loading,
    // который можно использовать для отображения состояния этого запроса на UI пользователя
    // В этом случае мы просто оборачиваем request.isLoading в SimpleLoading, 
    // т.к. для нашего кейса этого достаточно.
    .mapLoading { request, data -> SimpleLoading(request.isLoading) }
    // Если запрос завершился с ошибкой - обрабатываем ее.
    // Обработка идет по принципу Chain of Responsibility, т.е. если мы здесь
    // не обработали ошибку, то обязаны вернуть false и передать обработку
    // следующему обработчику по цепочке (если он, конечно же, есть)
    .handleError { error, data, loading ->
        when {
            error is NoSuchElementException -> {
                ch.updateFailed.accept()
                true // Обработка ошибки на этом и закончится
            }
            else -> false // Обработка ошибки будет передана следующему обработчику
        }
    }
    // Этот код выполняет тот же функционал, что и handleError выше, но в более
    // лаконичном стиле.
    .handleSpecificError<NoSuchElementException> { error, data, loading ->
        ch.updateFailed.accept()
        true
    }
    // Выполнится только если вышележащие handleError'ы вернули false
    .handleError { error, data, loading ->
        error?.let(errorHandler::handleError)
        true
    }
    // Также мы можем реагировать на события жизненного цикла запроса
    // и совершать определенный действия, связанные с этими событиями
    .reactOnSuccess { request, data, loading ->
        ch.updateSucceed.accept()
    }
    // Собираем цепочку, выполняем и получаем на выходе RequestUi.
    .build()
```

##### Наиболее оптимальный и лаконичный путь:
```kotlin
// Использовать заранее созданные мапперы для самых распространенных кейсов маппинга запроса и, 
// при необходимости, писать кастомные, для специфичных случаев.
// Реализации мапперов создаются для каждого проекта вручную.
// Реализацию мапперов семпла можно посмотреть в RequestMappers.kt.
val request: Request<Int>
val requestUi: RequestUi<Int>
val newRequestUi = RequestMapper.builder(request, requestUi)
    // Мапим данные по-умолчанию (пытаемся получить новые данные, иначе оставляем старые)
    .mapData(RequestMappers.data.default())
    // Мапим состояния загрузки (оборачиваем isLoading запроса в SimpleLoading)
    .mapLoading(RequestMappers.loading.simple())
    // Без обработки ошибок - отправляем на UI комманды о том, успешно ли
    .reactOnSuccess { _ -> ch.updateSucceed.accept() }
    // или неудачно ли завершился наш запрос
    .reactOnError { _ -> ch.updateFailed.accept() }
    .build()
```

##### Кейс с трансформацией:
```kotlin
val newRequestUi = RequestMapper.builder(event.request, state.loadPopularKittiesRequestUi)
    // Преобразования списка типа T в список типа R
    .mapRequest { kittiesList -> kittiesList.map { KittenUi(it) } }
    .mapData(RequestMappers.data.default())
    .mapLoading(RequestMappers.loading.simple())
    .reactOnSuccess { _ -> ch.updateSucceed.accept() }
    .reactOnError { _ -> ch.updateFailed.accept() }
    .build()
```

##### Кейс с пагинацией и трансформацией:
```kotlin
val newRequestUi = RequestMapper.builder(event.request, state.kittiesRequestUi)
    // Преобразуем Request<DataList<Kitten>> в Request<DataList<KittenUi>>
    .mapRequest { requestData -> requestData.transform { KittenUi(it) } }
    // Мапим данные пагинации специализированным маппером
    .mapData(RequestMappers.data.pagination())
    // Мапим состояние загрузки
    .mapLoading(RequestMappers.loading.pagination(event.isSwr))
    // Не обрабатываем ошибки намеренно, т.к это демо.
    .build()
```
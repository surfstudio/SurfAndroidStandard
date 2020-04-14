package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.KittenUi
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.util.RequestMappers
import ru.surfstudio.android.core.mvi.ui.mapper.RequestMapper
import ru.surfstudio.android.core.mvi.ui.reducer.Reducer
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SimpleLoading
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

internal data class KittiesState(
        val loadTopKittenRequestUi: RequestUi<KittenUi> = RequestUi(),
        val loadNewKittiesCountRequestUi: RequestUi<Int> = RequestUi(),
        val loadPopularKittiesRequestUi: RequestUi<List<KittenUi>> = RequestUi(),
        val updateMeowCountRequestUi: RequestUi<Int> = RequestUi(),
        val sendMeowRequestUi: RequestUi<Unit> = RequestUi()
) {
    // Шорткаты к данным запросов:
    val topKitten: KittenUi
        get() = loadTopKittenRequestUi.data ?: KittenUi()

    val newKittiesCount: Int
        get() = loadNewKittiesCountRequestUi.data ?: 0

    val popularKitties: List<KittenUi>
        get() = loadPopularKittiesRequestUi.data ?: emptyList()

    val meowCount: Int
        get() = updateMeowCountRequestUi.data ?: 0

    val isTopKittenLoading: Boolean
        get() = loadTopKittenRequestUi.isLoading

    val isNewKittiesCountLoading: Boolean
        get() = loadNewKittiesCountRequestUi.isLoading

    val isPopularKittiesLoading: Boolean
        get() = loadPopularKittiesRequestUi.isLoading

    val isMeowButtonLoading: Boolean
        get() = listOf(sendMeowRequestUi, updateMeowCountRequestUi).any { it.isLoading }
}

@PerScreen
internal class KittiesStateHolder @Inject constructor() : State<KittiesState>(KittiesState())

@PerScreen
internal class KittiesCommandsHolder @Inject constructor() {
    val kittenClicked = Command<Unit>()
    val updateSucceed = Command<Unit>()
    val updateFailed = Command<Unit>()
    val meowSent = Command<Unit>()
}

@PerScreen
internal class KittiesReducer @Inject constructor(
        private val errorHandler: ErrorHandler,
        private val ch: KittiesCommandsHolder
) : Reducer<KittiesEvent, KittiesState> {

    override fun reduce(state: KittiesState, event: KittiesEvent): KittiesState {
        return when (event) {
            is TopKittenRequestEvent -> onTopKittenRequest(state, event)
            is NewKittiesCountRequestEvent -> onNewKittiesCountRequest(state, event)
            is PopularKittiesRequestEvent -> onPopularKittiesRequest(state, event)
            is SendMeowRequestEvent -> onMeowSendRequest(state, event)
            is UpdateMeowCountRequestEvent -> onMeowUpdateCountRequest(state, event)
            is Input.KittenClicked -> state.also { ch.kittenClicked.accept() }
            else -> state
        }
    }

    // 1# Демо-метод, который инкапсулирует в себе кастомный (стандартный для экрана) маппинг запроса.
    // Рекомендуется к использованию, если на экране много однотипных запросов.
    private fun <T> mapDefaultRequest(request: Request<T>, requestUi: RequestUi<T>): RequestUi<T> {
        return RequestMapper.builder(request, requestUi)
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.simple())
                .reactOnSuccess { data -> ch.updateSucceed.accept() }
                .reactOnError { error -> ch.updateFailed.accept() }
                .build()
    }

    // 2# Самый длинный и гибкий путь - писать мапперы самостоятельно.
    private fun onTopKittenRequest(state: KittiesState, event: TopKittenRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.request, state.loadTopKittenRequestUi)
                // Приводим Request<Kitten> к Request<KittenUi>
                .mapRequest { kitten -> KittenUi(kitten) }
                // Либо берем данные из запроса, если он завершился удачно,
                // либо берем прошлые данных из RequestUi в стейте
                .mapData { request, data -> request.getDataOrNull() ?: data }
                // Мапим состояние загрузки запроса: здесь мы должны вернуть инстанс объекта Loading,
                // который можно использовать для отображения состояния этого запроса на UI пользователя
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
        return state.copy(loadTopKittenRequestUi = newRequestUi)
    }

    // #3.1 Наиболее оптимальный и лаконичный путь - использовать заранее созданные мапперы
    // для самых распространенных кейсов маппинга запроса и, при необходимости,
    // писать кастомные, для специфичных случаев.
    // Реализации мапперов создаются для каждого проекта вручную.
    // Реализацию мапперов семпла можно посмотреть в RequestMappers.kt.
    private fun onNewKittiesCountRequest(state: KittiesState, event: NewKittiesCountRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.request, state.loadNewKittiesCountRequestUi)
                // Мапим данные по-умолчанию (пытаемся получить новые данные, иначе оставляем старые)
                .mapData(RequestMappers.data.default())
                // Мапим состояния загрузки (оборачиваем isLoading запроса в SimpleLoading)
                .mapLoading(RequestMappers.loading.simple())
                // Без обработки ошибок - отправляем на UI комманды о том, успешно ли
                .reactOnSuccess { _ -> ch.updateSucceed.accept() }
                // или неудачно ли завершился наш запрос
                .reactOnError { _ -> ch.updateFailed.accept() }
                .build()
        return state.copy(loadNewKittiesCountRequestUi = newRequestUi)
    }

    // #3.2 Еще один пример
    private fun onPopularKittiesRequest(state: KittiesState, event: PopularKittiesRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.request, state.loadPopularKittiesRequestUi)
                // Преобразования списка типа T в список типа R
                .mapRequest { kittiesList -> kittiesList.map { KittenUi(it) } }
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.simple())
                .reactOnSuccess { _ -> ch.updateSucceed.accept() }
                .reactOnError { _ -> ch.updateFailed.accept() }
                .build()
        return state.copy(loadPopularKittiesRequestUi = newRequestUi)
    }

    // #3.3 И еще один
    private fun onMeowSendRequest(state: KittiesState, event: SendMeowRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.request, state.sendMeowRequestUi)
                .mapLoading(RequestMappers.loading.simple())
                .handleError(RequestMappers.error.forced(errorHandler))
                .reactOnSuccess { _ -> ch.meowSent.accept() }
                .build()
        return state.copy(sendMeowRequestUi = newRequestUi)
    }

    // #3.4 И еще один
    private fun onMeowUpdateCountRequest(state: KittiesState, event: UpdateMeowCountRequestEvent): KittiesState {
        val newRequestUi = RequestMapper.builder(event.request, state.updateMeowCountRequestUi)
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.simple())
                .handleError(RequestMappers.error.forced(errorHandler))
                .build()
        return state.copy(updateMeowCountRequestUi = newRequestUi)
    }
}
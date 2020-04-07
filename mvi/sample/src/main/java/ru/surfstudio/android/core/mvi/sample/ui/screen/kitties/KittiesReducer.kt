package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.KittiesEvent.*
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data.KittenUi
import ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.util.RequestMappers
import ru.surfstudio.android.core.mvi.ui.reducer.Reducer
import ru.surfstudio.android.core.mvi.ui.reducer.RequestMapper
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.Command
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SimpleLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
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
    val topKitten: KittenUi = loadTopKittenRequestUi.data ?: KittenUi()
    val newKittiesCount: Int = loadNewKittiesCountRequestUi.data ?: 0
    val popularKitties: List<KittenUi> = loadPopularKittiesRequestUi.data ?: emptyList()

    val meowCount: Int = updateMeowCountRequestUi.data ?: 0
    val isMeowButtonLoading: Boolean = listOf(sendMeowRequestUi, updateMeowCountRequestUi)
            .any { it.isLoading }
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
            is TopKitten.Req -> onTopKittenRequest(state, event)
            is NewKittiesCount.Req -> onNewKittiesCountRequest(state, event)
            is PopularKitties.Req -> onPopularKittiesRequest(state, event)
            is Meow.SendReq -> onMeowSendRequest(state, event)
            is Meow.UpdateCountReq -> onMeowUpdateCountRequest(state, event)
            is KittenClicked -> state.also { ch.kittenClicked.accept() }
            else -> state
        }
    }

    // Демо-метод, который инкапсулирует в себе стандартный (для экрана) маппинг запроса
    private fun <T> mapDefaultRequest(request: Request<T>, requestUi: RequestUi<T>): RequestUi<T> {
        return RequestMapper.builder(request, requestUi)
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.simple())
                .reactOnSuccess { data -> ch.updateSucceed.accept() }
                .reactOnError { error -> ch.updateFailed.accept() }
                .build()
    }

    private fun onTopKittenRequest(state: KittiesState, event: TopKitten.Req): KittiesState {
        val newRequestUi = when (1) {
            0 -> {
                // Самый длинный и гибкий путь - писать мапперы самостоятельно
                RequestMapper.builder(event.type, state.loadTopKittenRequestUi)
                        // Приводим Request<Kitten> к Request<KittenUi>
                        .mapRequest { kitten -> KittenUi(kitten) }
                        // Вручную маппим данные: либо берем их из запроса,
                        // если он завершился удачно, либо берем прошлые данных из RequestUi
                        .mapData { request, data -> request.dataOrNull ?: data }
                        // Мапим состояние загрузки запроса, здесь мы должны вернуть инстанс объекта
                        // Loading, который можно использовать для отображения состояния этого запроса
                        // на UI пользователя.
                        .mapLoading { request, data -> SimpleLoading(request.isLoading) }
                        // Обрабатываем ошибку запроса, если он завершился с ошибкой.
                        // Обработка идет по принципу Chain of Responsibility, т.е. если мы здесь
                        // не обработали ошибку, то обязаны вернуть false и передать обработку
                        // следующему обработчику по цепочке (если он, конечно же, есть)
                        .handleError { error, data, loading ->
                            when {
                                error is NoSuchElementException -> {
                                    ch.updateFailed.accept()
                                    true // Обработка ошибка на этом и закончится
                                }
                                else -> false // Обработка ошибки будет произведена и в errorHandler
                            }
                        }
                        // Этот код выполняет тот же функционал, что и handleError выше, но в более
                        // лаконичном стиле.
                        .handleSpecificError<NoSuchElementException> { error, data, loading ->
                            ch.updateFailed.accept()
                            true
                        }
                        // Выполнится только если вышележащий handleError вернул false
                        .handleError(RequestMappers.error.forced(errorHandler))
                        // Также мы можем реагировать на события жизненного цикла запроса
                        // и совершать определенный действия, связанные с этими событиями
                        .reactOnSuccess { request, data, loading ->
                            ch.updateSucceed.accept()
                        }
                        // Собираем цепочку, выполняем и получаем на выходе RequestUi.
                        .build()
            }
            else -> {
                // Наиболее оптимальный и лаконичный путь - использовать заранее созданные мапперы
                // Реализации мапперов можно посмотреть в RequestMappers.kt
                RequestMapper.builder(event.type, state.loadTopKittenRequestUi)
                        // Приводим Request<Kitten> к Request<KittenUi>
                        .mapRequest { kitten -> KittenUi(kitten) }
                        // Мапим данные по-умолчанию (пытаемся получить новые данные, иначе оставляем старые)
                        .mapData(RequestMappers.data.default())
                        // Мапим состояния загрузки (оборачиваем isLoading запроса в SimpleLoading)
                        .mapLoading(RequestMappers.loading.simple())
                        // Без обработки ошибок - отправляем на UI комманды о том, успешно ли
                        .reactOnSuccess { data -> ch.updateSucceed.accept() }
                        // или неудачно ли завершился наш запрос
                        .reactOnError { error -> ch.updateFailed.accept() }
                        .build()
            }
        }
        return state.copy(loadTopKittenRequestUi = newRequestUi)
    }

    private fun onNewKittiesCountRequest(state: KittiesState, event: NewKittiesCount.Req): KittiesState {
        // Наиболее быстрый в написании (для самых распространенных типов запросов):
        // либо в базовом редюсере для всего проекта, либо на конкретном экране пишем
        // кастомную функцию-маппер и передаем туда Request и RequestUi.
        val newRequestUi = mapDefaultRequest(event.type, state.loadNewKittiesCountRequestUi)
        return state.copy(loadNewKittiesCountRequestUi = newRequestUi)
    }

    private fun onPopularKittiesRequest(state: KittiesState, event: PopularKitties.Req): KittiesState {
        val newRequestUi = RequestMapper.builder(event.type, state.loadPopularKittiesRequestUi)
                // Преобразования списка типа T1 в список типа T2
                .mapRequest { kittiesList -> kittiesList.map { KittenUi(it) } }
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.simple())
                .reactOnSuccess { data -> ch.updateSucceed.accept() }
                .reactOnError { error -> ch.updateFailed.accept() }
                .build()
        return state.copy(loadPopularKittiesRequestUi = newRequestUi)
    }

    private fun onMeowSendRequest(state: KittiesState, event: Meow.SendReq): KittiesState {
        val newRequestUi = RequestMapper.builder(event.type, state.sendMeowRequestUi)
                .mapLoading(RequestMappers.loading.simple())
                .handleError(RequestMappers.error.forced(errorHandler))
                .reactOnSuccess { data -> ch.meowSent.accept() }
                .build()
        return state.copy(sendMeowRequestUi = newRequestUi)
    }

    private fun onMeowUpdateCountRequest(state: KittiesState, event: Meow.UpdateCountReq): KittiesState {
        val newRequestUi = RequestMapper.builder(event.type, state.updateMeowCountRequestUi)
                .mapData(RequestMappers.data.default())
                .mapLoading(RequestMappers.loading.simple())
                .handleError(RequestMappers.error.forced(errorHandler))
                .build()
        return state.copy(updateMeowCountRequestUi = newRequestUi)
    }
}
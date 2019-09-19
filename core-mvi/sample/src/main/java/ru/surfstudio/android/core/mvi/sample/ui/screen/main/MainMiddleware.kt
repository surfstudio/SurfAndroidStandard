package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseNavMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.NavigatorMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormActivityRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.network.toObservable
import javax.inject.Inject

/**
 * Главный Middleware с механизмом навигации
 */
class MainMiddleware @Inject constructor(
        baseNavMiddlewareDependency: BaseNavMiddlewareDependency,
        private val messageController: MessageController
) : BaseNavMiddleware<MainEvent>(baseNavMiddlewareDependency),
        NavigatorMiddleware<MainEvent> {

    override fun transform(eventStream: Observable<MainEvent>) = transformations(eventStream) {
        +mapNavigationAuto()
//        Когда нужна простая реакция на событие - можно использовать listenForResult с колбеком
        +listenForResult(InputFormActivityRoute::class.java, ::showResult)
//        Когда нужна трансформация результата в событие - используется listenForResultMap
//        +listenForResultMap(InputFormActivityRoute::class.java) {
//            showResult(it)
//            MainEvent.OpenComplexList().toObservable()
//        }

    }

    private fun showResult(result: ScreenResult<String>) {
        val message = if (result.isSuccess) result.data else "No result"
        messageController.show(message)
    }
}
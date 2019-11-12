package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.ObserveResult
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.listenForResult
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.observeResult
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormActivityRoute
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.core.mvi.sample.ui.screen.main.MainEvent.*
import javax.inject.Inject

/**
 * Главный Middleware с механизмом навигации
 */
@PerScreen
class MainMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val messageController: MessageController
) : BaseMiddleware<MainEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<MainEvent>) = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware,
//        Когда нужна простая реакция на событие - можно использовать listenForResult с колбеком
                listenForResult(InputFormActivityRoute::class.java, ::Navigation, ::showResult)
//        Когда нужна трансформация результата в событие - используется listenForResultMap
//        +listenForResultMap(InputFormActivityRoute::class.java) {
//            showResult(it)
//            Navigation().open(ComplexListActivityRoute()).toObservable())
//        }
        )

    }

    private fun showResult(result: ScreenResult<String>) {
        val message = if (result.isSuccess) result.data else "No result"
        messageController.show(message)
    }
}
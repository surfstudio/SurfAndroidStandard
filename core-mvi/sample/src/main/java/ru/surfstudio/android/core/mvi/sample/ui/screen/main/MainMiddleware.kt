package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseNavMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation.NavigatorMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormActivityRoute
import ru.surfstudio.android.core.mvi.sample.ui.screen.main.MainEvent.InputFormResult
import ru.surfstudio.android.message.MessageController
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
        +mapNavigationDefault()
        +openScreenForResult(InputFormActivityRoute::class.java, InputFormResult())
        +react<InputFormResult>(::showResult)
    }

    private fun showResult(event: InputFormResult) {
        val message = if (event.result.isSuccess) event.result.data else "No result"
        messageController.show(message)
    }
}
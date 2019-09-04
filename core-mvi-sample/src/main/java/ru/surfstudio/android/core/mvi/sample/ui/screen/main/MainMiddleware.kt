package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseNavMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.NavigatorMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormActivityRoute
import ru.surfstudio.android.message.MessageController
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.main.MainEvent.*

class MainMiddleware @Inject constructor(
        baseNavMiddlewareDependency: BaseNavMiddlewareDependency,
        private val messageController: MessageController
) : BaseNavMiddleware<MainEvent>(baseNavMiddlewareDependency),
        NavigatorMiddleware<MainEvent> {

    override fun transform(eventStream: Observable<MainEvent>) = transformations(eventStream) {
        +mapNavigationDefault()
        +openScreenForResult(InputFormActivityRoute::class.java, InputFormResult())
        +react<InputFormResult> { messageController.show(it.result.data) }
    }
}
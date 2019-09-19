package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseNavMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormEvent.InputFormClosed
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormEvent.SubmitClicked
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Middleware экрана [InputFormActivityView]
 */
@PerScreen
class InputFormMiddleware @Inject constructor(
        baseNavMiddlewareDependency: BaseNavMiddlewareDependency,
        private val sh: InputFormStateHolder
) : BaseNavMiddleware<InputFormEvent>(baseNavMiddlewareDependency) {

    override fun transform(eventStream: Observable<InputFormEvent>) = transformations(eventStream) {
        +eventStream.mapNavigationAuto()
        +map<SubmitClicked> { InputFormClosed(InputFormActivityRoute(), sh.inputString.value) }
    }
}
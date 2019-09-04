package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseNavMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.input.InputFormEvent.*

@PerScreen
class InputFormMiddleware @Inject constructor(
        baseNavMiddlewareDependency: BaseNavMiddlewareDependency,
        private val sh: InputFormStateHolder
) : BaseNavMiddleware<InputFormEvent>(baseNavMiddlewareDependency) {

    override fun transform(eventStream: Observable<InputFormEvent>): Observable<out InputFormEvent> =
            transformations(eventStream) {
                +eventStream.closeScreenDefault()
                +map<SubmitClicked> { InputFormClosed(InputFormActivityRoute(), sh.inputString.value) }
            }
}
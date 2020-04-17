package ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.input

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.close
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.input.InputFormEvent.*
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Middleware экрана [InputFormActivityView]
 */
@PerScreen
class InputFormMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val sh: InputFormStateHolder
) : BaseMiddleware<InputFormEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<InputFormEvent>) = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware,
                SubmitClicked::class mapTo { Navigation().close(InputFormActivityRoute(), sh.inputString.value) }
        )
    }
}
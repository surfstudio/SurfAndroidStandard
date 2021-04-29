package ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.dialog.standard.StandardReactDialogRoute
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.open
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.input.InputFormActivityRoute
import ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.main.MainEvent.Navigation
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.rx.extension.toObservable
import javax.inject.Inject

/**
 * Главный Middleware с механизмом навигации
 */
@PerScreen
class MainMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware,
        private val messageController: MessageController,
        private val activityNavigator: ActivityNavigator
) : BaseMiddleware<MainEvent>(baseMiddlewareDependency) {

    override fun transform(eventStream: Observable<MainEvent>) = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware,
                activityNavigator.observeResult(InputFormActivityRoute::class.java).flatMap(::showResultMap)
        )

    }

    /**
     * Показ снека с результирующим текстом, либо показ диалога с сообщением о том,
     * что результата нет, и предложением открыть экран с вводом текста заново.
     */
    private fun showResultMap(result: ScreenResult<String?>): Observable<MainEvent> {
        val hasResultData = result.isSuccess && !result.data.isNullOrEmpty()
        return if (hasResultData) {
            messageController.show(result.data!!).skip()
        } else {
            Navigation().open(StandardReactDialogRoute<MainEvent>(
                title = "No result",
                message = "Try again?",
                positiveButtonEvent = Navigation().open(InputFormActivityRoute()))
            ).toObservable()
        }
    }

    /**
     * Простой показ снека с результирующим текстом
     */
    private fun showResultReact(result: ScreenResult<String>) {
        val hasResultData = result.isSuccess && !result.data.isNullOrEmpty()
        val message = if (hasResultData) result.data else "No result"
        messageController.show(message)
    }
}
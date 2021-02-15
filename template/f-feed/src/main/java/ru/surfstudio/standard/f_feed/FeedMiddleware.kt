package ru.surfstudio.standard.f_feed

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_feed.FeedEvent.Navigation
import ru.surfstudio.standard.ui.mvi.navigation.AppNavigationMiddleware
import javax.inject.Inject

@PerScreen
internal class FeedMiddleware @Inject constructor(
        basePresenterDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: AppNavigationMiddleware
) : BaseMiddleware<FeedEvent>(basePresenterDependency) {

    override fun transform(eventStream: Observable<FeedEvent>): Observable<out FeedEvent> = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware
        )
    }
}
package ru.surfstudio.standard.f_profile

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.route.result.ActivityResultData
import ru.surfstudio.android.navigation.rx.extension.observeScreenResult
import ru.surfstudio.android.rx.extension.toObservable
import ru.surfstudio.standard.f_profile.ProfileEvent.Navigation
import ru.surfstudio.standard.ui.mvi.navigation.AppNavigationMiddleware
import ru.surfstudio.standard.ui.mvi.navigation.extension.startForResult
import javax.inject.Inject

@PerScreen
internal class ProfileMiddleware @Inject constructor(
        basePresenterDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: AppNavigationMiddleware,
        private val screenResultObserver: ScreenResultObserver
) : BaseMiddleware<ProfileEvent>(basePresenterDependency) {

    override fun transform(eventStream: Observable<ProfileEvent>): Observable<out ProfileEvent> = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware,
                observeVoiceSearchResult().flatMap { onVoiceSearchResult(it.data) },
                ProfileEvent.Search::class mapTo { openSearch() }
        )
    }

    private fun onVoiceSearchResult(it: String): Observable<out ProfileEvent> {
        ru.surfstudio.android.logger.Logger.d(it)
        return ProfileEvent.SkipEvent.toObservable()
    }

    private fun observeVoiceSearchResult(): Observable<out ActivityResultData<String>> {
        return screenResultObserver.observeScreenResult(
                SearchRoute("asd")
        )
    }

    private fun openSearch(): ProfileEvent {
        return Navigation().startForResult(SearchRoute("asd"))
    }
}
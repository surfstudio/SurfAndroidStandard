package ru.surfstudio.standard.f_profile

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_profile.ProfileEvent.Navigation
import ru.surfstudio.standard.ui.mvi.navigation.AppNavigationMiddleware
import javax.inject.Inject

@PerScreen
internal class ProfileMiddleware @Inject constructor(
        basePresenterDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: AppNavigationMiddleware
) : BaseMiddleware<ProfileEvent>(basePresenterDependency) {

    override fun transform(eventStream: Observable<ProfileEvent>): Observable<out ProfileEvent> = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware
        )
    }
}
package ru.surfstudio.standard.f_main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.extension.toObservable
import ru.surfstudio.standard.f_main.MainEvent.Navigation
import ru.surfstudio.standard.ui.mvi.navigation.base.NavigationMiddleware
import ru.surfstudio.standard.ui.mvi.navigation.extension.replace
import ru.surfstudio.standard.ui.navigation.routes.MainBarRoute
import javax.inject.Inject

@PerScreen
internal class MainMiddleware @Inject constructor(
        basePresenterDependency: BaseMiddlewareDependency,
        private val navigationMiddleware: NavigationMiddleware
) : BaseMiddleware<MainEvent>(basePresenterDependency) {

    override fun transform(eventStream: Observable<MainEvent>): Observable<out MainEvent> = transformations(eventStream) {
        addAll(
                Navigation::class decomposeTo navigationMiddleware,
                onCreate() eventMap { Navigation().replace(MainBarRoute()).toObservable() }
        )
    }
}

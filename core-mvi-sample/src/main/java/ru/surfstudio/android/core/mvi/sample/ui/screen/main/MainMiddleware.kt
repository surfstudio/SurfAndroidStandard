package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.OpenScreenMiddleware
import javax.inject.Inject

class MainMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMiddleware<MainEvent>(baseMiddlewareDependency),
        OpenScreenMiddleware<MainEvent> {

    override fun transform(eventStream: Observable<MainEvent>) =
            eventStream.openScreenDefault()

    override fun flatMap(event: MainEvent): Observable<out MainEvent> = skip()
}
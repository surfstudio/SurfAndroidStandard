package ru.surfstudio.android.core.mvi.sample.ui.screen.main

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseMiddleware
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.ReactiveListRoute
import javax.inject.Inject
import ru.surfstudio.android.core.mvi.sample.ui.screen.main.MainEvent.*

class MainMiddleware @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseMiddleware<MainEvent>(baseMiddlewareDependency) {

    override fun flatMap(event: MainEvent): Observable<out MainEvent> = when (event) {
        is OpenComplexList -> activityNavigator.start(ReactiveListRoute()).skip()
        is OpenSimpleList -> skip() //TODO
        is OpenInputForm -> skip()  //TODO
        else -> skip()
    }
}
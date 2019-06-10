package ru.surfstudio.android.mvp.binding.rx.sample.react

import ru.surfstudio.android.core.mvp.binding.react.Reducer
import ru.surfstudio.android.core.mvp.binding.react.ReducerPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class ReactivePresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val bm: ReactiveBindModel
) : ReducerPresenter(basePresenterDependency) {

    override val reducers: List<Reducer> = listOf(bm.queryReducer, bm.listReducer)
}
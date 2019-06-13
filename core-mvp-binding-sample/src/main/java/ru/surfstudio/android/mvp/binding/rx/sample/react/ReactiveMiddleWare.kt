package ru.surfstudio.android.mvp.binding.rx.sample.react

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.LoadNextData
import ru.surfstudio.android.core.mvp.binding.react.event.ReloadData
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.BaseMiddleware
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.LoadListEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.ListFeature
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class ReactiveMiddleWare @Inject constructor(
        baseMiddlewareDependency: BaseMiddlewareDependency,
        private val listFeature: ListFeature
) : BaseMiddleware(baseMiddlewareDependency) {

    override fun onFirstLoad() {
        super.onFirstLoad()
        loadData()
    }

    override fun accept(event: Event) {
        when (event) {
            is ReloadData -> loadData()
            is LoadNextData -> loadData(listFeature.state.value.data.get().nextPage)
        }
    }

    private fun loadData(offset: Int = 0) = createListObservable(offset)
            .send(LoadListEvent())

    private fun createListObservable(page: Int) = Observable.timer(2, TimeUnit.SECONDS).map {
        DataList<String>(
                (1..20).map { (it + page * 20).toString() },
                page,
                20
        )
    }
}
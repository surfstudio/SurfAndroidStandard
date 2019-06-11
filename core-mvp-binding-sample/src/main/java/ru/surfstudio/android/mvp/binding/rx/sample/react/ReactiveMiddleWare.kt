package ru.surfstudio.android.mvp.binding.rx.sample.react

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.LoadData
import ru.surfstudio.android.core.mvp.binding.react.ui.BaseMiddleWare
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.EventManager
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ListRxEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.ListFeature
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@PerScreen
class ReactiveMiddleWare @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        hub: EventManager,
        private val listFeature: ListFeature
) : BaseMiddleWare(basePresenterDependency, hub) {

    override fun onFirstLoad() {
        loadData()
    }

    override fun <T : Event> accept(event: T) {
        when (event) {
            is LoadData.First -> loadData()
            is LoadData.Next -> loadData(listFeature.list.value.nextPage)
            is LoadData.All -> {
                //PaginationUtil.requestPortions
            }
        }
    }

    private fun loadData(offset: Int = 0) = createListObservable(offset)
            .sendEvent(ListRxEvent())

    private fun createListObservable(page: Int) = Observable.timer(2, TimeUnit.SECONDS).map {
        DataList<String>(
                (1..20).map { (it + page * 20).toString() },
                page,
                20
        )
    }
}
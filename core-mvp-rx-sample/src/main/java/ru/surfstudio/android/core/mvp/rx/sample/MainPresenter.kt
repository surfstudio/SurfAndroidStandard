package ru.surfstudio.android.core.mvp.rx.sample

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import ru.surfstudio.android.logger.Logger
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BaseRxPresenter<MainModel, MainActivityView>(basePresenterDependency) {
    private val model = MainModel()
    override fun getRxModel() = model

    override fun onFirstLoad() {
        super.onFirstLoad()

        model.incAction.subscribe { _ -> model.counterState.transform { it + 1 } }
        model.decAction.subscribe { _ -> model.counterState.transform { it - 1 } }

        var i = 0

        model.longQueryAction
                .doOnEach { Observable.just(++i).delay(1L, TimeUnit.SECONDS) }
                .subscribeIoHandleError(
                        {
                            Logger.d("i=$i")
                        },
                        {

                        }
                )

        model.doubleTextAction.subscribe { _ -> model.textEditState.textState.transform { it + it } }
    }
}
package ru.surfstudio.android.core.mvp.rx.sample.checkbox

import io.reactivex.Observable
import io.reactivex.functions.Function3
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.mvp.rx.ui.BaseRxPresenter
import javax.inject.Inject

class CheckboxPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BaseRxPresenter<CheckboxModel, CheckboxActivityView>(basePresenterDependency) {

    private val model = CheckboxModel()
    override fun getRxModel() = model

    override fun onFirstLoad() {
        super.onFirstLoad()

        subscribe(Observable.combineLatest<Boolean, Boolean, Boolean, Int>(
                model.checkAction1.getObservable(),
                model.checkAction2.getObservable(),
                model.checkAction3.getObservable(),
                Function3 { t1, t2, t3 -> t1.toInt() + t2.toInt() + t3.toInt() }))
        { model.count.getConsumer().accept(it) }
    }
}

private fun Boolean.toInt() = if (this) 1 else 0

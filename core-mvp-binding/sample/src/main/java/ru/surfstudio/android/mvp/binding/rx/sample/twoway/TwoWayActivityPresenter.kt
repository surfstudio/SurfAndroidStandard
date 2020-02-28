package ru.surfstudio.android.mvp.binding.rx.sample.twoway

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

/**
 * Презентер [TwoWayActivityView].
 */
@PerScreen
class TwoWayActivityPresenter @Inject constructor(
        private val bm: TwoWayBindModel,
        basePresenterDependency: BasePresenterDependency
): BaseRxPresenter(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        bm.moneyInputAction
            .observable
            .map { it.replace("\\D".toRegex(), "") } // функция обратной трансформации {сумма ₽} - ₽
            .distinctUntilChanged()
            .bindTo { bm.moneyState.accept(it) }

        Observable
            .interval(12, TimeUnit.SECONDS)
            .map { Random.nextInt(1,99999999).toString() }
            .bindTo{ bm.moneyState.accept(it) }
    }
}
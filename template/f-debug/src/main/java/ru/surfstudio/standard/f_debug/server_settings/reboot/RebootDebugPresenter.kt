package ru.surfstudio.standard.f_debug.server_settings.reboot

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DELAY_SEC = 3L // количество секунд, по истечении которого будет произведён перезапуск

/**
 * Презентер экрана перезапуска приложения
 */
class RebootDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<RebootActivityDebugView>(basePresenterDependency) {

    private val screenModel: RebootDebugScreenModel = RebootDebugScreenModel(DELAY_SEC)

    fun rebootApp() {
        val delay = Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil { it == DELAY_SEC }

        subscribe(delay) {
            screenModel.countdownToRestart = DELAY_SEC - it
            view.render(screenModel)
        }
    }
}
package ru.surfstudio.standard.f_debug.server_settings.reboot

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.standard.f_debug.server_settings.reboot.interactor.RebootInteractor
import ru.surfstudio.standard.f_debug.server_settings.reboot.route.LauncherActivityRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DELAY_SEC = 1L // количество секунд, по истечении которого будет произведён перезапуск

/**
 * Презентер экрана перезапуска приложения
 */
class RebootDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val rebootInteractor: RebootInteractor
) : BasePresenter<RebootActivityDebugView>(basePresenterDependency) {

    override fun onFirstLoad() {
        startCounting()
    }

    private fun startCounting() {
        val delay = Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil { it == DELAY_SEC }

        subscribe(delay) {
            if (it == 0L) {
                rebootInteractor.reboot(LauncherActivityRoute())
            }
        }
    }
}
package ru.surfstudio.standard.f_debug.server_settings.reboot

import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.DebugInteractor
import ru.surfstudio.standard.f_debug.server_settings.reboot.route.LauncherActivityRoute
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val DELAY_SEC = 4L // количество секунд, по истечении которого будет произведён перезапуск

/**
 * Презентер экрана перезапуска приложения
 */
class RebootDebugPresenter @Inject constructor(
        private val route: RebootDebugActivityRoute,
        private val debugInteractor: DebugInteractor,
        private val activityNavigator: ActivityNavigator,
        private val messageController: MessageController,
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<RebootActivityDebugView>(basePresenterDependency) {

    private val sm = RebootDebugScreenModel(DELAY_SEC)
    private var delayDisposable = Disposables.disposed()

    override fun onFirstLoad() {
        startCounting()
        view.render(sm)
    }

    fun rebootNow() {
        cancelReboot()
        debugInteractor.reboot(LauncherActivityRoute())
    }

    fun cancelReboot() {
        delayDisposable.dispose()
        messageController.showToast(R.string.reboot_canceled_toast)
        activityNavigator.finishWithResult(route, false, true)
    }

    private fun startCounting() {
        val delay = Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil { it == DELAY_SEC }

        delayDisposable = subscribe(delay) {
            sm.secondBeforeReboot = DELAY_SEC - it - 1
            view.render(sm)

            if (it == DELAY_SEC - 1) {
                debugInteractor.reboot(LauncherActivityRoute())
            }
        }
    }
}
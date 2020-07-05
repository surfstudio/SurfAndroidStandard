package ru.surfstudio.standard.f_debug.server_settings

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute
import ru.surfstudio.standard.f_debug.DebugInteractor
import javax.inject.Inject
import kotlin.math.log2

/**
 * Презентер экрана настроек сервера
 */
@PerScreen
class ServerSettingsDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val debugInteractor: DebugInteractor,
        private val activityNavigator: ActivityNavigator
) : BasePresenter<ServerSettingsDebugActivityView>(basePresenterDependency) {

    private val sm: ServerSettingsDebugScreenModel =
            ServerSettingsDebugScreenModel(
                    debugInteractor.isChuckEnabled,
                    debugInteractor.isTestServerEnabled,
                    millisecondsToSeconds(debugInteractor.requestDelay),
                    delayMillisecondsToCoefficient(debugInteractor.requestDelay)
            )

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun setChuckEnabled(value: Boolean) {
        debugInteractor.isChuckEnabled = value
        activityNavigator.start(RebootDebugActivityRoute())
    }

    fun setTestServerEnabled(isEnabled: Boolean) {
        debugInteractor.isTestServerEnabled = isEnabled
        activityNavigator.start(RebootDebugActivityRoute())
        activityNavigator.finishAffinity()
    }

    fun requestDelayCoefficientChanges(coefficientObservable: Observable<Int>) {
        subscribe(coefficientObservable) {
            val requestDelayMilliseconds = delayCoefficientToMilliseconds(it)
            debugInteractor.requestDelay = requestDelayMilliseconds
            sm.requestDelayCoefficient = it
            sm.requestDelaySeconds = millisecondsToSeconds(requestDelayMilliseconds)
            view.render(sm)
        }
    }

    private fun millisecondsToSeconds(milliseconds: Long): Float {
        return milliseconds / 1000.0F
    }

    private fun delayMillisecondsToCoefficient(delay: Long): Int {
        return if (delay == 0L) 0 else log2(delay / 500.0).toInt() + 1
    }

    private fun delayCoefficientToMilliseconds(coefficient: Int): Long {
        return if (coefficient == 0) 0 else (1L shl (coefficient - 1)) * 500L
    }
}
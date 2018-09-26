package ru.surfstudio.standard.f_debug.debug

import android.support.annotation.StringRes
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.notification.interactor.push.storage.FcmStorage
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.debug_controllers.DebugControllersActivityRoute
import javax.inject.Inject

/**
 * Презентер экрана показа информации для дебага
 */
@PerScreen
class DebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val stringsProvider: StringsProvider,
        private val fcmStorage: FcmStorage
) : BasePresenter<DebugActivityView>(basePresenterDependency) {

    fun openControllersScreen() {
        activityNavigator.start(DebugControllersActivityRoute())
    }

    fun showFcmToken() {
        val fcmToken = fcmStorage.fcmToken
        val message: String
        if (fcmToken.isEmpty()) {
            message = getString(R.string.fcm_empty_message)
        } else {
            message = getString(R.string.fcm_copied_message)
            Logger.d("FCM-token: $fcmToken")
        }
        view.showMessage(message)
    }

    private fun getString(@StringRes stringId: Int): String = stringsProvider.getString(stringId)
}

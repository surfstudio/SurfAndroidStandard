package ru.surfstudio.android.broadcast.extension.sample.ui.screen.main

import android.telephony.SmsManager
import ru.surfstudio.android.broadcast.extension.sample.interactor.SmsBroadcastReceiver
import ru.surfstudio.android.broadcast.extension.sample.interactor.SmsPermissionRequest
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val permissionManager: PermissionManager,
        private val messageController: MessageController,
        private val smsBroadcastReceiver: SmsBroadcastReceiver
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()
    private val smsPermissionRequest = SmsPermissionRequest()

    override fun onFirstLoad() {
        subscribe(smsBroadcastReceiver.observeBroadcast()) { sms ->
            sms?.let {
                messageController.show(it)
            }
        }
    }

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(sm)
    }

    fun sendSms(phoneNumber: String, message: String) {
        if (phoneNumber.isNotEmpty()) {
            subscribe(permissionManager.request(smsPermissionRequest).toObservable(), {
                SmsManager.getDefault()
                        .sendTextMessage(
                                phoneNumber,
                                null,
                                message,
                                null,
                                null
                        )
            }, {
                Logger.e(it)
            })
        }
    }
}
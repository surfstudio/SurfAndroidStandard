package ru.surfstudio.standard.f_debug.memory

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.standard.f_debug.memory.route.StorageDebugScreenRoute
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute
import ru.surfstudio.standard.f_debug.DebugInteractor
import javax.inject.Inject

@PerScreen
class MemoryDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val debugInteractor: DebugInteractor,
        private val messageController: MessageController
) : BasePresenter<MemoryDebugActivityView>(basePresenterDependency) {

    private val screenModel = MemoryDebugScreenModel(debugInteractor.isLeakCanaryEnabled)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        view.render(screenModel)

        subscribe(activityNavigator.observeResult<Boolean>(RebootDebugActivityRoute::class.java)) {
            if (it.isSuccess && !it.data) messageController.showToast(R.string.reboot_canceled_toast)
        }
    }

    fun setLeakCanaryEnabled(isEnabled: Boolean) {
        debugInteractor.isLeakCanaryEnabled = isEnabled
        activityNavigator.startForResult(RebootDebugActivityRoute())
    }

    fun openStorageDebugScreen() {
        activityNavigator.start(StorageDebugScreenRoute())
    }
}
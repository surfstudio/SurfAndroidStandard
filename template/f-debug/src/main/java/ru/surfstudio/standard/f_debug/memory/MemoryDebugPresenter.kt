package ru.surfstudio.standard.f_debug.memory

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.standard.f_debug.memory.storage.MemoryDebugStorage
import ru.surfstudio.standard.f_debug.server_settings.reboot.RebootDebugActivityRoute
import javax.inject.Inject

class MemoryDebugPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val activityNavigator: ActivityNavigator,
        private val memoryDebugStorage: MemoryDebugStorage
) : BasePresenter<MemoryDebugActivityView>(basePresenterDependency) {

    private val screenModel = MemoryDebugScreenModel(memoryDebugStorage.isLeakCanaryEnabled)

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        view.render(screenModel)
    }

    fun setLeakCanaryEnabled(isEnabled: Boolean) {
        memoryDebugStorage.isLeakCanaryEnabled = isEnabled
        activityNavigator.start(RebootDebugActivityRoute())
        activityNavigator.finishAffinity()
    }
}
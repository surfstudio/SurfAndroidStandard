package ru.surfstudio.android.core.ui.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.sample.R
import ru.surfstudio.android.core.ui.sample.ui.core.CustomOnRestoreStateDelegate
import ru.surfstudio.android.core.ui.sample.ui.core.CustomOnSaveStateDelegate
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @Inject
    internal lateinit var messageController: MessageController

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        // Регистрация делегата только при первом старте экрана
        if (!viewRecreated) {
            val delegateManager = persistentScope.screenEventDelegateManager
            val saveStateDelegate = CustomOnSaveStateDelegate(delegateManager)
            val restoreStateDelegate = CustomOnRestoreStateDelegate(delegateManager)
            messageController.show(R.string.snackbar_message)
        }
    }

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = MainScreenConfigurator(intent)
}

package ru.surfstudio.android.core.ui.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseDelegate
import ru.surfstudio.android.core.ui.sample.R
import ru.surfstudio.android.core.ui.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.message.MessageController
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
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        // Регистрация делегата только при первом старте экрана
        if (!viewRecreated) {
            val delegate = object : OnPauseDelegate {
                init {
                    persistentScope.screenEventDelegateManager.registerDelegate(this)
                }

                override fun onPause() = Logger.d("CustomOnPauseDelegate onPause")
            }
            messageController.show("$delegate registered")
        }
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }
}

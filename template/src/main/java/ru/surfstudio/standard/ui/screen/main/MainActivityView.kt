package ru.surfstudio.standard.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.find
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.standard.R
import ru.surfstudio.standard.ui.base.configurator.ActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        placeholder.render(LoadState.MAIN_LOADING)
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getName(): String {
        return "main"
    }

    override fun getPresenters(): Array<CorePresenter<*>> {
        return arrayOf(presenter)
    }

    override fun createConfigurator(): ActivityScreenConfigurator {
        return MainScreenConfigurator(intent)
    }
}
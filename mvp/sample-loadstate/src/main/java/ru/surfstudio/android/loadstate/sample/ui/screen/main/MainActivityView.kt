package ru.surfstudio.android.loadstate.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.CoreActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана семпла работы с лоадстейтами
 */
class MainActivityView : CoreActivityView() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator = MainScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        initListeners()
    }

    private fun initListeners() {
        activity_main_default_btn.setOnClickListener { presenter.openDefaultStatesScreen() }
        activity_main_stubs_btn.setOnClickListener { presenter.openStubsStatesScreen() }
    }

    override fun getScreenName(): String = "main"
}

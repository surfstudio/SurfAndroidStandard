package ru.surfstudio.android.broadcast.extension.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.broadcast.extension.sample.R
import ru.surfstudio.android.broadcast.extension.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.utilktx.ktx.ui.activity.hideKeyboard
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String  = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        send_btn.setOnClickListener {
            hideKeyboard()
            presenter.sendSms(number_et.text.toString(), message_et.text.toString())
        }
    }

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)
}

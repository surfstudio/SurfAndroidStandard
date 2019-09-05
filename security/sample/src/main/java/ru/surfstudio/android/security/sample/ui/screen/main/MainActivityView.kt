package ru.surfstudio.android.security.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.security.session.SessionalActivity
import ru.surfstudio.android.security.ui.deleteContextMenuItems
import ru.surfstudio.android.security.ui.disableContextMenu
import ru.surfstudio.android.security.ui.enableSecureMode
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>(), SessionalActivity {

    @Inject
    lateinit var presenter: MainPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        //Включение secure mode для Activity
        enableSecureMode()
        initListeners()
    }

    override fun renderInternal(screenModel: MainScreenModel) {}

    override fun getScreenName(): String = "main"

    private fun initListeners() {
        check_root_btn.setOnClickListener { presenter.checkRoot() }
        sign_in_btn.setOnClickListener { presenter.createPin(api_key_et.text.toString()) }

        //удаление пунктов "Копировать" и "Вырезать" из контекстного меню в EditText
        api_key_et.deleteContextMenuItems(android.R.id.copy, android.R.id.cut)
        //отключение контекстного меню у EditText
        api_key_et.disableContextMenu()
    }

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

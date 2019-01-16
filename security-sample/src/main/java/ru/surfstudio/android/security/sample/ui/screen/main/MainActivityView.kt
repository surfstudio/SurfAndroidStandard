package ru.surfstudio.android.security.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.ui.base.configurator.CustomActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = MainScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        initListeners()
    }

    override fun renderInternal(screenModel: MainScreenModel) { }

    override fun getScreenName(): String = "main"

    private fun initListeners() {
        check_root_btn.setOnClickListener { presenter.checkRoot() }
        sign_in_btn.setOnClickListener { presenter.createPin(api_key_et.text.toString()) }

        api_key_et.customSelectionActionModeCallback = object: ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = false

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                //удаляем пункт "копировать"
                val menuItemCopy = menu.findItem(android.R.id.copy)
                menuItemCopy?.let {
                    menu.removeItem(android.R.id.copy)
                }

                //удаляем пункт "вырезать"
                val menuItemCut = menu.findItem(android.R.id.cut)
                menuItemCut?.let {
                    menu.removeItem(android.R.id.cut)
                }
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                //empty
            }
        }
    }

    fun showMessage(message: String) {
        toast(message)
    }
}

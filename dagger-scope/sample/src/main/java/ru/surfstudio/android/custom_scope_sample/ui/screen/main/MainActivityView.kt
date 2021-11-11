package ru.surfstudio.android.custom_scope_sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.custom_scope_sample.R
import ru.surfstudio.android.custom_scope_sample.ui.base.LoginScopeStorage
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivity"

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        open_another_screen_btn.setOnClickListener { presenter.openAnotherScreen() }
    }

    override fun renderInternal(sm: MainScreenModel) {}

    override fun onStart() {
        super.onStart()
        LoginScopeStorage.addActivity(this::class.java)
    }

    override fun onDestroy() {
        LoginScopeStorage.removeActivity(this::class.java)
        super.onDestroy()
    }

    fun showMessage(message: String) {
        if (message.isNotEmpty()) {
            Toast
                .makeText(this, message, Toast.LENGTH_SHORT)
                .apply {
                    show()
                }
        }
    }
}

package ru.surfstudio.android.mvp.dialog.sample.ui.screen.main

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.IdRes
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.mvp.dialog.sample.R
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью главного экрана
 */
class MainActivityView : BaseRenderableActivityView<MainScreenModel>() {
    override fun getScreenName(): String = "MainActivity"

    @Inject
    internal lateinit var presenter: MainPresenter

    @IdRes
    override fun getContentView(): Int = R.layout.activity_main

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        show_simple_dialog_btn.setOnClickListener { presenter.showSimpleDialog() }
        show_simple_bottomsheet_dialog_btn.setOnClickListener { presenter.showSimpleBottomSheetDialog() }
        show_complex_dialog_btn.setOnClickListener { presenter.showComplexDialog() }
        show_complex_bottomsheet_dialog_btn.setOnClickListener { presenter.showComplexBottomSheetDialog() }
    }

    override fun renderInternal(sm: MainScreenModel) {}

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): DefaultActivityScreenConfigurator =
        MainScreenConfigurator(intent)

    fun showSimpleDialogAcceptedMessage() {
        toast(getString(R.string.simple_dialog_accepted))
    }

    fun showSimpleBottomSheetDialogAcceptedMessage() {
        toast(getString(R.string.simple_bottomsheet_dialog_accepted))
    }

    fun showMessage(message: String) {
        toast(message)
    }

    private fun toast(message: String) {
        Toast
            .makeText(this, message, Toast.LENGTH_SHORT)
            .apply {
                show()
            }
    }
}
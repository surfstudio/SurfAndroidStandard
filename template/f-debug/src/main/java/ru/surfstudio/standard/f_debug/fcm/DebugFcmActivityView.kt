package ru.surfstudio.standard.f_debug.fcm

import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import androidx.core.widget.toast
import kotlinx.android.synthetic.main.activity_debug_fcm.*
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.utilktx.ktx.ui.view.goneIf
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import javax.inject.Inject

/**
 * Вью экрана показа fcm-токена
 */
class DebugFcmActivityView : BaseLdsActivityView<DebugFcmScreenModel>() {

    @Inject
    lateinit var presenter: DebugFcmPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_debug_fcm

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        initListeners()
    }

    override fun renderInternal(screenModel: DebugFcmScreenModel) {
        fcm_tv.text = screenModel.fcmToken

        val hasFcmToken = screenModel.loadState != LoadState.EMPTY
        container.goneIf(!hasFcmToken)
        placeholder.goneIf(hasFcmToken)
    }

    override fun getScreenName(): String = "debug_fcm"

    override fun getPlaceHolderView(): PlaceHolderViewInterface = placeholder

    private fun initListeners() {
        copy_fcm_btn.setOnClickListener { presenter.copyFcmToken() }
        placeholder.buttonLambda = { presenter.loadFcmToken() }
    }

    fun showMessage(message: String) {
        toast(message)
    }
}

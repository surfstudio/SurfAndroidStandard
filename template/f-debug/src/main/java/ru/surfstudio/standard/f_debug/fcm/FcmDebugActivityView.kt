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
import ru.surfstudio.android.utilktx.ktx.ui.view.copyTextToClipboard
import ru.surfstudio.android.utilktx.ktx.ui.view.goneIf
import ru.surfstudio.standard.base_ui.component.provider.ComponentProvider
import javax.inject.Inject

/**
 * Вью экрана показа fcm-токена
 */
class FcmDebugActivityView : BaseLdsActivityView<FcmDebugScreenModel>() {

    @Inject
    lateinit var presenter: FcmDebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = ComponentProvider.createActivityScreenConfigurator(intent, this::class)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_debug_fcm

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    override fun renderInternal(screenModel: FcmDebugScreenModel) {
        fcm_tv.text = screenModel.fcmToken

        val hasFcmToken = screenModel.loadState != LoadState.EMPTY
        container.goneIf(!hasFcmToken)
        placeholder.goneIf(hasFcmToken)
    }

    override fun getScreenName(): String = "debug_fcm"

    override fun getPlaceHolderView(): PlaceHolderViewInterface = placeholder

    private fun initListeners() {
        fcm_tv.setOnClickListener { presenter.copyFcmToken() }
        placeholder.buttonLambda = { presenter.loadFcmToken() }
    }

    fun copyFcmToken() = fcm_tv.copyTextToClipboard()

    fun showMessage(message: String) {
        toast(message)
    }
}

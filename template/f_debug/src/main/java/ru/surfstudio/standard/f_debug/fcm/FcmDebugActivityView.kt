package ru.surfstudio.standard.f_debug.fcm

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_fcm_debug.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.utilktx.ktx.ui.view.copyTextToClipboard
import ru.surfstudio.android.utilktx.ktx.ui.view.goneIf
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.FcmDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа fcm-токена
 */
class FcmDebugActivityView : BaseRenderableActivityView<FcmDebugScreenModel>() {

    @Inject
    lateinit var presenter: FcmDebugPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator() = FcmDebugScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_fcm_debug

    override fun onCreate(
            savedInstanceState: Bundle?,
            persistentState: PersistableBundle?,
            viewRecreated: Boolean
    ) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    override fun renderInternal(sm: FcmDebugScreenModel) {
        val hasFcmToken = sm.hasFcmToken()
        debug_fcm_tv.text = sm.fcmToken
        debug_fcm_tv.goneIf(!hasFcmToken)
        debug_container.goneIf(hasFcmToken)
    }

    override fun getScreenName(): String = "debug_fcm"

    private fun initListeners() {
        debug_fcm_tv.setOnClickListener { presenter.copyFcmToken() }
        debug_reload_btn.setOnClickListener { presenter.loadFcmToken() }
    }

    fun copyFcmToken() = debug_fcm_tv.copyTextToClipboard()

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

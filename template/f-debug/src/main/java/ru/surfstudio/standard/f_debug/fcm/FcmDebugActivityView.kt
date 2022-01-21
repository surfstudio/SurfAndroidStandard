package ru.surfstudio.standard.f_debug.fcm

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.core.ui.view_binding.viewBinding
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.ActivityFcmDebugBinding
import ru.surfstudio.android.utilktx.ktx.ui.view.copyTextToClipboard
import ru.surfstudio.android.utilktx.ktx.ui.view.goneIf
import ru.surfstudio.standard.f_debug.injector.ui.screen.configurator.activity.FcmDebugScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана показа fcm-токена
 */
class FcmDebugActivityView : BaseRenderableActivityView<FcmDebugScreenModel>() {

    private val binding by viewBinding(ActivityFcmDebugBinding::bind) { rootView }

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
        with(binding){
            debugFcmTv.text = sm.fcmToken
            debugFcmTv.goneIf(!hasFcmToken)
            debugContainer.goneIf(hasFcmToken)
        }
    }

    override fun getScreenName(): String = "debug_fcm"

    private fun initListeners() {
        binding.debugFcmTv.setOnClickListener { presenter.copyFcmToken() }
        binding.debugReloadBtn.setOnClickListener { presenter.loadFcmToken() }
    }

    fun copyFcmToken() = binding.debugFcmTv.copyTextToClipboard()

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

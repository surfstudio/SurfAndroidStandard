package ru.surfstudio.android.security.sample.ui.screen.pin

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_create_pin.*
import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.ui.base.configurator.CustomActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана ввода pin-кода
 */
class CreatePinActivityView : BaseRenderableActivityView<CreatePinScreenModel>() {

    @Inject
    lateinit var presenter: CreatePinPresenter

    override fun getPresenters(): Array<CorePresenter<*>> = arrayOf(presenter)

    override fun createConfigurator(): CustomActivityScreenConfigurator = CreatePinScreenConfigurator(intent)

    @LayoutRes
    override fun getContentView(): Int = R.layout.activity_create_pin

    override fun onCreate(savedInstanceState: Bundle?,
                          persistentState: PersistableBundle?,
                          viewRecreated: Boolean) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated)
        initListeners()
    }

    override fun renderInternal(screenModel: CreatePinScreenModel) {
        encrypt_pin_by_biometrics.isVisible = screenModel.isBiometricsAvailable
        encrypted_pin_et.setText(screenModel.encryptedPin)
    }

    override fun getScreenName(): String = "session"

    private fun initListeners() {
        enter_pin_btn.setOnClickListener { presenter.submitPin(getPin()) }
        get_api_key_btn.setOnClickListener { presenter.getApiKey(getPin()) }
        encrypt_pin_by_biometrics.setOnClickListener { presenter.encryptPin(getPin()) }
        decrypt_pin_btn.setOnClickListener { presenter.decryptPin(encrypted_pin_et.text.toString()) }
    }

    private fun getPin(): String = enter_pin_et.text.toString()

    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

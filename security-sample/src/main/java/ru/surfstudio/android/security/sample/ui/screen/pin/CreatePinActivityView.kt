package ru.surfstudio.android.security.sample.ui.screen.pin

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.activity_create_pin.*
import org.jetbrains.anko.toast
import ru.surfstudio.android.core.mvp.activity.BaseLdsActivityView
import ru.surfstudio.android.core.mvp.presenter.CorePresenter
import ru.surfstudio.android.sample.common.ui.base.loadstate.ErrorLoadState
import ru.surfstudio.android.sample.common.ui.base.loadstate.renderer.DefaultLoadStateRenderer
import ru.surfstudio.android.security.sample.R
import ru.surfstudio.android.security.sample.ui.base.configurator.CustomActivityScreenConfigurator
import javax.inject.Inject

/**
 * Вью экрана ввода pin-кода
 */
class CreatePinActivityView : BaseLdsActivityView<CreatePinScreenModel>() {

    private val renderer: DefaultLoadStateRenderer by lazy {
        DefaultLoadStateRenderer(placeholder)
                .apply {
                    //пример добавления представления кастомного стейта
//                    putPresentation(
//                            CustomLoadState::class,
//                            CustomLoadStatePresentation(placeholder))
//
//                    // установка листнеров на кнопки, при необходимости смена ресурсов
//                    configEmptyState(onBtnClickedListener = { toast(R.string.empty_state_toast_msg) })
//                    configErrorState(onBtnClickedListener = { toast(R.string.error_state_toast_msg) })
//
//                    //пример задания дополнительных действий при смене лоадстейта
//                    forState(ErrorLoadState::class,
//                            run = { colorToolbar(R.color.colorAccent) },
//                            elseRun = { colorToolbar(R.color.colorPrimary) })
                }
    }

    override fun getLoadStateRenderer() = renderer

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
        enter_pin_et.text.clear()
    }

    override fun getScreenName(): String = "session"

    private fun initListeners() {
        enter_pin_btn.setOnClickListener { presenter.submitPin(getPin()) }
        get_api_key_btn.setOnClickListener { presenter.getApiKey(getPin()) }
    }

    private fun getPin(): String = enter_pin_et.text.toString()

    fun showMessage(message: String) {
        toast(message)
    }
}

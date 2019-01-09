package ru.surfstudio.core_mvp_rxbinding_sample

import com.jakewharton.rxbinding2.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.core_mvp_rxbinding.base.ui.lds.BaseLdsRxActivityView
import javax.inject.Inject

class MainActivityView : BaseLdsRxActivityView<MainModel>() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun bind(sm: MainModel) {
        super.bind(sm)

        sm.counterState.observable.map { it.toString() }.bindTo { main_counter_tv.text = it }
        main_inc_btn.clicks().bindTo(sm.incAction)
        main_dec_btn.clicks().bindTo(sm.decAction)

        sm.textEditState bindTo main_text_et
        main_double_text_btn.clicks().bindTo(sm.doubleTextAction)
    }

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun getPresenters() = arrayOf(presenter)

    override fun getLoadStateRenderer(): LoadStateRendererInterface = LoadStateRendererInterface { }
}

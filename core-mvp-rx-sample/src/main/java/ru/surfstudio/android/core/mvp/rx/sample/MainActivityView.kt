package ru.surfstudio.android.core.mvp.rx.sample

import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.rx.domain.Relation
import ru.surfstudio.android.core.mvp.rx.ui.lds.BaseLdsRxActivityView
import ru.surfstudio.sample.R
import javax.inject.Inject

class MainActivityView : BaseLdsRxActivityView<MainModel>() {

    @Inject
    lateinit var presenter: MainPresenter

    override fun bind(sm: MainModel) {
        super.bind(sm)

        sm.counterState.bindTo{ main_counter_tv.text = it.toString() }
        main_inc_btn.clicks().bindTo { sm.incAction.getConsumer().accept(Unit) }
        main_dec_btn.clicks().bindTo { sm.decAction.getConsumer().accept(Unit) }

        main_text_et.textChanges().map { it.toString() } bindTo {sm.textEditState.getConsumer().accept(it)}
        sm.textEditState.bindTo { main_text_et.setText(it) }
        main_double_text_btn.clicks().bindTo { sm.doubleTextAction.getConsumer().accept(Unit) }

        sm.sampleCommand bindTo { text_tv.text = it }
    }

    override fun createConfigurator() = MainScreenConfigurator(intent)

    override fun getScreenName(): String = "MainActivityView"

    override fun getContentView(): Int = R.layout.activity_main

    override fun getPresenters() = arrayOf(presenter)

    override fun getLoadStateRenderer(): LoadStateRendererInterface = LoadStateRendererInterface { }
}

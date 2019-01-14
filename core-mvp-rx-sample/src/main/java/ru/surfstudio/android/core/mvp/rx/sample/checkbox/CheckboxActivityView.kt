package ru.surfstudio.android.core.mvp.rx.sample.checkbox

import com.jakewharton.rxbinding2.widget.checkedChanges
import kotlinx.android.synthetic.main.activity_checkboxes.*
import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.rx.ui.lds.BaseLdsRxActivityView
import ru.surfstudio.sample.R
import javax.inject.Inject

/**
 * Экран демострирующий возможность работы со связными данными типа чекбоксов
 */
class CheckboxActivityView : BaseLdsRxActivityView<CheckboxModel>() {

    @Inject
    lateinit var presenter: CheckboxPresenter

    override fun bind(sm: CheckboxModel) {
        super.bind(sm)

        checkbox_1.checkedChanges() bindTo sm.checkAction1
        checkbox_2.checkedChanges() bindTo sm.checkAction2
        checkbox_3.checkedChanges() bindTo sm.checkAction3

        sm.count.getObservable() bindTo { counter_et.text = it.toString() }
    }

    override fun createConfigurator() = CheckboxScreenConfigurator(intent)

    override fun getScreenName(): String = "CheckboxActivityView"

    override fun getContentView(): Int = R.layout.activity_checkboxes

    override fun getPresenters() = arrayOf(presenter)

    override fun getLoadStateRenderer(): LoadStateRendererInterface = LoadStateRendererInterface { }
}

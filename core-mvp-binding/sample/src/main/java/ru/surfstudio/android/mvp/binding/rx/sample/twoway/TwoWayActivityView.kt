package ru.surfstudio.android.mvp.binding.rx.sample.twoway

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import com.jakewharton.rxbinding2.widget.textChanges
import kotlinx.android.synthetic.main.activity_two_way.*
import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxActivityView
import ru.surfstudio.android.core.mvp.binding.sample.R
import javax.inject.Inject

private const val RUR = "\u20BD"

/**
 * Вью для демонстрации двустороннего биндинга
 */
class TwoWayActivityView : BaseRxActivityView() {

    @Inject
    lateinit var bm: TwoWayBindModel

    override fun getScreenName() = "TwoWayActivityView"

    override fun createConfigurator() = TwoWayScreenConfigurator(intent)

    override fun getContentView(): Int = R.layout.activity_two_way

    override fun onCreate(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?,
        viewRecreated: Boolean
    ) {
        a()
        bind()
    }

    private fun bind() {
        input.textChanges().map { it.toString() } bindTo bm.moneyInputAction

        bm.moneyState bindTo input::setText
        bm.moneyState bindTo state_value::setText
    }

    private fun a() {
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(txt: Editable) {
                txt.apply {
                    if (endsWith(RUR).not()) {
                        append(RUR)
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
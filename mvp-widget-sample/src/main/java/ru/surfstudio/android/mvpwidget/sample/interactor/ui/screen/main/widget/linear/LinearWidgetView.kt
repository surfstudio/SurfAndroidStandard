package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.main.widget.linear

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import ru.surfstudio.android.mvp.widget.view.CoreLinearLayoutView
import ru.surfstudio.android.mvpwidget.sample.R
import javax.inject.Inject


/**
 * Базовый пример виджета на базе {@link ru.surfstudio.android.mvp.widget.view.CoreLinearLayoutView}
 * */
class LinearWidgetView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CoreLinearLayoutView(context, attrs, defStyleAttr) {

    @Inject lateinit var presenter: LinearViewPresenter

    init {
        View.inflate(context, R.layout.widget_view, this)
        this.findViewById<TextView>(R.id.widget_tv)?.text = "Hello $name"
    }

    override fun getName() = "Linear widget view"

    override fun createConfigurator() = LinearViewConfigurator()

    override fun getPresenters() = arrayOf(presenter)
}
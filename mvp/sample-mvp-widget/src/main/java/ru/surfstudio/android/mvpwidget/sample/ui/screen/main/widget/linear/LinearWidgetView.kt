package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.linear

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import ru.surfstudio.android.mvp.widget.view.CoreFrameLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreLinearLayoutView
import ru.surfstudio.android.mvpwidget.sample.R
import javax.inject.Inject

/**
 * Базовый пример виджета на базе {@link ru.surfstudio.android.mvp.widget.view.CoreLinearLayoutView}
 */
class LinearWidgetView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CoreLinearLayoutView(context, attrs, defStyleAttr) {

    @Inject
    lateinit var presenter: LinearViewPresenter

    init {
        View.inflate(context, R.layout.widget_view, this)
        @SuppressLint("SetTextI18n")
        this.findViewById<TextView>(R.id.widget_tv)?.text = "Hello $name"
        setBackgroundColor(Color.YELLOW)

    }

    override fun getWidgetId(): String {
        val id = super.getWidgetId()
        val invalidId = CoreFrameLayoutView.NO_ID.toString()
        return if (id == invalidId) name.hashCode().toString() else id
    }

    override fun getName() = "Linear widget view"

    override fun createConfigurator() = LinearViewConfigurator()

    override fun getPresenters() = arrayOf(presenter)
}
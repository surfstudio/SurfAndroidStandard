package ru.surfstudio.standard.ui.screen.widget.relative

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import ru.surfstudio.android.mvp.widget.view.CoreRelativeLayoutView
import ru.surfstudio.standard.R
import javax.inject.Inject

/**
 * Базовый пример виджета на базе {@link ru.surfstudio.android.mvp.widget.view.CoreRelativeLayoutView}
 * */
class RelativeWidgetView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : CoreRelativeLayoutView(context, attrs, defStyleAttr) {

    @Inject lateinit var presenter: RelativeViewPresenter

    init {
        View.inflate(context, R.layout.widget_view, this)
        this.findViewById<TextView>(R.id.widget_tv)?.text = "Hello $name"
    }

    override fun getName() = "Relative widget view"

    override fun createConfigurator() = RelativeViewConfigurator()

    override fun getPresenters() = arrayOf(presenter)
}
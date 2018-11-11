package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.frame

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import ru.surfstudio.android.mvp.widget.view.CoreFrameLayoutView
import ru.surfstudio.android.mvpwidget.sample.R
import javax.inject.Inject

/**
 * Базовый пример виджета на базе {@link ru.surfstudio.android.mvp.widget.view.CoreFrameLayoutView}
 * */
class FrameWidgetView @JvmOverloads constructor(context: Context,
                                                attrs: AttributeSet? = null,
                                                defStyleAttr: Int = 0
) : CoreFrameLayoutView(context, attrs, defStyleAttr) {

    @Inject lateinit var presenter: FrameViewPresenter

    init {
        View.inflate(context, R.layout.widget_view, this)
        this.findViewById<TextView>(R.id.widget_tv)?.text = "Hello $name"
    }

    override fun getName() = "Frame widget view"

    override fun createConfigurator() = FrameViewConfigurator()

    override fun getPresenters() = arrayOf(presenter)
}
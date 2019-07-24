package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.widget.relative

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import ru.surfstudio.android.mvp.widget.view.CoreFrameLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreRelativeLayoutView
import ru.surfstudio.android.mvpwidget.sample.R
import javax.inject.Inject

/**
 * Базовый пример виджета на базе {@link ru.surfstudio.android.mvp.widget.view.CoreRelativeLayoutView}
 */
class RelativeWidgetView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : CoreRelativeLayoutView(context, attrs, defStyleAttr) {

    @Inject
    lateinit var presenter: RelativeViewPresenter

    init {
        View.inflate(context, R.layout.widget_view, this)
        @SuppressLint("SetTextI18n")
        this.findViewById<TextView>(R.id.widget_tv)?.text = "Hello $name"
        setBackgroundColor(Color.LTGRAY)

    }

    override fun getWidgetId(): String {
        val id = super.getWidgetId()
        val invalidId = CoreFrameLayoutView.NO_ID.toString()
        return if (id == invalidId) name.hashCode().toString() else id
    }

    override fun getName() = "Relative widget view"

    override fun createConfigurator() = RelativeViewConfigurator()

    override fun getPresenters() = arrayOf(presenter)

    override fun onAttachedToWindow() {
        Log.d("Widget", "onAttached")
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        Log.d("Widget", "onDetachedFromWindow")
        super.onDetachedFromWindow()
    }
}
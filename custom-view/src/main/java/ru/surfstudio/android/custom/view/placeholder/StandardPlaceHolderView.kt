package ru.surfstudio.android.custom.view.placeholder

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderView
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.logger.Logger

class StandardPlaceHolderView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet,
                                                        defStyle: Int = R.attr.placeHolderStyle)
    : FrameLayout(context, attrs, defStyle),
        PlaceHolderView {

    private var opaqueBackgroundColor = Color.TRANSPARENT
    private var transparentBackgroundColor = Color.TRANSPARENT

    init {
        View.inflate(context, R.layout.placeholder_view_layout, this)

        applyAttributes(context, attrs, defStyle)
    }

    override fun render(loadState: LoadState?) {

    }

    private fun applyAttributes(context: Context, attrs: AttributeSet, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderView, defStyle, R.style.PlaceHolderView_Default)
        this.opaqueBackgroundColor = ta.getColor(R.styleable.PlaceHolderView_opaqueBackgroundColor, Color.TRANSPARENT)
        this.transparentBackgroundColor = ta.getColor(R.styleable.PlaceHolderView_transparentBackgroundColor, Color.TRANSPARENT)
        ta.recycle()
    }
}
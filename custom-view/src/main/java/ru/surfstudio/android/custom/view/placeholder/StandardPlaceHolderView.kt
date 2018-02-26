package ru.surfstudio.android.custom.view.placeholder

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderView
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.custom.view.R.attr.*
import ru.surfstudio.android.logger.Logger

const val NOT_ASSIGNED = -1 //заглушка для незаданного атрибута

class StandardPlaceHolderView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet,
                                                        defStyle: Int = R.attr.placeHolderStyle)
    : FrameLayout(context, attrs, defStyle),
        PlaceHolderView {

    private var progressBar: MaterialProgressBar

    private var loadState = LoadState.NONE          //текущее состояние плейсхолдера
        set(value) {
            field = value
            updateView()
        }

    private var opaqueBackgroundColor: Int = 0          //непрозрачный цвет фоновой заливки
    private var transparentBackgroundColor: Int = 0     //цвет полупрозрачной маски
    private var progressBarColor: Int = 0               //цвет индикатора ProgressBar

    init {
        View.inflate(context, R.layout.placeholder_view_layout, this)
        progressBar = findViewById(R.id.placeholder_loading_pb)
        applyAttributes(context, attrs, defStyle)
    }

    override fun render(loadState: LoadState) {
        this.loadState = loadState
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderView, defStyle, R.style.PlaceHolderView_Default)
        this.opaqueBackgroundColor = ta.getResourceId(R.styleable.PlaceHolderView_opaqueBackgroundColor, NOT_ASSIGNED)
        this.transparentBackgroundColor = ta.getResourceId(R.styleable.PlaceHolderView_transparentBackgroundColor, NOT_ASSIGNED)
        this.progressBarColor = ta.getResourceId(R.styleable.PlaceHolderView_progressBarColor, NOT_ASSIGNED)

        Logger.d("1111 opaqueBackgroundColor = $opaqueBackgroundColor")
        Logger.d("1111 transparentBackgroundColor = $transparentBackgroundColor")
        Logger.d("1111 progressBarColor = $progressBarColor")

        setBackgroundColor()
        setProgressBarColor()

        ta.recycle()
    }

    private fun updateView() {
        setBackgroundColor()
        when (loadState) {
            LoadState.NONE -> {

            }
            LoadState.EMPTY -> {

            }
            LoadState.NOT_FOUND -> {

            }
            LoadState.ERROR -> {

            }
            LoadState.MAIN_LOADING -> {

            }
            LoadState.TRANSPARENT_LOADING -> {

            }
        }
    }

    /**
     * Окрашивание индикатора [MaterialProgressBar].
     *
     * Если цвет не задан явно в стилях - используется ?colorAccent приложения.
     */
    private fun setProgressBarColor() {
        if (progressBarColor != NOT_ASSIGNED) {
            progressBar.indeterminateTintList = ContextCompat.getColorStateList(context, progressBarColor)
        }
    }

    private fun setBackgroundColor() {
        when (loadState) {
            LoadState.TRANSPARENT_LOADING -> {
                setBackgroundResource(transparentBackgroundColor)
            }
            else -> {
                setBackgroundResource(opaqueBackgroundColor)
            }
        }
    }
}
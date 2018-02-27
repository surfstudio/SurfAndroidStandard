package ru.surfstudio.android.custom.view.placeholder

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderView
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.ui.textview.setTextOrGone

const val NOT_ASSIGNED = -1 //заглушка для незаданного атрибута

class StandardPlaceHolderView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet,
                                                        defStyle: Int = R.attr.placeHolderStyle)
    : FrameLayout(context, attrs, defStyle),
        PlaceHolderView {

    private var progressBar: MaterialProgressBar
    private var titleTv: TextView
    private var subtitleTv: TextView

    private val styler = PlaceholderStyler()                   //менеджер стилизации плейсхолдера
    private val dataContainer = PlaceholderDataContainer()     //менеджер данных плейсхолдера
    private val stater = PlaceholderStater { updateView() }    //менеджер состояния плейсхолдера

    init {
        View.inflate(context, R.layout.placeholder_view_layout, this)
        progressBar = findViewById(R.id.placeholder_loading_pb)
        titleTv = findViewById(R.id.placeholder_title_tv)
        subtitleTv = findViewById(R.id.placeholder_subtitle_tv)
        applyAttributes(context, attrs, defStyle)
    }

    override fun render(loadState: LoadState) {
        this.stater.loadState = loadState
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderView, defStyle, R.style.PlaceHolderView)
        this.styler.opaqueBackgroundColor = ta.getResourceId(R.styleable.PlaceHolderView_opaqueBackgroundColor, NOT_ASSIGNED)
        this.styler.transparentBackgroundColor = ta.getResourceId(R.styleable.PlaceHolderView_transparentBackgroundColor, NOT_ASSIGNED)
        this.styler.progressBarColor = ta.getResourceId(R.styleable.PlaceHolderView_progressBarColor, NOT_ASSIGNED)

        this.dataContainer.title = ta.getString(R.styleable.PlaceHolderView_title)
        this.dataContainer.subtitle = ta.getString(R.styleable.PlaceHolderView_subtitle)

        updateView()

        ta.recycle()
    }

    private fun updateView() {
        setBackgroundColor()
        setProgressBarColor()
        setVisibility()
        setData()
    }

    /**
     * Окрашивание индикатора [MaterialProgressBar].
     *
     * Если цвет не задан явно в стилях - используется ?colorAccent приложения.
     */
    private fun setProgressBarColor() {
        if (styler.progressBarColor != NOT_ASSIGNED) {
            progressBar.indeterminateTintList = ContextCompat.getColorStateList(context, styler.progressBarColor)
        }
    }

    /**
     * Установка цвета фона плейсхолдера в зависимости от текущего [LoadState].
     */
    private fun setBackgroundColor() {
        when (stater.loadState) {
            LoadState.TRANSPARENT_LOADING -> {
                setBackgroundResource(styler.transparentBackgroundColor)
            }
            else -> {
                setBackgroundResource(styler.opaqueBackgroundColor)
            }
        }
    }

    /**
     * Инициализация плейсхолдера данными
     */
    private fun setData() {
        titleTv.setTextOrGone(dataContainer.title)
        subtitleTv.setTextOrGone(dataContainer.subtitle)
    }

    /**
     * Установка видимости плейсхолдера в зависимости от текущего [LoadState].
     */
    private fun setVisibility() {
        visibility = when (stater.loadState) {
            LoadState.NONE -> {
                View.INVISIBLE
            }
            else -> {
                View.VISIBLE
            }
        }
    }

    /**
     * Хранилище всех настроек визуального стиля [StandardPlaceHolderView].
     */
    data class PlaceholderStyler(var opaqueBackgroundColor: Int = NOT_ASSIGNED,
                                 var transparentBackgroundColor: Int = NOT_ASSIGNED,
                                 var progressBarColor: Int = NOT_ASSIGNED)

    /**
     * Хранилище всех данных [StandardPlaceHolderView].
     */
    data class PlaceholderDataContainer(var title: String = "",
                                        var subtitle: String = "")

    /**
     * Менеджер текущего состояния [StandardPlaceHolderView] и переключения между состояниями.
     */
    class PlaceholderStater(private var onStateChanged: ((loadState: LoadState) -> (Unit))) {
        var loadState = LoadState.NONE          //текущее состояние плейсхолдера
            set(value) {
                field = value
                onStateChanged.invoke(field)
            }

        var loadStateSubject: PublishSubject<LoadState> = PublishSubject.create() //шина изменений loadState
    }
}
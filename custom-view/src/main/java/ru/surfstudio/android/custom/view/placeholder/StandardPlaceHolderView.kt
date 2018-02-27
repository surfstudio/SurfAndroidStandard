package ru.surfstudio.android.custom.view.placeholder

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderView
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.utilktx.ktx.attr.obtainDrawableAttribute
import ru.surfstudio.android.utilktx.ktx.ui.view.setImageDrawableOrGone
import ru.surfstudio.android.utilktx.ktx.ui.view.setTextOrGone

const val NOT_ASSIGNED = -1 //заглушка для незаданного атрибута

class StandardPlaceHolderView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet,
                                                        defStyle: Int = R.attr.placeHolderStyle)
    : FrameLayout(context, attrs, defStyle),
        PlaceHolderView {

    private var contentContainer: ViewGroup
    private var progressBar: MaterialProgressBar
    private var titleTv: TextView
    private var subtitleTv: TextView
    private var button: Button
    private var secondButton: Button
    private var imageIv: ImageView

    private val styler = PlaceholderStyler()                   //менеджер стилизации плейсхолдера
    private val dataContainer = PlaceholderDataContainer()     //менеджер данных плейсхолдера
    private val stater = PlaceholderStater { updateView() }    //менеджер состояния плейсхолдера

    init {
        View.inflate(context, R.layout.placeholder_view_layout, this)

        contentContainer = findViewById(R.id.placeholder_content_container)
        progressBar = findViewById(R.id.placeholder_loading_pb)
        titleTv = findViewById(R.id.placeholder_title_tv)
        subtitleTv = findViewById(R.id.placeholder_subtitle_tv)
        button = findViewById(R.id.placeholder_first_btn)
        secondButton = findViewById(R.id.placeholder_second_btn)
        imageIv = findViewById(R.id.placeholder_image_iv)

        applyAttributes(context, attrs, defStyle)

        updateView()
    }

    override fun render(loadState: LoadState) {
        this.stater.loadState = loadState
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderView, defStyle, R.style.PlaceHolderView)

        this.styler.opaqueBackgroundColor =
                ta.getResourceId(R.styleable.PlaceHolderView_opaqueBackgroundColor, NOT_ASSIGNED)
        this.styler.transparentBackgroundColor =
                ta.getResourceId(R.styleable.PlaceHolderView_transparentBackgroundColor, NOT_ASSIGNED)
        this.styler.progressBarColor =
                ta.getResourceId(R.styleable.PlaceHolderView_progressBarColor, NOT_ASSIGNED)

        val title = ta.getString(R.styleable.PlaceHolderView_title) ?: ""
        val subtitle = ta.getString(R.styleable.PlaceHolderView_subtitle) ?: ""
        val buttonText = ta.getString(R.styleable.PlaceHolderView_buttonText) ?: ""
        val secondButtonText = ta.getString(R.styleable.PlaceHolderView_secondButtonText) ?: ""
        val image = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_image)
        this.dataContainer.defaultViewData =
                PlaceholderDataContainer.ViewData(title, subtitle, buttonText, secondButtonText, image)

        val emptyTitle = ta.getString(R.styleable.PlaceHolderView_emptyTitle) ?: ""
        val emptySubtitle = ta.getString(R.styleable.PlaceHolderView_emptySubtitle) ?: ""
        val emptyButtonText = ta.getString(R.styleable.PlaceHolderView_emptyButtonText) ?: ""
        val emptySecondButtonText = ta.getString(R.styleable.PlaceHolderView_emptySecondButtonText)
                ?: ""
        val emptyImage = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_emptyImage)
        this.dataContainer.viewDataMap[LoadState.EMPTY] =
                PlaceholderDataContainer.ViewData(emptyTitle,
                        emptySubtitle,
                        emptyButtonText,
                        emptySecondButtonText,
                        emptyImage)

        val notFoundTitle = ta.getString(R.styleable.PlaceHolderView_notFoundTitle) ?: ""
        val notFoundSubtitle = ta.getString(R.styleable.PlaceHolderView_notFoundSubtitle) ?: ""
        val notFoundButtonText = ta.getString(R.styleable.PlaceHolderView_notFoundButtonText) ?: ""
        val notFoundSecondButtonText = ta.getString(R.styleable.PlaceHolderView_notFoundSecondButtonText)
                ?: ""
        val notFoundImage = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_notFoundImage)
        this.dataContainer.viewDataMap[LoadState.NOT_FOUND] =
                PlaceholderDataContainer.ViewData(notFoundTitle,
                        notFoundSubtitle,
                        notFoundButtonText,
                        notFoundSecondButtonText,
                        notFoundImage)

        val errorFoundTitle = ta.getString(R.styleable.PlaceHolderView_errorTitle) ?: ""
        val errorFoundSubtitle = ta.getString(R.styleable.PlaceHolderView_errorSubtitle) ?: ""
        val errorFoundButtonText = ta.getString(R.styleable.PlaceHolderView_errorButtonText) ?: ""
        val errorFoundSecondButtonText = ta.getString(R.styleable.PlaceHolderView_errorSecondButtonText)
                ?: ""
        val errorFoundImage = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_errorImage)
        this.dataContainer.viewDataMap[LoadState.ERROR] =
                PlaceholderDataContainer.ViewData(errorFoundTitle,
                        errorFoundSubtitle,
                        errorFoundButtonText,
                        errorFoundSecondButtonText,
                        errorFoundImage)

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
     * Инициализация плейсхолдера данными.
     */
    private fun setData() {
        val viewData = dataContainer.getViewData(stater.loadState)

        titleTv.setTextOrGone(viewData.title)
        subtitleTv.setTextOrGone(viewData.subtitle)
        button.setTextOrGone(viewData.buttonText)
        secondButton.setTextOrGone(viewData.secondButtonText)
        imageIv.setImageDrawableOrGone(viewData.image)
    }

    /**
     * Установка видимости плейсхолдера в зависимости от текущего [LoadState].
     */
    private fun setVisibility() {
        visibility = when (stater.loadState) {
            LoadState.NONE -> {
                View.INVISIBLE
            }
            LoadState.MAIN_LOADING, LoadState.TRANSPARENT_LOADING -> {
                contentContainer.visibility = View.INVISIBLE
                View.VISIBLE
            }
            else -> {
                contentContainer.visibility = View.VISIBLE
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
    data class PlaceholderDataContainer(var viewDataMap: ArrayMap<LoadState, ViewData> = ArrayMap(),
                                        var defaultViewData: ViewData = ViewData()) {

        data class ViewData(var title: String = "",
                            var subtitle: String = "",
                            var buttonText: String = "",
                            var secondButtonText: String = "",
                            var image: Drawable? = null)

        /**
         * Получение актуальных данных для отображения.
         *
         * @param loadState текущее состояние загрузки
         */
        fun getViewData(loadState: LoadState): ViewData = viewDataMap[loadState] ?: defaultViewData
    }

    /**
     * Менеджер текущего состояния [StandardPlaceHolderView] и переключения между состояниями.
     *
     * @param onStateChangedLambda лямбда, срабатывающая при изменении состояния [StandardPlaceHolderView].
     */
    class PlaceholderStater(private var onStateChangedLambda: ((loadState: LoadState) -> (Unit))) {
        var loadState = LoadState.NONE          //текущее состояние плейсхолдера
            set(value) {
                field = value
                onStateChangedLambda.invoke(field)
            }

        var loadStateSubject: PublishSubject<LoadState> = PublishSubject.create() //шина изменений loadState
    }
}
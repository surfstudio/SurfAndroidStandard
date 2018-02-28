package ru.surfstudio.android.custom.view.placeholder

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.reactivex.subjects.PublishSubject
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderView
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.utilktx.ktx.attr.obtainDrawableAttribute
import ru.surfstudio.android.utilktx.ktx.attr.obtainStringAttribute
import ru.surfstudio.android.utilktx.ktx.ui.view.setImageDrawableOrGone
import ru.surfstudio.android.utilktx.ktx.ui.view.setTextOrGone


const val NOT_ASSIGNED = -1         //заглушка для незаданного атрибута
const val NOT_ASSIGNED_DIMEN = 0    //заглушка для незаданного dimen-атрибута

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
        this.styler.titleBottomMargin =
                ta.getDimensionPixelOffset(R.styleable.PlaceHolderView_titleBottomMargin, NOT_ASSIGNED_DIMEN)

        val title = ta.obtainStringAttribute(R.styleable.PlaceHolderView_title)
        val subtitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_subtitle)
        val buttonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_buttonText)
        val secondButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_secondButtonText)
        val image = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_image)
        this.dataContainer.defaultViewData =
                PlaceholderDataContainer.ViewData(title, subtitle, buttonText, secondButtonText, image)

        val emptyTitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptyTitle)
        val emptySubtitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptySubtitle)
        val emptyButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptyButtonText)
        val emptySecondButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptySecondButtonText)
        val emptyImage = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_emptyImage)
        this.dataContainer.putViewData(
                LoadState.EMPTY,
                PlaceholderDataContainer.ViewData(
                        emptyTitle,
                        emptySubtitle,
                        emptyButtonText,
                        emptySecondButtonText,
                        emptyImage))

        val notFoundTitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_notFoundTitle)
        val notFoundSubtitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_notFoundSubtitle)
        val notFoundButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_notFoundButtonText)
        val notFoundSecondButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_notFoundSecondButtonText)
        val notFoundImage = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_notFoundImage)
        this.dataContainer.putViewData(
                LoadState.NOT_FOUND,
                PlaceholderDataContainer.ViewData(
                        notFoundTitle,
                        notFoundSubtitle,
                        notFoundButtonText,
                        notFoundSecondButtonText,
                        notFoundImage))

        val errorFoundTitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_errorTitle)
        val errorFoundSubtitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_errorSubtitle)
        val errorFoundButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_errorButtonText)
        val errorFoundSecondButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_errorSecondButtonText)
        val errorFoundImage = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_errorImage)
        this.dataContainer.putViewData(
                LoadState.ERROR,
                PlaceholderDataContainer.ViewData(
                        errorFoundTitle,
                        errorFoundSubtitle,
                        errorFoundButtonText,
                        errorFoundSecondButtonText,
                        errorFoundImage))
        ta.recycle()
    }

    private fun updateView() {
        setBackgroundColor()
        setProgressBarColor()
        setBottomMargins()
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

    private fun setBottomMargins() {
        setBottomMargin(titleTv, styler.titleBottomMargin)
    }

    private fun setBottomMargin(view: View, bottomMargin: Int) {
        val params = view.layoutParams as MarginLayoutParams
        params.bottomMargin = bottomMargin
        view.layoutParams = params
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
                progressBar.visibility = View.VISIBLE
                View.VISIBLE
            }
            else -> {
                contentContainer.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
                View.VISIBLE
            }
        }
    }

    /**
     * Хранилище всех настроек визуального стиля [StandardPlaceHolderView].
     */
    data class PlaceholderStyler(var opaqueBackgroundColor: Int = NOT_ASSIGNED,
                                 var transparentBackgroundColor: Int = NOT_ASSIGNED,
                                 var progressBarColor: Int = NOT_ASSIGNED,
                                 var titleBottomMargin: Int = 0,
                                 var subtitleBottomMargin: Int = 0,
                                 var buttonBottomMargin: Int = 0,
                                 var secondButtonBottomMargin: Int = 0,
                                 var imageBottomMargin: Int = 0)

    /**
     * Хранилище всех данных [StandardPlaceHolderView].
     */
    data class PlaceholderDataContainer(var defaultViewData: ViewData = ViewData(),
                                        private var viewDataMap: ArrayMap<LoadState, ViewData> = ArrayMap()) {

        data class ViewData(var title: String = "",
                            var subtitle: String = "",
                            var buttonText: String = "",
                            var secondButtonText: String = "",
                            var image: Drawable? = null) {

            fun isEmpty() =
                    title.isBlank() && subtitle.isBlank() && buttonText.isBlank() &&
                            secondButtonText.isBlank() && image == null
        }

        fun putViewData(loadState: LoadState, viewData: ViewData) {
            if (!viewData.isEmpty()) {
                viewDataMap[loadState] = viewData
            }
        }

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
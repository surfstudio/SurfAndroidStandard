package ru.surfstudio.android.custom.view.placeholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.util.ArrayMap
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.wang.avi.AVLoadingIndicatorView
import com.wang.avi.indicators.BallPulseIndicator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.placeholder_view_layout.view.*
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.animations.anim.AnimationUtil.fadeIn
import ru.surfstudio.android.animations.anim.AnimationUtil.fadeOut
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.ktx.attr.*
import ru.surfstudio.android.utilktx.ktx.ui.view.*
import java.text.Format
import java.util.concurrent.TimeUnit

/**
 * Стандартный полноэкранный плейсхолдер с поддержкой смены состояний.
 */
open class StandardPlaceHolderView @JvmOverloads constructor(context: Context,
                                                             attrs: AttributeSet? = null,
                                                             defStyle: Int = R.attr.standardPlaceHolderStyle)
    : FrameLayout(context, attrs, defStyle) {

    @Suppress("MemberVisibilityCanBePrivate")
    var buttonLambda: ((loadState: PlaceholderStater.LoadState) -> Unit)? = null    //обработчик нажатия на первую кнопку
        set(value) {
            field = value
            button.setOnClickListener { buttonLambda?.invoke(stater.loadState) }
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var secondButtonLambda: ((loadState: PlaceholderStater.LoadState) -> Unit)? = null  //обработчик нажатия на вторую кнопку
        set(value) {
            field = value
            secondButton.setOnClickListener { secondButtonLambda?.invoke(stater.loadState) }
        }

    private var contentContainer: ViewGroup
    private var progressBarContainer: FrameLayout
    private var progressBar: MaterialProgressBar
    private var progressBarAV: AVLoadingIndicatorView
    private var titleTv: TextView
    private var subtitleTv: TextView
    private var button: Button
    private var secondButton: Button
    private var imageIv: ImageView

    private val styler = PlaceholderStyler()                   //менеджер стилизации плейсхолдера
    private val dataContainer = PlaceholderDataContainer()     //менеджер данных плейсхолдера
    private val stater = PlaceholderStater { updateView() }    //менеджер состояния плейсхолдера

    init {
        @Suppress("LeakingThis")
        View.inflate(context, R.layout.placeholder_view_layout, this)

        contentContainer = findViewById(R.id.placeholder_content_container)
        progressBarContainer = findViewById(R.id.progress_bar_container)
        progressBar = findViewById(R.id.placeholder_loading_pb)
        progressBarAV = findViewById(R.id.placeholder_loading_av_indicator)
        titleTv = findViewById(R.id.placeholder_title_tv)
        subtitleTv = findViewById(R.id.placeholder_subtitle_tv)
        button = findViewById(R.id.placeholder_first_btn)
        secondButton = findViewById(R.id.placeholder_second_btn)
        imageIv = findViewById(R.id.placeholder_image_iv)

        applyAttributes(context, attrs, defStyle)

        initialSetUp()
    }

    /**
     * Состояние окончания асинхронного процесса.
     *
     * При установке этого состояния плейсхолдер скрывается.
     */
    fun setNoneState() {
        this.stater.loadState = PlaceholderStater.LoadState.NONE
    }

    /**
     * Состояние запуска асинхронного процесса.
     *
     * При установке этого состояния плейсхолдер блокирует UI и полностью скрывает его.
     */
    fun setMainLoadingState() {
        this.stater.loadState = PlaceholderStater.LoadState.MAIN_LOADING
    }

    /**
     * Состояние запуска асинхронного процесса.
     *
     * При установке этого состояния плейсхолдер блокирует UI, но остаётся полупрозрачным,
     * не скрывая контент.
     */
    fun setTransparentLoadingState() {
        this.stater.loadState = PlaceholderStater.LoadState.TRANSPARENT_LOADING
    }

    /**
     * Состояние ошибки в процессе работы асинхронного процесса.
     *
     * При установке этого состояния плейсхолдер отображается в конфигурации для отображения ошибки.
     */
    fun setErrorState() {
        this.stater.loadState = PlaceholderStater.LoadState.ERROR
    }

    /**
     * Состояние пустого результата.
     *
     * При установке этого состояния плейсхолдер отображается в конфигурации empty-state.
     */
    fun setEmptyState() {
        this.stater.loadState = PlaceholderStater.LoadState.EMPTY
    }

    /**
     * Состояние пустого результата фильтрации.
     *
     * При установке этого состояния плейсхолдер отображается в конфигурации empty-state
     * для фильтрации.
     */
    fun setNotFoundState() {
        this.stater.loadState = PlaceholderStater.LoadState.NOT_FOUND
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet? = null, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StandardPlaceHolderView, defStyle, R.style.StandardPlaceHolderView)
        this.styler.opaqueBackgroundColor = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvOpaqueBackgroundColor)
        this.styler.transparentBackgroundColor = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvTransparentBackgroundColor)
        this.styler.progressBarColor = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvProgressBarColor)
        this.styler.progressBarType = ProgressIndicatorType.byId(ta.getInt(R.styleable.StandardPlaceHolderView_pvProgressBarType, 0))
        this.styler.titleBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvTitleBottomMargin)
        this.styler.subtitleBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSubtitleBottomMargin)
        this.styler.buttonBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvButtonBottomMargin)
        this.styler.secondButtonBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonBottomMargin)
        this.styler.imageBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvImageBottomMargin)
        this.styler.titleTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvTitleTopMargin)
        this.styler.subtitleTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSubtitleTopMargin)
        this.styler.buttonTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvButtonTopMargin)
        this.styler.secondButtonTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonTopMargin)
        this.styler.imageTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvImageTopMargin)
        this.styler.titleTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvTitleTextAppearance)
        this.styler.subtitleTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvSubtitleTextAppearance)
        this.styler.buttonTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvButtonTextAppearance)
        this.styler.secondButtonTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonTextAppearance)
        this.styler.buttonStyleResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvButtonStyle)
        this.styler.secondButtonStyleResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonStyle)
        this.styler.imageStyleResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvImageStyle)

        val title = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvTitle)
        val subtitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvSubtitle)
        val buttonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvButtonText)
        val secondButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonText)
        val image = ta.obtainDrawableAttribute(context, R.styleable.StandardPlaceHolderView_pvImage)
        this.dataContainer.defaultViewData =
                PlaceholderDataContainer.ViewData(
                        title,
                        subtitle,
                        buttonText,
                        secondButtonText,
                        image)

        val emptyTitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvEmptyTitle)
        val emptySubtitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvEmptySubtitle)
        val emptyButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvEmptyButtonText)
        val emptySecondButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvEmptySecondButtonText)
        val emptyImage = ta.obtainDrawableAttribute(context, R.styleable.StandardPlaceHolderView_pvEmptyImage)
        this.dataContainer.putViewData(
                PlaceholderStater.LoadState.EMPTY,
                PlaceholderDataContainer.ViewData(
                        emptyTitle,
                        emptySubtitle,
                        emptyButtonText,
                        emptySecondButtonText,
                        emptyImage))

        val notFoundTitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNotFoundTitle)
        val notFoundSubtitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNotFoundSubtitle)
        val notFoundButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNotFoundButtonText)
        val notFoundSecondButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNotFoundSecondButtonText)
        val notFoundImage = ta.obtainDrawableAttribute(context, R.styleable.StandardPlaceHolderView_pvNotFoundImage)
        this.dataContainer.putViewData(
                PlaceholderStater.LoadState.NOT_FOUND,
                PlaceholderDataContainer.ViewData(
                        notFoundTitle,
                        notFoundSubtitle,
                        notFoundButtonText,
                        notFoundSecondButtonText,
                        notFoundImage))

        val errorFoundTitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorTitle)
        val errorFoundSubtitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorSubtitle)
        val errorFoundButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorButtonText)
        val errorFoundSecondButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorSecondButtonText)
        val errorFoundImage = ta.obtainDrawableAttribute(context, R.styleable.StandardPlaceHolderView_pvErrorImage)
        this.dataContainer.putViewData(
                PlaceholderStater.LoadState.ERROR,
                PlaceholderDataContainer.ViewData(
                        errorFoundTitle,
                        errorFoundSubtitle,
                        errorFoundButtonText,
                        errorFoundSecondButtonText,
                        errorFoundImage))

        ta.recycle()

        updateView()
    }

    private fun initialSetUp() {
        setStyles() //метод setStyles обязательно должен вызываться первым
        setProgressBarColor()
        setMargins()
        setListeners()
    }

    /**
     * Установка стилей для всех виджетов.
     *
     * Виджеты, к которым применяется стиль из атрибутов, пересоздаются - поэтому этот метод
     * нужно обязательно вызывать первым при конфигурировании [StandardPlaceHolderView]. В противном
     * случае, ранее применённые настройки к данным виджетам могут перезатереться.
     */
    private fun setStyles() {
        titleTv.setTextAppearanceStyle(styler.titleTextAppearanceResId)
        subtitleTv.setTextAppearanceStyle(styler.subtitleTextAppearanceResId)
        button.setTextAppearanceStyle(styler.buttonTextAppearanceResId)
        secondButton.setTextAppearanceStyle(styler.secondButtonTextAppearanceResId)

        styler.buttonStyleResId.also {
            if (it != NOT_ASSIGNED_RESOURCE) {
                val viewIndex = contentContainer.indexOfChild(button)
                contentContainer.removeViewInLayout(button)
                button = Button(ContextThemeWrapper(context, it), null, 0)
                button.layoutParams = extractLayoutParamsFromStyle(styler.buttonStyleResId)
                contentContainer.addView(button, viewIndex)
            }
        }
        styler.secondButtonStyleResId.also {
            if (it != NOT_ASSIGNED_RESOURCE) {
                val viewIndex = contentContainer.indexOfChild(secondButton)
                contentContainer.removeViewInLayout(secondButton)
                secondButton = Button(ContextThemeWrapper(context, it), null, 0)
                secondButton.layoutParams = extractLayoutParamsFromStyle(styler.secondButtonStyleResId)
                contentContainer.addView(secondButton, viewIndex)
            }
        }
        styler.imageStyleResId.also {
            if (it != NOT_ASSIGNED_RESOURCE) {
                val viewIndex = contentContainer.indexOfChild(imageIv)
                contentContainer.removeViewInLayout(imageIv)
                imageIv = ImageView(ContextThemeWrapper(context, it), null, 0)
                imageIv.layoutParams = extractLayoutParamsFromStyle(styler.imageStyleResId)
                contentContainer.addView(imageIv, viewIndex)
            }
        }
    }

    /**
     * Окрашивание индикатора [MaterialProgressBar].
     *
     * Если цвет не задан явно в стилях - используется ?colorAccent приложения.
     */
    private fun setProgressBarColor() {
        if (styler.progressBarColor != NOT_ASSIGNED_RESOURCE) {
            progressBar.indeterminateTintList = ContextCompat.getColorStateList(context, styler.progressBarColor)
        }
    }

    /**
     * Установка отступов между виджетами.
     */
    private fun setMargins() {
        titleTv.setBottomMargin(styler.titleBottomMargin)
        subtitleTv.setBottomMargin(styler.subtitleBottomMargin)
        button.setBottomMargin(styler.buttonBottomMargin)
        secondButton.setBottomMargin(styler.secondButtonBottomMargin)
        imageIv.setBottomMargin(styler.imageBottomMargin)

        titleTv.setTopMargin(styler.titleTopMargin)
        subtitleTv.setTopMargin(styler.subtitleTopMargin)
        button.setTopMargin(styler.buttonTopMargin)
        secondButton.setTopMargin(styler.secondButtonTopMargin)
        imageIv.setTopMargin(styler.imageTopMargin)
    }

    /**
     * Установка обработчиков нажатий на кнопки
     */
    private fun setListeners() {
        button.setOnClickListener {
            buttonLambda?.invoke(stater.loadState)
        }
        secondButton.setOnClickListener {
            secondButtonLambda?.invoke(stater.loadState)
        }
    }

    /**
     * Обновление состояния виджета
     */
    private fun updateView() {
        setBackgroundColor()
        setVisibility()
        setData()
        setProgressIndicator()
    }

    /**
     * Установка цвета фона плейсхолдера в зависимости от текущего LoadState.
     */
    private fun setBackgroundColor() {
        when (stater.loadState) {
            PlaceholderStater.LoadState.TRANSPARENT_LOADING -> {
                setBackgroundResource(styler.transparentBackgroundColor)
            }
            else -> {
                setBackgroundResource(styler.opaqueBackgroundColor)
            }
        }
    }

    /**
     * Установка видимости плейсхолдера в зависимости от текущего LoadState.
     */
    private fun setVisibility() {
        when (stater.loadState) {
            PlaceholderStater.LoadState.NONE -> {
                fadeOut(this, 300L)
            }
            PlaceholderStater.LoadState.MAIN_LOADING, PlaceholderStater.LoadState.TRANSPARENT_LOADING -> {
                contentContainer.visibility = View.INVISIBLE
                progressBarContainer.visibility = View.VISIBLE
                fadeIn(this, 300L)
            }
            else -> {
                contentContainer.visibility = View.VISIBLE
                progressBarContainer.visibility = View.INVISIBLE
                fadeIn(this, 300L)
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
     * Установка прогресс-индикатора.
     */
    private fun setProgressIndicator() {
        val progressBarType = styler.progressBarType

        Log.d("1111", "1111 progress bar type = $progressBarType")
        //progressBar.invisibleIf(progressBarType != ProgressIndicatorType.STANDARD_CIRCLE_INDICATOR)
        progressBar.visibility = View.INVISIBLE
        //progressBarAV.visibility = View.VISIBLE
        //progressBarAV.invisibleIf(progressBarType == ProgressIndicatorType.STANDARD_CIRCLE_INDICATOR)

        //if (progressBarType != ProgressIndicatorType.STANDARD_CIRCLE_INDICATOR) {
            Log.d("1111", "1111 progress bar name = ${progressBarType.title}")
        progressBarAV.visibility = View.VISIBLE
        progressBarAV.indicator = BallPulseIndicator()
            //progressBarAV.setIndicatorColor(R.color.progressbar_color)
            //progressBarAV.show()
        //}
    }

    /**
     * Извлечение [ViewGroup.LayoutParams] из стиля виджета.
     *
     * Только так можно применить параметры ширины и высоты виджета, заданные через стиль.
     */
    @SuppressLint("ResourceType")
    private fun extractLayoutParamsFromStyle(style: Int): ViewGroup.LayoutParams {
        val t = context.theme.obtainStyledAttributes(null,
                intArrayOf(android.R.attr.layout_width, android.R.attr.layout_height), style, style)
        try {
            val w = t.getLayoutDimension(0, ViewGroup.LayoutParams.WRAP_CONTENT)
            val h = t.getLayoutDimension(1, ViewGroup.LayoutParams.WRAP_CONTENT)
            return ViewGroup.LayoutParams(w, h)
        } finally {
            t.recycle()
        }
    }

    /**
     * Хранилище всех настроек визуального стиля [StandardPlaceHolderView].
     */
    data class PlaceholderStyler(@ColorRes var opaqueBackgroundColor: Int = NOT_ASSIGNED_RESOURCE,
                                 @ColorRes var transparentBackgroundColor: Int = NOT_ASSIGNED_RESOURCE,
                                 @ColorRes var progressBarColor: Int = NOT_ASSIGNED_RESOURCE,
                                 var progressBarType: ProgressIndicatorType = ProgressIndicatorType.STANDARD_CIRCLE_INDICATOR,
                                 var titleBottomMargin: Int = 0,
                                 var subtitleBottomMargin: Int = 0,
                                 var buttonBottomMargin: Int = 0,
                                 var secondButtonBottomMargin: Int = 0,
                                 var imageBottomMargin: Int = 0,
                                 var titleTopMargin: Int = 0,
                                 var subtitleTopMargin: Int = 0,
                                 var buttonTopMargin: Int = 0,
                                 var secondButtonTopMargin: Int = 0,
                                 var imageTopMargin: Int = 0,
                                 @StyleRes var titleTextAppearanceResId: Int = NOT_ASSIGNED_RESOURCE,
                                 @StyleRes var subtitleTextAppearanceResId: Int = NOT_ASSIGNED_RESOURCE,
                                 @StyleRes var buttonTextAppearanceResId: Int = NOT_ASSIGNED_RESOURCE,
                                 @StyleRes var secondButtonTextAppearanceResId: Int = NOT_ASSIGNED_RESOURCE,
                                 @StyleRes var buttonStyleResId: Int = NOT_ASSIGNED_RESOURCE,
                                 @StyleRes var secondButtonStyleResId: Int = NOT_ASSIGNED_RESOURCE,
                                 @StyleRes var imageStyleResId: Int = NOT_ASSIGNED_RESOURCE)

    /**
     * Хранилище всех данных [StandardPlaceHolderView].
     */
    data class PlaceholderDataContainer(var defaultViewData: ViewData = ViewData(),
                                        private var viewDataMap: ArrayMap<PlaceholderStater.LoadState, ViewData> = ArrayMap()) {

        data class ViewData(var title: String = "",
                            var subtitle: String = "",
                            var buttonText: String = "",
                            var secondButtonText: String = "",
                            var image: Drawable? = null) {

            fun isEmpty() =
                    title.isBlank() && subtitle.isBlank() && buttonText.isBlank() &&
                            secondButtonText.isBlank() && image == null
        }

        fun putViewData(loadState: PlaceholderStater.LoadState, viewData: ViewData) {
            if (!viewData.isEmpty()) {
                viewDataMap[loadState] = viewData
            }
        }

        /**
         * Получение актуальных данных для отображения.
         *
         * @param loadState текущее состояние загрузки
         */
        fun getViewData(loadState: PlaceholderStater.LoadState): ViewData =
                if (loadState == PlaceholderStater.LoadState.NONE) ViewData()
                else viewDataMap[loadState]
                        ?: defaultViewData
    }

    @Suppress("PrivatePropertyName")
    /**
     * Менеджер текущего состояния [StandardPlaceHolderView] и переключения между состояниями.
     *
     * @param onStateChangedLambda лямбда, срабатывающая при изменении состояния [StandardPlaceHolderView].
     */
    class PlaceholderStater(private var onStateChangedLambda: ((loadState: LoadState) -> (Unit))) {

        enum class LoadState {
            NONE, //контент загружен
            MAIN_LOADING, //прогресс, закрывающий весь контент
            TRANSPARENT_LOADING, //полупрозрачный прогресс, блокирует весь интерфейс
            ERROR, //ошибка загрузки данных
            EMPTY, //данных нет
            NOT_FOUND //нет данных по заданному фильтру
        }

        private val STATE_TOGGLE_DELAY_MS: Long = 250

        var loadState = LoadState.NONE          //текущее состояние плейсхолдера
            set(value) {
                field = value
                loadStateSubject.onNext(field)
            }

        private var loadStateSubject: PublishSubject<LoadState> = PublishSubject.create() //шина изменений loadState

        init {
            loadStateSubject
                    .debounce(STATE_TOGGLE_DELAY_MS, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        onStateChangedLambda.invoke(it)
                    }
        }
    }

    enum class ProgressIndicatorType(var id: Int, var title: String = "") {
        STANDARD_CIRCLE_INDICATOR(0),
        BALL_PULSE_INDICATOR(1, "BallPulseIndicator"),
        BALL_GRID_PULSE_INDICATOR(2, "BallGridPulseIndicator"),
        BALL_CLIP_ROTATE_INDICATOR(3, "BallClipRotateIndicator"),
        BALL_CLIP_ROTATE_PULSE_INDICATOR(4, "BallClipRotatePulseIndicator"),
        SQUARE_SPIN_INDICATOR(5, "SquareSpinIndicator"),
        BALL_CLIP_ROTATE_MULTIPLE_INDICATOR(6, "BallClipRotateMultipleIndicator"),
        BALL_PULSE_RISE_INDICATOR(7, "BallPulseRiseIndicator"),
        BALL_ROTATE_INDICATOR(8, "BallRotateIndicator"),
        CUBE_TRANSITION_INDICATOR(9, "CubeTransitionIndicator"),
        BALL_ZIG_ZAG_INDICATOR(10, "BallZigZagIndicator"),
        BALL_ZIG_ZAG_DEFLECT_INDICATOR(11, "BallZigZagDeflectIndicator"),
        BALL_TRIANGLE_PATH_INDICATOR(12, "BallTrianglePathIndicator"),
        BALL_SCALE_INDICATOR(13, "BallScaleIndicator"),
        LINE_SCALE_INDICATOR(14, "LineScaleIndicator"),
        LINE_SCALE_PARTY_INDICATOR(15, "LineScalePartyIndicator"),
        BALL_SCALE_MULTIPLE_INDICATOR(16, "BallScaleMultipleIndicator"),
        BALL_PULSE_SYNC_INDICATOR(17, "BallPulseSyncIndicator"),
        BALL_BEAT_INDICATOR(18, "BallBeatIndicator"),
        LINE_SCALE_PULSE_OUT_INDICATOR(19, "LineScalePulseOutIndicator"),
        LINE_SCALE_PULSE_OUT_RAPID_INDICATOR(20, "LineScalePulseOutRapidIndicator"),
        BALL_SCALE_RIPPLE_INDICATOR(21, "BallScaleRippleIndicator"),
        BALL_SCALE_RIPPLE_MULTIPLE_INDICATOR(22, "BallScaleRippleMultipleIndicator"),
        BALL_SPIN_FADE_LOADER_INDICATOR(23, "BallSpinFadeLoaderIndicator"),
        LINE_SPIN_FADE_LOADER_INDICATOR(24, "LineSpinFadeLoaderIndicator"),
        TRIANGLE_SKEW_SPIN_INDICATOR(25, "TriangleSkewSpinIndicator"),
        PACMAN_INDICATOR(26, "PacmanIndicator"),
        BALL_GRID_BEAT_INDICATOR(28, "BallGridBeatIndicator"),
        SEMI_CIRCLE_SPIN_INDICATOR(29, "SemiCircleSpinIndicator");

        companion object {

            internal fun byId(id: Int): ProgressIndicatorType {
                for (f in values()) {
                    if (f.id == id) return f
                }
                throw IllegalArgumentException()
            }
        }
    }
}
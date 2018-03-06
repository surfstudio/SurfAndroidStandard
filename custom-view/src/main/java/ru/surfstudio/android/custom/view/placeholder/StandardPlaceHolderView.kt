package ru.surfstudio.android.custom.view.placeholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.StyleRes
import android.support.v4.content.ContextCompat
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.animations.anim.AnimationUtil.fadeIn
import ru.surfstudio.android.animations.anim.AnimationUtil.fadeOut
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.utilktx.ktx.attr.*
import ru.surfstudio.android.utilktx.ktx.ui.view.*
import java.util.concurrent.TimeUnit

/**
 * Стандартный полноэкранный плейсхолдер с поддержкой смены состояний.
 */
open class StandardPlaceHolderView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet,
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

    private fun applyAttributes(context: Context, attrs: AttributeSet, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderView, defStyle, R.style.StandardPlaceHolderView)

        this.styler.opaqueBackgroundColor = ta.obtainResourceIdAttribute(R.styleable.PlaceHolderView_opaqueBackgroundColor)
        this.styler.transparentBackgroundColor = ta.obtainResourceIdAttribute(R.styleable.PlaceHolderView_transparentBackgroundColor)
        this.styler.progressBarColor = ta.obtainResourceIdAttribute(R.styleable.PlaceHolderView_progressBarColor)
        this.styler.titleBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_titleBottomMargin)
        this.styler.subtitleBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_subtitleBottomMargin)
        this.styler.buttonBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_buttonBottomMargin)
        this.styler.secondButtonBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_secondButtonBottomMargin)
        this.styler.imageBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_imageBottomMargin)
        this.styler.titleTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_titleTopMargin)
        this.styler.subtitleTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_subtitleTopMargin)
        this.styler.buttonTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_buttonTopMargin)
        this.styler.secondButtonTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_secondButtonTopMargin)
        this.styler.imageTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.PlaceHolderView_imageTopMargin)
        this.styler.titleTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.TextAttributes_titleTextAppearance)
        this.styler.subtitleTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.TextAttributes_subtitleTextAppearance)
        this.styler.buttonTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.TextAttributes_buttonTextAppearance)
        this.styler.secondButtonTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.TextAttributes_secondButtonTextAppearance)
        this.styler.buttonStyleResId = ta.obtainResourceIdAttribute(R.styleable.PlaceHolderView_buttonStyle)
        this.styler.secondButtonStyleResId = ta.obtainResourceIdAttribute(R.styleable.PlaceHolderView_secondButtonStyle)
        this.styler.imageStyleResId = ta.obtainResourceIdAttribute(R.styleable.PlaceHolderView_imageStyle)

        val title = ta.obtainStringAttribute(R.styleable.PlaceHolderView_title)
        val subtitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_subtitle)
        val buttonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_buttonText)
        val secondButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_secondButtonText)
        val image = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_image)
        this.dataContainer.defaultViewData =
                PlaceholderDataContainer.ViewData(
                        title,
                        subtitle,
                        buttonText,
                        secondButtonText,
                        image)

        val emptyTitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptyTitle)
        val emptySubtitle = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptySubtitle)
        val emptyButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptyButtonText)
        val emptySecondButtonText = ta.obtainStringAttribute(R.styleable.PlaceHolderView_emptySecondButtonText)
        val emptyImage = ta.obtainDrawableAttribute(context, R.styleable.PlaceHolderView_emptyImage)
        this.dataContainer.putViewData(
                PlaceholderStater.LoadState.EMPTY,
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
                PlaceholderStater.LoadState.NOT_FOUND,
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
    }

    /**
     * Установка цвета фона плейсхолдера в зависимости от текущего [LoadState].
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
     * Установка видимости плейсхолдера в зависимости от текущего [LoadState].
     */
    private fun setVisibility() {
        when (stater.loadState) {
            PlaceholderStater.LoadState.NONE -> {
                fadeOut(this, 300L)
            }
            PlaceholderStater.LoadState.MAIN_LOADING, PlaceholderStater.LoadState.TRANSPARENT_LOADING -> {
                contentContainer.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
                fadeIn(this, 300L)
            }
            else -> {
                contentContainer.visibility = View.VISIBLE
                progressBar.visibility = View.INVISIBLE
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
}
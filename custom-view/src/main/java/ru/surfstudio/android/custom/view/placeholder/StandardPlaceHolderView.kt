/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.custom.view.placeholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import android.util.AttributeSet
import android.util.SparseIntArray
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.collection.ArrayMap
import com.wang.avi.AVLoadingIndicatorView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.animations.anim.AnimationUtil.fadeIn
import ru.surfstudio.android.animations.anim.AnimationUtil.fadeOut
import ru.surfstudio.android.custom.view.R
import ru.surfstudio.android.utilktx.ktx.attr.*
import ru.surfstudio.android.utilktx.ktx.ui.view.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Полноэкранный плейсхолдер с поддержкой смены состояний.
 *
 * Поддержка приостановлена. Следует использовать [PlaceHolderViewContainer]
 */
open class StandardPlaceHolderView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = R.attr.standardPlaceHolderStyle
) : FrameLayout(context, attrs, defStyle) {

    @Suppress("MemberVisibilityCanBePrivate")
    var buttonLambda: ((loadState: PlaceholderStater.StandardLoadState) -> Unit)? = null    //обработчик нажатия на первую кнопку
        set(value) {
            field = value
            button.setOnClickListener { buttonLambda?.invoke(stater.loadState) }
        }

    @Suppress("MemberVisibilityCanBePrivate")
    var secondButtonLambda: ((loadState: PlaceholderStater.StandardLoadState) -> Unit)? = null  //обработчик нажатия на вторую кнопку
        set(value) {
            field = value
            secondButton.setOnClickListener { secondButtonLambda?.invoke(stater.loadState) }
        }

    private var contentContainer: ViewGroup
    private var progressBarContainer: FrameLayout
    private var progressBar: MaterialProgressBar
    private var avIndicatorView: AVLoadingIndicatorView? = null
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
        this.stater.loadState = PlaceholderStater.StandardLoadState.NONE
    }

    /**
     * Состояние запуска асинхронного процесса.
     *
     * При установке этого состояния плейсхолдер блокирует UI и полностью скрывает его.
     */
    fun setMainLoadingState() {
        this.stater.loadState = PlaceholderStater.StandardLoadState.MAIN_LOADING
    }

    /**
     * Состояние запуска асинхронного процесса.
     *
     * При установке этого состояния плейсхолдер блокирует UI, но остаётся полупрозрачным,
     * не скрывая контент.
     */
    fun setTransparentLoadingState() {
        this.stater.loadState = PlaceholderStater.StandardLoadState.TRANSPARENT_LOADING
    }

    /**
     * Состояние ошибки в процессе работы асинхронного процесса.
     *
     * При установке этого состояния плейсхолдер отображается в конфигурации для отображения ошибки.
     */
    fun setErrorState() {
        this.stater.loadState = PlaceholderStater.StandardLoadState.ERROR
    }

    /**
     * Состояние пустого результата.
     *
     * При установке этого состояния плейсхолдер отображается в конфигурации empty-state.
     */
    fun setEmptyState() {
        this.stater.loadState = PlaceholderStater.StandardLoadState.EMPTY
    }

    /**
     * Состояние пустого результата фильтрации.
     *
     * При установке этого состояния плейсхолдер отображается в конфигурации empty-state
     * для фильтрации.
     */
    fun setNotFoundState() {
        this.stater.loadState = PlaceholderStater.StandardLoadState.NOT_FOUND
    }

    /**
     * Состояние отсутствия интернет-соединения.
     *
     * При установке этого состояния плейсхолдер отображается в конфигурации для отображения ошибки интернет-соединения.
     */
    fun setNoInternetState() {
        this.stater.loadState = PlaceholderStater.StandardLoadState.NO_INTERNET
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet? = null, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.StandardPlaceHolderView, defStyle, R.style.StandardPlaceHolderView)

        styler.apply {
            opaqueBackgroundColor = ta.obtainColorAttribute(R.styleable.StandardPlaceHolderView_pvOpaqueBackgroundColor)
            opaqueBackground = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvOpaqueBackground)
            transparentBackgroundColor = ta.obtainColorAttribute(R.styleable.StandardPlaceHolderView_pvTransparentBackgroundColor)
            transparentBackground = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvTransparentBackground)
            progressBarColor = ta.obtainColorAttribute(R.styleable.StandardPlaceHolderView_pvProgressBarColor)
            progressBarWidth = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvProgressBarWidth, WRAP_CONTENT)
            progressBarHeight = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvProgressBarHeight, WRAP_CONTENT)
            progressBarType = ProgressIndicatorType.byId(ta.getInt(R.styleable.StandardPlaceHolderView_pvProgressBarType, 0))
            titleBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvTitleBottomMargin)
            subtitleBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSubtitleBottomMargin)
            buttonBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvButtonBottomMargin)
            secondButtonBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonBottomMargin)
            imageBottomMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvImageBottomMargin)
            titleTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvTitleTopMargin)
            subtitleTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSubtitleTopMargin)
            buttonTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvButtonTopMargin)
            secondButtonTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonTopMargin)
            imageTopMargin = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvImageTopMargin)
            titleTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvTitleTextAppearance)
            subtitleTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvSubtitleTextAppearance)
            buttonTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvButtonTextAppearance)
            secondButtonTextAppearanceResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonTextAppearance)
            titleLineSpacingExtraPx = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvTitleLineSpacingExtra)
            subtitleLineSpacingExtraPx = ta.obtainDimensionPixelAttribute(R.styleable.StandardPlaceHolderView_pvSubtitleLineSpacingExtra)
            buttonStyleResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvButtonStyle)
            secondButtonStyleResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvSecondButtonStyle)
            imageStyleResId = ta.obtainResourceIdAttribute(R.styleable.StandardPlaceHolderView_pvImageStyle)
        }


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
                PlaceholderStater.StandardLoadState.EMPTY,
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
                PlaceholderStater.StandardLoadState.NOT_FOUND,
                PlaceholderDataContainer.ViewData(
                        notFoundTitle,
                        notFoundSubtitle,
                        notFoundButtonText,
                        notFoundSecondButtonText,
                        notFoundImage))

        val noInternetTitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNoInternetTitle)
        val noInternetSubtitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNoInternetSubtitle)
        val noInternetButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNoInternetButtonText)
        val noInternetSecondButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvNoInternetSecondButtonText)
        val noInternetImage = ta.obtainDrawableAttribute(context, R.styleable.StandardPlaceHolderView_pvNoInternetImage)
        this.dataContainer.putViewData(
                PlaceholderStater.StandardLoadState.NO_INTERNET,
                PlaceholderDataContainer.ViewData(
                        noInternetTitle,
                        noInternetSubtitle,
                        noInternetButtonText,
                        noInternetSecondButtonText,
                        noInternetImage))


        val errorFoundTitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorTitle)
        val errorFoundSubtitle = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorSubtitle)
        val errorFoundButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorButtonText)
        val errorFoundSecondButtonText = ta.obtainStringAttribute(R.styleable.StandardPlaceHolderView_pvErrorSecondButtonText)
        val errorFoundImage = ta.obtainDrawableAttribute(context, R.styleable.StandardPlaceHolderView_pvErrorImage)
        this.dataContainer.putViewData(
                PlaceholderStater.StandardLoadState.ERROR,
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
        titleTv.setLineSpacing(styler.titleLineSpacingExtraPx.toFloat(), 1.0f)
        subtitleTv.setTextAppearanceStyle(styler.subtitleTextAppearanceResId)
        subtitleTv.setLineSpacing(styler.subtitleLineSpacingExtraPx.toFloat(), 1.0f)
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
            progressBar.supportIndeterminateTintList = ColorStateList.valueOf(styler.progressBarColor)
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
        setData()
        setProgressIndicator()
        setVisibility()
    }

    /**
     * Установка цвета фона плейсхолдера в зависимости от текущего LoadState.
     */
    private fun setBackgroundColor() {
        when (stater.loadState) {
            PlaceholderStater.StandardLoadState.TRANSPARENT_LOADING -> {
                if (styler.transparentBackground != NOT_ASSIGNED_RESOURCE) {
                    setBackgroundResource(styler.transparentBackground)
                } else if (styler.transparentBackgroundColor != NOT_ASSIGNED_RESOURCE) {
                    setBackgroundColor(styler.transparentBackgroundColor)
                }
            }
            else -> {
                if (styler.opaqueBackground != NOT_ASSIGNED_RESOURCE) {
                    setBackgroundResource(styler.opaqueBackground)
                } else if (styler.opaqueBackgroundColor != NOT_ASSIGNED_RESOURCE) {
                    setBackgroundColor(styler.opaqueBackgroundColor)
                }
            }
        }
    }

    /**
     * Установка видимости плейсхолдера в зависимости от текущего LoadState.
     */
    private fun setVisibility() {
        when (stater.loadState) {
            PlaceholderStater.StandardLoadState.NONE -> {
                fadeOut(this, 0L, defaultAlpha = 1f)
            }
            PlaceholderStater.StandardLoadState.MAIN_LOADING, PlaceholderStater.StandardLoadState.TRANSPARENT_LOADING -> {
                contentContainer.visibility = View.INVISIBLE
                avIndicatorView?.smoothToShow()
                progressBarContainer.visibility = View.VISIBLE
                fadeIn(this, 0L, defaultAlpha = 1f)
            }
            else -> {
                contentContainer.visibility = View.VISIBLE
                avIndicatorView?.smoothToHide()
                progressBarContainer.visibility = View.INVISIBLE
                fadeIn(this, 0L, defaultAlpha = 1f)
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

        progressBar.invisibleIf(progressBarType != ProgressIndicatorType.STANDARD_CIRCLE_INDICATOR)

        val lp = progressBarContainer.layoutParams
        lp.width = styler.progressBarWidth
        lp.height = styler.progressBarHeight
        progressBarContainer.layoutParams = lp

        if (progressBarType != ProgressIndicatorType.STANDARD_CIRCLE_INDICATOR) {
            val avIndicatorLayout = styler.getLoaderIndicatorLayout(progressBarType)
            avIndicatorView = LayoutInflater.from(context).inflate(avIndicatorLayout, null) as AVLoadingIndicatorView
            avIndicatorView?.setIndicatorColor(styler.progressBarColor)
            progressBarContainer.removeAllViews()
            progressBarContainer.addView(avIndicatorView)
        }
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
    data class PlaceholderStyler(@ColorInt var opaqueBackgroundColor: Int = NOT_ASSIGNED_RESOURCE,
                                 @DrawableRes var opaqueBackground: Int = NOT_ASSIGNED_RESOURCE,
                                 @ColorInt var transparentBackgroundColor: Int = NOT_ASSIGNED_RESOURCE,
                                 @DrawableRes var transparentBackground: Int = NOT_ASSIGNED_RESOURCE,
                                 @ColorInt var progressBarColor: Int = Color.BLACK,
                                 var progressBarType: ProgressIndicatorType = ProgressIndicatorType.STANDARD_CIRCLE_INDICATOR,
                                 var progressBarWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                                 var progressBarHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
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
                                 @StyleRes var imageStyleResId: Int = NOT_ASSIGNED_RESOURCE,
                                 var titleLineSpacingExtraPx: Int = 0,
                                 var subtitleLineSpacingExtraPx: Int = 0) {

        private val loaderIndicatorLayoutList: SparseIntArray by lazy { initializeLoaderIndicatorLayout() }

        fun getLoaderIndicatorLayout(type: ProgressIndicatorType): Int = loaderIndicatorLayoutList.get(type.id)

        private fun initializeLoaderIndicatorLayout(): SparseIntArray {
            val loaderIndicatorLayoutList = SparseIntArray(28)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_BEAT_INDICATOR.id,
                    R.layout.loader_indicator_ball_beat)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_CLIP_ROTATE_INDICATOR.id,
                    R.layout.loader_indicator_ball_clip_rotate)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_CLIP_ROTATE_MULTIPLE_INDICATOR.id,
                    R.layout.loader_indicator_ball_clip_rotate_multiple)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_CLIP_ROTATE_PULSE_INDICATOR.id,
                    R.layout.loader_indicator_ball_clip_rotate_pulse)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_GRID_BEAT_INDICATOR.id,
                    R.layout.loader_indicator_ball_grid_beat)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_GRID_PULSE_INDICATOR.id,
                    R.layout.loader_indicator_ball_grid_pulse)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_PULSE_INDICATOR.id,
                    R.layout.loader_indicator_ball_pulse)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_PULSE_RISE_INDICATOR.id,
                    R.layout.loader_indicator_ball_pulse_rise)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_PULSE_SYNC_INDICATOR.id,
                    R.layout.loader_indicator_ball_pulse_sync)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_ROTATE_INDICATOR.id,
                    R.layout.loader_indicator_ball_rotate)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_SCALE_INDICATOR.id,
                    R.layout.loader_indicator_ball_scale)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_SCALE_MULTIPLE_INDICATOR.id,
                    R.layout.loader_indicator_ball_scale_multiple)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_SCALE_RIPPLE_INDICATOR.id,
                    R.layout.loader_indicator_ball_scale_ripple)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_SCALE_RIPPLE_MULTIPLE_INDICATOR.id,
                    R.layout.loader_indicator_ball_scale_ripple_multiple)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_SPIN_FADE_LOADER_INDICATOR.id,
                    R.layout.loader_indicator_ball_spin_fade)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_ZIG_ZAG_INDICATOR.id,
                    R.layout.loader_indicator_ball_zig_zag)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_ZIG_ZAG_DEFLECT_INDICATOR.id,
                    R.layout.loader_indicator_ball_zig_zag_deflect)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.BALL_TRIANGLE_PATH_INDICATOR.id,
                    R.layout.loader_indicator_ball_triangle_path)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.CUBE_TRANSITION_INDICATOR.id,
                    R.layout.loader_indicator_cube_transition)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.LINE_SCALE_INDICATOR.id,
                    R.layout.loader_indicator_line_scale)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.LINE_SCALE_PARTY_INDICATOR.id,
                    R.layout.loader_indicator_line_scale_party)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.LINE_SCALE_PULSE_OUT_INDICATOR.id,
                    R.layout.loader_indicator_line_scale_pulse_out)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.LINE_SCALE_PULSE_OUT_RAPID_INDICATOR.id,
                    R.layout.loader_indicator_line_scale_pulse_out_rapid)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.LINE_SPIN_FADE_LOADER_INDICATOR.id,
                    R.layout.loader_indicator_line_spin_fade)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.PACMAN_INDICATOR.id,
                    R.layout.loader_indicator_pacman)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.SEMI_CIRCLE_SPIN_INDICATOR.id,
                    R.layout.loader_indicator_semi_circle_spin)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.SQUARE_SPIN_INDICATOR.id,
                    R.layout.loader_indicator_square_spin)
            loaderIndicatorLayoutList.put(ProgressIndicatorType.TRIANGLE_SKEW_SPIN_INDICATOR.id,
                    R.layout.loader_indicator_triangle_skew_spin)
            return loaderIndicatorLayoutList
        }
    }

    /**
     * Хранилище всех данных [StandardPlaceHolderView].
     */
    data class PlaceholderDataContainer(
            var defaultViewData: ViewData = ViewData(),
            private var viewDataMap: ArrayMap<PlaceholderStater.StandardLoadState, ViewData> = ArrayMap()
    ) {

        data class ViewData(var title: String = "",
                            var subtitle: String = "",
                            var buttonText: String = "",
                            var secondButtonText: String = "",
                            var image: Drawable? = null) {

            fun isEmpty() =
                    title.isBlank() && subtitle.isBlank() && buttonText.isBlank() &&
                            secondButtonText.isBlank() && image == null
        }

        fun putViewData(loadState: PlaceholderStater.StandardLoadState, viewData: ViewData) {
            if (!viewData.isEmpty()) {
                viewDataMap[loadState] = viewData
            }
        }

        /**
         * Получение актуальных данных для отображения.
         *
         * @param loadState текущее состояние загрузки
         */
        fun getViewData(loadState: PlaceholderStater.StandardLoadState): ViewData =
                if (loadState == PlaceholderStater.StandardLoadState.NONE) ViewData()
                else viewDataMap[loadState]
                        ?: defaultViewData
    }

    @Suppress("PrivatePropertyName")
    /**
     * Менеджер текущего состояния [StandardPlaceHolderView] и переключения между состояниями.
     *
     * @param onStateChangedLambda лямбда, срабатывающая при изменении состояния [StandardPlaceHolderView].
     */
    class PlaceholderStater(private var onStateChangedLambda: ((loadState: StandardLoadState) -> (Unit))) {

        enum class StandardLoadState {
            NONE, //контент загружен
            MAIN_LOADING, //прогресс, закрывающий весь контент
            TRANSPARENT_LOADING, //полупрозрачный прогресс, блокирует весь интерфейс
            ERROR, //ошибка загрузки данных
            EMPTY, //данных нет
            NOT_FOUND, //нет данных по заданному фильтру
            NO_INTERNET //нет интернет-соединения
        }

        private val STATE_TOGGLE_DELAY_MS: Long = 250

        var loadState = StandardLoadState.NONE          //текущее состояние плейсхолдера
            set(value) {
                field = value
                loadStateSubject.onNext(field)
            }

        //шина изменений loadState
        private var loadStateSubject: PublishSubject<StandardLoadState> = PublishSubject.create()

        init {
            val isFirstEmission = AtomicBoolean(true)
            loadStateSubject.debounce { t ->
                if (isFirstEmission.getAndSet(false)) {
                    return@debounce Observable.just(t)
                } else {
                    return@debounce Observable.just(t).delay(STATE_TOGGLE_DELAY_MS, TimeUnit.MILLISECONDS)
                }
            }
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
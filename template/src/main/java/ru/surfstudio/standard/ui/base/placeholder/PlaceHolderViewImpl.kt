package ru.surfstudio.standard.ui.base.placeholder

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListener
import android.support.v7.widget.AppCompatButton
import android.support.v7.widget.AppCompatTextView
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ru.surfstudio.android.core.ui.base.placeholder.PlaceHolderView
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.standard.R
import java.util.*

/**
 * плейсхолдер с состояниями: PlaceHolderView.State#LOADING, LOADING_TRANSPARENT, EMPTY, ERROR, NOT_FOUND, NONE.
 * Используется на всех экранах, где для отображения контента необходимо сначала загрузить данные.
 */
class PlaceHolderViewImpl @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = R.attr.placeHolderStyle) : FrameLayout(context, attrs, defStyle), PlaceHolderView {

    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == MSG_SHOW || msg.what == MSG_HIDE) {
                visibleUptime = if (msg.what == MSG_HIDE) 0 else SystemClock.uptimeMillis()
                if (msg.obj as Int == this@PlaceHolderViewImpl.visibility) {
                    return
                }

                val msgWhat = msg.what
                val visibility = msg.obj as Int
                ViewCompat.animate(this@PlaceHolderViewImpl)
                        .alpha(if (msg.what == MSG_SHOW) 1.0f else 0f)
                        .setDuration(VISIBILITY_TOGGLE_ANIMATION_DURATION)
                        .setListener(object : ViewPropertyAnimatorListener {
                            override fun onAnimationStart(view: View) {
                                if (view.visibility != View.GONE && view.visibility != View.INVISIBLE) {
                                    view.alpha = if (msgWhat == MSG_SHOW) 0f else 1.0f
                                    super@PlaceHolderViewImpl.setVisibility(View.VISIBLE)
                                }
                            }

                            override fun onAnimationEnd(view: View) {
                                view.alpha = 1.0f

                                super@PlaceHolderViewImpl.setVisibility(visibility)
                            }

                            override fun onAnimationCancel(view: View) {

                                super@PlaceHolderViewImpl.setVisibility(visibility)
                            }
                        })
            }
        }
    }

    private var iconIv: ImageView? = null
    private var titleTv: TextView? = null
    private var subtitleTv: TextView? = null
    private var submitBtn: Button? = null
    private var loadingPb: ProgressBar? = null
    private var contentContainer: ViewGroup? = null

    private var listener: OnActionClickListener? = null

    private var viewDataMap: MutableMap<LoadState, ViewData> = HashMap()
    private var defaultViewData: ViewData? = null

    private var opaqueBackgroundColor = Color.TRANSPARENT
    private var transparentBackgroundColor = Color.TRANSPARENT

    private var state = LoadState.UNSPECIFIED

    private var visibleUptime: Long = 0

    interface OnActionClickListener {
        fun onActionClick(state: LoadState)
    }

    init {
        init(context, attrs, defStyle)
    }

    /**
     * переводит плейсхолдер в нужное состояние
     */
    override fun render(state: LoadState) {
        this.state = state
        if (state == LoadState.UNSPECIFIED || state == LoadState.NONE) {
            visibility = View.INVISIBLE
            return
        }

        var backgroundColor = opaqueBackgroundColor

        if (state == LoadState.MAIN_LOADING || state == LoadState.TRANSPARENT_LOADING) {
            if (state == LoadState.TRANSPARENT_LOADING) {
                backgroundColor = transparentBackgroundColor
            }

            contentContainer!!.visibility = View.INVISIBLE
            loadingPb!!.visibility = View.VISIBLE
        } else {
            contentContainer!!.visibility = View.VISIBLE
            loadingPb!!.visibility = View.INVISIBLE
            updateContentState()
        }

        setBackgroundColor(backgroundColor)
        visibility = View.VISIBLE
    }

    fun setOnActionClickListener(listener: OnActionClickListener) {
        this.listener = listener
    }

    override fun setVisibility(visibility: Int) {
        handler.removeMessages(MSG_HIDE)
        if (visibility != View.VISIBLE) {
            handler.removeMessages(MSG_SHOW)
            val uptime = SystemClock.uptimeMillis()
            handler.sendMessageAtTime(handler.obtainMessage(MSG_HIDE, visibility),
                    uptime + visibleUptime + VISIBILITY_TOGGLE_DELAY_MS - uptime)
        } else {
            visibleUptime = SystemClock.uptimeMillis() + VISIBILITY_TOGGLE_DELAY_MS
            if (!handler.hasMessages(MSG_SHOW)) {
                handler.sendMessageDelayed(handler.obtainMessage(MSG_SHOW, visibility),
                        VISIBILITY_TOGGLE_DELAY_MS)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        handler.removeMessages(MSG_SHOW)
        handler.removeMessages(MSG_HIDE)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeMessages(MSG_SHOW)
        handler.removeMessages(MSG_HIDE)
    }

    private fun init(context: Context, attrs: AttributeSet, defStyle: Int) {
        View.inflate(context, R.layout.placeholder_view_layout, this)

        isClickable = true
        isFocusable = true
        isFocusableInTouchMode = true

        findViews()
        initChildren(context, attrs, defStyle)
        applyAttributes(context, attrs, defStyle)
        render(state)
    }

    private fun findViews() {
        iconIv = findViewById(R.id.placeholder_icon_iv)
        loadingPb = findViewById(R.id.placeholder_loading_pb)
        contentContainer = findViewById(R.id.placeholder_content_view_container)
    }

    private fun initChildren(context: Context, attrs: AttributeSet, defStyle: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.App, defStyle, R.style.Widget_PlaceHolder)

        val titleStyle = ta.getResourceId(R.styleable.App_placeHolderTitleStyle, 0)
        val titleLayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        titleLayoutParams.bottomMargin = resources.getDimensionPixelOffset(R.dimen.default_placeholder_title_margin)
        titleTv = AppCompatTextView(ContextThemeWrapper(context, titleStyle), null, 0)
        titleTv!!.layoutParams = titleLayoutParams

        val subtitleStyle = ta.getResourceId(R.styleable.App_placeHolderSubtitleStyle, 0)
        subtitleTv = AppCompatTextView(ContextThemeWrapper(context, subtitleStyle), null, 0)
        subtitleTv!!.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        val buttonStyle = ta.getResourceId(R.styleable.App_placeHolderButtonStyle, 0)
        val submitLayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        submitLayoutParams.topMargin = resources.getDimensionPixelOffset(R.dimen.default_placeholder_button_margin)
        submitBtn = AppCompatButton(ContextThemeWrapper(context, buttonStyle), null, 0)
        submitBtn!!.layoutParams = submitLayoutParams
        submitBtn!!.visibility = View.GONE

        ta.recycle()

        contentContainer!!.addView(titleTv)
        contentContainer!!.addView(subtitleTv)
        contentContainer!!.addView(submitBtn)

        submitBtn!!.setOnClickListener { _ ->
            if (listener != null) {
                listener!!.onActionClick(state)
            }
        }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet, defStyle: Int) {
        val viewTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderViewImpl, defStyle, R.style.Widget_PlaceHolder)
        val appTypedArray = context.obtainStyledAttributes(attrs, R.styleable.App, defStyle, R.style.Widget_PlaceHolder)

        opaqueBackgroundColor = obtainColorAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_opaqueBackgroundColor,
                opaqueBackgroundColor)
        transparentBackgroundColor = obtainColorAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_transparentBackgroundColor,
                transparentBackgroundColor)

        val emptyTitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_emptyTitle)
        val emptySubtitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_emptySubtitle)
        val emptyButtonText = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_emptyButtonText)
        val emptyIcon = obtainDrawableAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_emptyIcon, context)
        viewDataMap.put(LoadState.EMPTY, ViewData(emptyTitle, emptySubtitle, emptyButtonText, emptyIcon))

        val notFoundTitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_notFoundTitle)
        val notFoundSubtitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_notFoundSubtitle)
        val notFoundButtonText = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_notFoundButtonText)
        val notFoundIcon = obtainDrawableAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_notFoundIcon, context)
        viewDataMap.put(LoadState.NOT_FOUND, ViewData(notFoundTitle, notFoundSubtitle, notFoundButtonText, notFoundIcon))

        val errorTitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_errorTitle)
        val errorSubtitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_errorSubtitle)
        val errorButtonText = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_errorButtonText)
        val errorIcon = obtainDrawableAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_errorIcon, context)
        viewDataMap.put(LoadState.ERROR, ViewData(errorTitle, errorSubtitle, errorButtonText, errorIcon))

        val defaultTitle = appTypedArray.getString(R.styleable.App_placeHolderDefaultTitle)
        val defaultSubtitle = appTypedArray.getString(R.styleable.App_placeHolderDefaultSubtitle)
        val defaultButtonText = appTypedArray.getString(R.styleable.App_placeHolderDefaultButtonText)
        val defaultIcon = obtainDrawableAttribute(appTypedArray, R.styleable.App_placeHolderDefaultIcon, context)
        defaultViewData = ViewData(defaultTitle, defaultSubtitle,
                defaultButtonText, defaultIcon)

        viewTypedArray.recycle()
        appTypedArray.recycle()
    }

    private fun updateContentState() {
        var viewData: ViewData? = viewDataMap[state]
        if (viewData == null || viewData.isEmpty) {
            viewData = defaultViewData
        }

        if (viewData!!.icon != null) {
            iconIv!!.setImageDrawable(viewData.icon)
            if (iconIv!!.visibility != View.VISIBLE) {
                iconIv!!.visibility = View.VISIBLE
            }
        } else if (iconIv!!.visibility != View.GONE) {
            iconIv!!.visibility = View.GONE
        }

        setContentTextOrHide(titleTv, viewData.title)
        setContentTextOrHide(subtitleTv, viewData.subtitle)
        setContentTextOrHide(submitBtn, viewData.buttonText)
    }

    private class ViewData(internal val title: String, internal val subtitle: String, internal val buttonText: String, internal val icon: Drawable?) {

        internal val isEmpty: Boolean
            get() = TextUtils.isEmpty(title) &&
                    TextUtils.isEmpty(subtitle) &&
                    TextUtils.isEmpty(buttonText) &&
                    icon == null
    }

    companion object {
        private val VISIBILITY_TOGGLE_ANIMATION_DURATION: Long = 250
        private val VISIBILITY_TOGGLE_DELAY_MS: Long = 250

        private val MSG_SHOW = 1
        private val MSG_HIDE = 0

        private fun setContentTextOrHide(textView: TextView?, text: String) {
            val visibility = textView!!.visibility
            if (!TextUtils.isEmpty(text)) {
                textView.text = text
                if (visibility != View.VISIBLE) {
                    textView.visibility = View.VISIBLE
                }
            } else if (visibility != View.GONE) {
                textView.visibility = View.GONE
            }
        }

        private fun obtainColorAttribute(ta: TypedArray, idx: Int, fallback: Int): Int {
            var color = fallback
            try {
                color = ta.getColor(idx, fallback)
            } catch (e: UnsupportedOperationException) {
                val tv = TypedValue()
                ta.getValue(idx, tv)
                Logger.w("PlaceHolderView : неопознанный тип цвета. " +
                        "Забыл установить в стилях?\n" + tv.coerceToString(), e)
            }

            return color
        }

        private fun obtainDrawableAttribute(ta: TypedArray, idx: Int, context: Context): Drawable? {
            val resId = ta.getResourceId(idx, 0)
            return if (resId == 0) {
                null
            } else ContextCompat.getDrawable(context, resId)

        }
    }
}
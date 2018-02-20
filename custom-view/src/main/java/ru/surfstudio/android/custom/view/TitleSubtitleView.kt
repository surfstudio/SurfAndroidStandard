package ru.surfstudio.android.core.ui.widjet

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import ru.surfstudio.android.custom.view.R

private const val DEFAULT_MAX_LINES: Int = 1

/**
 * Виджет для отображения текста с подписью
 *
 * Изменять параметры форматирования текста можно посредством атрибутов в xml.
 * Возможно изменение заголовка и подписи по отдельности.
 */
class TitleSubtitleView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var titleView: TextView = TextView(context)
    private var subTitleView: TextView = TextView(context)

    private var defaultTitle: String = "Title"
        set(value) {
            field = value
            titleView.text = field
        }

    private var defaultSubTitle: String = "SubTitle"
        set(value) {
            field = value
            subTitleView.text = field
        }

    var titleText: String = defaultTitle
        set(value) {
            field = value
            updateView()
        }

    var subTitleText: String = defaultSubTitle
        set(value) {
            field = value
            updateView()
        }

    var onTitleClickListenerCallback: (String) -> Unit = {}
    var onSubTitleClickListenerCallback: (String) -> Unit = {}

    init {
        orientation = LinearLayout.VERTICAL

        addViews()
        applyAttrs(attributeSet)
        initListeners()
    }

    /**
     * Возвращает заголовок к дефолтному значению
     */
    fun resetTitleText() {
        titleText = defaultTitle
    }

    /**
     * Возвращает подзаголовок к дефолтному значению
     */
    fun resetSubTitleText() {
        subTitleText = defaultSubTitle
    }

    private fun addViews() {
        addView(titleView)
        addView(subTitleView)
    }

    private fun initListeners() {
        titleView.setOnClickListener { onTitleClickListenerCallback(titleText) }
        subTitleView.setOnClickListener { onSubTitleClickListenerCallback(subTitleText) }
    }

    private fun updateView() {
        titleView.text = titleText
        subTitleView.text = subTitleText
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        val ta: TypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TitleSubtitleView, 0, 0)

        //заголовок
        setupTitle(ta)
        //подзаголовок
        setupSubTitle(ta)

        ta.recycle()
    }

    private fun setupTitle(ta: TypedArray) {
        with(titleView) {
            defaultTitle = ta.getString(R.styleable.TitleSubtitleView_titleText) ?: defaultTitle

            setupTextAppearance(ta, R.styleable.TitleSubtitleView_titleTextAppearance)
            setupTextSize(ta, R.styleable.TitleSubtitleView_titleTextSize)

            setTextColor(ta.getColor(
                    R.styleable.TitleSubtitleView_titleTextColor,
                    ContextCompat.getColor(context, android.R.color.black)
            ))

            setLineSpacing(ta.getDimension(R.styleable.TitleSubtitleView_titleLineSpacingExtra, 0f), 1f)
            setPadding(
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingStart, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingTop, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingEnd, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_titlePaddingBottom, 0)
            )

            setLines(ta.getInt(R.styleable.TitleSubtitleView_titleLines, lineCount))
            maxLines = ta.getInt(R.styleable.TitleSubtitleView_titleMaxLines, DEFAULT_MAX_LINES)

            gravity = ta.getInt(R.styleable.TitleSubtitleView_titleGravity, gravity)

            setBackgroundColor(ta.getColor(
                    R.styleable.TitleSubtitleView_titleBackgroundColor,
                    ContextCompat.getColor(context, android.R.color.transparent)
            ))

            ellipsize = getEllipsizeFromResource(ta, R.styleable.TitleSubtitleView_titleEllipsize)
        }
    }

    private fun setupSubTitle(ta: TypedArray) {
        with(subTitleView) {
            defaultSubTitle = ta.getString(R.styleable.TitleSubtitleView_subTitleText) ?: defaultSubTitle

            setupTextAppearance(ta, R.styleable.TitleSubtitleView_subTitleTextAppearance)
            setupTextSize(ta, R.styleable.TitleSubtitleView_subTitleTextSize)

            setLineSpacing(ta.getDimension(R.styleable.TitleSubtitleView_subTitleLineSpacingExtra, 0f), 1f)

            setTextColor(ta.getColor(
                    R.styleable.TitleSubtitleView_subTitleTextColor,
                    ContextCompat.getColor(context, android.R.color.black)
            ))
            setPadding(
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingStart, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingTop, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingEnd, 0),
                    ta.getDimensionPixelOffset(R.styleable.TitleSubtitleView_subTitlePaddingBottom, 0)
            )

            setLines(ta.getInt(R.styleable.TitleSubtitleView_subTitleLines, lineCount))
            maxLines = ta.getInt(R.styleable.TitleSubtitleView_subTitleMaxLines, DEFAULT_MAX_LINES)

            gravity = ta.getInt(R.styleable.TitleSubtitleView_subTitleGravity, gravity)

            setBackgroundColor(ta.getColor(
                    R.styleable.TitleSubtitleView_subTitleBackgroundColor,
                    ContextCompat.getColor(context, android.R.color.transparent)
            ))

            ellipsize = getEllipsizeFromResource(ta, R.styleable.TitleSubtitleView_subTitleEllipsize)
        }
    }

    private fun TextView.setupTextAppearance(ta: TypedArray, index: Int) {
        val ap = ta.getResourceId(index, -1)
        if (ap != -1) {
            TextViewCompat.setTextAppearance(this, ap)
        }
    }

    private fun TextView.setupTextSize(ta: TypedArray, index: Int) {
        val size = ta.getDimensionPixelSize(index, -1).toFloat()
        if (size > 0) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        }
    }

    private fun getEllipsizeFromResource(ta: TypedArray, index: Int): TextUtils.TruncateAt? =
            when (ta.getInt(index, 0)) {
                1 -> TextUtils.TruncateAt.START
                2 -> TextUtils.TruncateAt.MIDDLE
                3 -> TextUtils.TruncateAt.END
                4 -> TextUtils.TruncateAt.MARQUEE
                else -> TextUtils.TruncateAt.END
            }
}
package ru.surfstudio.android.core.ui.base.screen.widjet

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.content.ContextCompat
import android.support.v4.widget.TextViewCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import ru.surfstudio.android.core.R
import ru.surfstudio.android.logger.Logger

/**
 * Виджет для отображения текста с подписью
 */

private const val DEFAULT_MAX_LINES: Int = 5

class TitleSubtitleView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr) {

    private var titleView: TextView
    private var subTitleView: TextView

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
        inflate(context, R.layout.layout_title_subtitle_view, this)
        orientation = LinearLayout.VERTICAL

        titleView = findViewById(R.id.view_title_subtitle_title_tv)
        subTitleView = findViewById(R.id.view_title_subtitle_subtitle_tv)
//        addViews()
        applyAttrs(attributeSet)
        initListeners()
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

            val ap = ta.getResourceId(R.styleable.TitleSubtitleView_titleTextAppearance, -1)
            Logger.d("TitleSubtitleView ttleTextAppearance $ap")
            if (ap != -1) {
                TextViewCompat.setTextAppearance(this, ap)
            }

            textSize = ta.getDimensionPixelSize(R.styleable.TitleSubtitleView_titleTextSize, -1).toFloat()

            setTextColor(ta.getColor(
                    R.styleable.TitleSubtitleView_titleTextColor,
                    ContextCompat.getColor(context, android.R.color.black)
            ))
            Logger.d("TitleSubtitleView ttleTextAppearance after $ap")
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

            val ap = ta.getResourceId(R.styleable.TitleSubtitleView_subTitleTextAppearance, -1)
            if (ap != -1) {
                TextViewCompat.setTextAppearance(this, ap)
            }

            val size = ta.getDimensionPixelSize(R.styleable.TitleSubtitleView_subTitleTextSize, textSize.toInt()).toFloat()
            if (size > 0) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
            }
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

    private fun getEllipsizeFromResource(ta: TypedArray, index: Int): TextUtils.TruncateAt? =
            when (ta.getInt(index, 0)) {
                1 -> TextUtils.TruncateAt.START
                2 -> TextUtils.TruncateAt.MIDDLE
                3 -> TextUtils.TruncateAt.END
                4 -> TextUtils.TruncateAt.MARQUEE
                else -> TextUtils.TruncateAt.END
            }
}
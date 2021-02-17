package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.EasyAdapter
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.utilktx.util.ViewUtil
import ru.surfstudio.standard.ui.view.keyboard.CustomKeyboardUtils.getKeysList
import ru.surfstudio.standard.ui.view.keyboard.controller.EmptyKeyController
import ru.surfstudio.standard.ui.view.keyboard.controller.IconController
import ru.surfstudio.standard.ui.view.keyboard.controller.KeyController

/**
 * Вью клавиатуры
 * todo удалить, если не требуется на проекте
 */
class KeyboardView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0
) : RecyclerView(context, attrs, defStyleAttrs) {

    /**
     * Кнопка слева от кнопки ноля
     */
    var buttonLeft: Key = EmptyKey()
        set(value) {
            field = value
            render()
        }

    /**
     * Кнопка справа от кнопки ноля
     */
    var buttonRight: Key = EmptyKey()
        set(value) {
            field = value
            render()
        }

    /**
     * Флаг показывает нужно ли отображать на кнопках буквы под цифрами
     */
    var areLettersVisible = true
        set(value) {
            field = value
            keyController.isSubtitleVisible = value
            render()
        }

    /**
     * Обработчик нажатия цифры на клавиатуре
     */
    var onNumberKeyClick: (code: String) -> Unit = {}
        set(value) {
            field = value
            render()
        }

    private val easyAdapter = EasyAdapter().apply { isFirstInvisibleItemEnabled = true }

    private lateinit var keyController: KeyController
    private val iconController = IconController()
    private val emptyController = EmptyKeyController()

    init {
        initAttrs(attrs)
        layoutManager = GridLayoutManager(context, KEYBOARD_COLUMNS_COUNT)
        overScrollMode = View.OVER_SCROLL_NEVER
        adapter = easyAdapter
    }

    private fun render() {
        easyAdapter.setItems(
                ItemList.create().apply {
                    getKeysList(onNumberKeyClick, buttonLeft, buttonRight).forEach {
                        when (it) {
                            is TextKey -> add(it, keyController)
                            is IconKey -> add(it, iconController)
                            is EmptyKey -> add(it, emptyController)
                        }
                    }
                }
        )
    }

    private fun initAttrs(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.KeyboardView) {
            keyController = KeyController(
                    titleTextColor = getColor(R.styleable.KeyboardView_titleTextColor, KeyView.DEFAULT_TITLE_COLOR),
                    subtitleTextColor = getColor(R.styleable.KeyboardView_subtitleTextColor, KeyView.DEFAULT_SUBTITLE_COLOR),
                    titleTextSize = getDimension(
                            R.styleable.KeyboardView_titleTextSize,
                            ViewUtil.convertSpToPx(context, KeyView.DEFAULT_TITLE_SIZE_SP).toFloat()
                    ),
                    subtitleTextSize = getDimension(
                            R.styleable.KeyboardView_subtitleTextSize,
                            ViewUtil.convertSpToPx(context, KeyView.DEFAULT_SUBTITLE_SIZE_SP).toFloat()
                    ),
                    titleFont = getResourceId(R.styleable.KeyboardView_titleFont, KeyView.UNDEFINE_ATTR),
                    subtitleFont = getResourceId(R.styleable.KeyboardView_subtitleFont, KeyView.UNDEFINE_ATTR),
                    subtitleMargin = getDimension(
                            R.styleable.KeyboardView_subtitle_margin,
                            ViewUtil.convertDpToPx(context, KeyView.DEFAULT_SUBTITLE_MARGIN_DP).toFloat()
                    )
            )

            areLettersVisible = getBoolean(R.styleable.KeyboardView_areLettersVisible, true)
        }
    }

    companion object {

        const val KEYBOARD_COLUMNS_COUNT = 3
    }
}

package ru.surfstudio.standard.ui.view.keyboard

import androidx.annotation.DrawableRes
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

//todo удалить, если не требуется на проекте

/**
 * Кнопка на клавиатуре
 */
interface Key

/**
 * Пустое пространство на клавиатуре
 */
class EmptyKey : Key

/**
 * Класс изображения на клавиатуре
 */
data class IconKey(
        @DrawableRes val icon: Int,
        val onClickListener: () -> Unit
) : Key

/**
 * Кнопка с текстом на клавиатуре
 */
data class TextKey(
        val title: String,
        val subtitle: String = EMPTY_STRING,
        val code: String = title,
        val onClickListener: (code:String) -> Unit
) : Key

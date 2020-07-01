package ru.surfstudio.standard.ui.view.keyboard

import androidx.annotation.DrawableRes
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Кнопка на клавиатуре
 */
interface Key

/**
 * Пустое пространство на клавиатуре
 */
class EmptyKey : Key

/**
 * Базовый класс изображения на клавиатуре
 */
abstract class BaseIconKey : Key {

    @get:DrawableRes
    abstract val icon: Int
}

/**
 * Базовый класс кнопки с текстом на клавиатуре
 */
abstract class BaseTextKey : Key {

    abstract val title: String
    abstract val subtitle: String
}

/**
 * Класс изображения на клавиатуре
 */
data class IconKey(@DrawableRes override val icon: Int) : BaseIconKey()

/**
 * Кнопка с текстом на клавиатуре
 */
data class TextKey(
        override val title: String,
        override val subtitle: String = EMPTY_STRING,
        val code: String = title
) : BaseTextKey()

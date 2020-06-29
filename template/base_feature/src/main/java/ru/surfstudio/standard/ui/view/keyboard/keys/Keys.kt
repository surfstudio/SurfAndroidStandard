package ru.surfstudio.standard.ui.view.keyboard.keys

import androidx.annotation.DrawableRes

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
        val code: String = title
) : BaseTextKey()

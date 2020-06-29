package ru.surfstudio.standard.ui.view.keyboard.keys

import androidx.annotation.DrawableRes

interface Key

class EmptyKey : Key

abstract class BaseIconKey : Key {

    @get:DrawableRes
    abstract val icon: Int
}

/**
 * Базовый класс кнопки с текстом на клавиатуре
 */
abstract class BaseTextKey : Key {

    abstract val text: String
}

data class IconKey(@DrawableRes override val icon: Int) : BaseIconKey()

/**
 * Кнопка с текстом на клавиатуре
 */
data class TextKey(override val text: String, val code: String = text) : BaseTextKey()

package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data

import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Тестовая модель данных: киця.
 * */
internal data class Kitty(
        val name: String = EMPTY_STRING,
        val picturePreviewUrl: String = EMPTY_STRING
)
package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data

import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

internal data class Kitten(
        val name: String = EMPTY_STRING,
        val picturePreviewUrl: String = EMPTY_STRING
)
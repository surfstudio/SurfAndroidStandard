package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties.data

import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

internal data class DividerUi(
        val type: DividerType,
        val title: String,
        val isLoading: Boolean = false,
        val isAllVisible: Boolean = false
)
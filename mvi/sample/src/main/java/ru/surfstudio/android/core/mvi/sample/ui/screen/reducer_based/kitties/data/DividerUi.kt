package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.data

internal data class DividerUi(
        val type: DividerType,
        val title: String,
        val isLoading: Boolean = false,
        val isAllVisible: Boolean = false
)
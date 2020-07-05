package ru.surfstudio.standard.f_debug.ui_tools

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель экрана показа UI-tools
 */
class UiToolsDebugScreenModel(
        var isFpsEnabled: Boolean = false
) : ScreenModel()
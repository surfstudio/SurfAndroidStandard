package ru.surfstudio.standard.f_debug.server_settings

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель экрана настроек сервера
 */
class ServerSettingsDebugScreenModel(
        var isChuckEnabled: Boolean,
        var isTestServerEnabled: Boolean,
        var requestDelaySeconds: Float,
        var requestDelayCoefficient: Int
) : ScreenModel()
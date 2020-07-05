package ru.surfstudio.standard.f_debug.server_settings.reboot

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель экрана перезапуска приложения
 */
class RebootDebugScreenModel(var secondBeforeReboot: Long) : ScreenModel()
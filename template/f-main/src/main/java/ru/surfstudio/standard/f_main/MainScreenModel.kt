package ru.surfstudio.standard.f_main

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.standard.ui.navigation.MainTabType

/**
 * Модель главного экрана
 */
data class MainScreenModel(
     var tabType: MainTabType = MainTabType.FEED
) : ScreenModel()

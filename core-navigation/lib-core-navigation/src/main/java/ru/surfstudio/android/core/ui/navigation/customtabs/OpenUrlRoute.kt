package ru.surfstudio.android.core.ui.navigation.customtabs

import androidx.annotation.ColorRes
import ru.surfstudio.android.core.ui.navigation.R
import ru.surfstudio.android.core.ui.navigation.Route

/**
 * Route для открытия url
 */
data class OpenUrlRoute(
        val url: String,
        @ColorRes val toolbarColorId: Int = R.color.design_default_color_primary,
        @ColorRes val secondaryToolbarColorId: Int = R.color.design_default_color_primary_dark
) : Route
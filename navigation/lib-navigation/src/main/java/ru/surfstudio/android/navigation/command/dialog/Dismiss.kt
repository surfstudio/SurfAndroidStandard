package ru.surfstudio.android.navigation.command.dialog

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

/**
 * Dismiss dialog from the screen
 */
data class Dismiss(
        override val route: DialogRoute,
        override val animations: Animations
) : DialogNavigationCommand
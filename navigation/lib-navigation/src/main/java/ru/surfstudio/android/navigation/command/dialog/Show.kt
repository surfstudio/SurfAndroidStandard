package ru.surfstudio.android.navigation.command.dialog

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

/**
 * Show dialog on the screen
 */
data class Show(
        override val route: DialogRoute,
        override val animations: Animations = EmptyResourceAnimations
) : DialogNavigationCommand
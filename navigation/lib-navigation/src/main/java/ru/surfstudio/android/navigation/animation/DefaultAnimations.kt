package ru.surfstudio.android.navigation.animation

import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.animation.resource.NoResourceAnimations

/**
 * Object with the default animations for command execution.
 *
 * You can set the desired default animations in your app, and all command of a desired screen type
 * will be executed with this animations.
 */
object DefaultAnimations {

    /**
     * Animations to be executed for each
     * [ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand].
     * Warning! Activity animations are experimental and may not always work correctly.
     */
    var activity: Animations = EmptyResourceAnimations

    /**
     * Animations to be executed for each
     * [ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand].
     */
    var fragment: Animations = EmptyResourceAnimations


    /**
     * Animations to be executed for each
     * [ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand].
     *
     * Warning! Dialog animations are not yet implemented.
     */
    @Deprecated("Dialog animations are not yet implemented.")
    var dialog: Animations = NoResourceAnimations

    /**
     * Animations to be executed for each WidgetNavigationCommand

     * Warning! Widget animations are not yet implemented.
     */
    @Deprecated("Widget animations are not yet implemented.")
    var widget: Animations = NoResourceAnimations
}
package ru.surfstudio.android.navigation.animation.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.animation.set.SetAnimations
import ru.surfstudio.android.navigation.animation.shared.SharedElementAnimations
import ru.surfstudio.android.navigation.animation.styled.StyledAnimations

/**
 * Supplier which is responsible for inflating Activity transition with animations.
 */
open class ActivityAnimationSupplier {

    /**
     * Add animations to activity transition options.
     *
     * @param activity activity which will execute transition
     * @param options bundle with activity transition options
     * @param animations bundle with animations
     *
     * @return [Bundle] options with animations
     */
    open fun supplyWithAnimations(
            activity: Activity,
            options: Bundle?,
            animations: Animations
    ): Bundle? {
        return when (animations) {
            is SetAnimations -> {
                val nonNullOptions = options ?: Bundle()
                animations.set.forEach { supplyWithAnimations(activity, nonNullOptions, it) }
                if (nonNullOptions.isEmpty) null else nonNullOptions
            }
            is SharedElementAnimations ->
                setSharedElementAnimations(activity, options, animations)
            is BaseResourceAnimations ->
                setResourceAnimations(activity, options, animations)
            is StyledAnimations ->
                setStyledAnimations(activity, options, animations)
            else -> null
        }
    }

    /**
     * Add resource animations to activity transition options.
     *
     * @param context activity which will execute transition
     * @param options bundle with activity transition options
     * @param animations bundle with animations
     *
     * @return [Bundle] options with animations
     */
    fun setResourceAnimations(
            context: Context,
            options: Bundle?,
            animations: BaseResourceAnimations
    ): Bundle? {
        val resourceAnimations = ActivityOptionsCompat.makeCustomAnimation(
                context,
                animations.enterAnimation,
                animations.exitAnimation
        )
        val nonNullOptions = options ?: Bundle()
        nonNullOptions.putAll(resourceAnimations.toBundle())
        return nonNullOptions
    }

    /**
     * Add [SharedElementAnimations] to activity transition options.
     *
     * @param activity activity which will execute transition
     * @param options bundle with activity transition options
     * @param animations bundle with animations
     *
     * @return [Bundle] options with animations
     */
    fun setSharedElementAnimations(
            activity: Activity,
            options: Bundle?,
            animations: SharedElementAnimations
    ): Bundle? {
        val sharedElements = animations.sharedElements
                .map { androidx.core.util.Pair(it.sharedView, it.transitionName) }
        val resourceAnimations =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedElements.toTypedArray())
        val nonNullOptions = options ?: Bundle()
        nonNullOptions.putAll(resourceAnimations.toBundle())
        return nonNullOptions
    }

    /**
     * Add [StyledAnimations] to activity window style.
     *
     * @param activity activity which will execute transition
     * @param options bundle with activity transition options
     * @param animations bundle with animations
     *
     * @return [Bundle] options with animations
     */
    fun setStyledAnimations(
            activity: Activity,
            options: Bundle?,
            animations: StyledAnimations
    ): Bundle? {
        activity.window.setWindowAnimations(animations.style)
        return options ?: Bundle()
    }
}
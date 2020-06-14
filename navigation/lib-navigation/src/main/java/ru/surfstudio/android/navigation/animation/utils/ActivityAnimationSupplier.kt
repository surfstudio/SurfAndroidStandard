package ru.surfstudio.android.navigation.animation.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.animation.set.SetAnimations
import ru.surfstudio.android.navigation.animation.shared.SharedElementAnimations

open class ActivityAnimationSupplier {

    fun setResourceAnimations(
            context: Context,
            optionsCompat: Bundle?,
            animationBundle: BaseResourceAnimations
    ): Bundle? {
        val resourceAnimations = ActivityOptionsCompat.makeCustomAnimation(
                context,
                animationBundle.enterAnimation,
                animationBundle.exitAnimation
        )
        return resourceAnimations.toBundle()?.apply { optionsCompat?.let(::putAll) }
    }

    fun setSharedElementAnimations(
            activity: Activity,
            optionsCompat: Bundle?,
            animationBundle: SharedElementAnimations
    ): Bundle? {
        val sharedElements = animationBundle.sharedElements
                .map { androidx.core.util.Pair(it.sharedView, it.transitionName) }
        val resourceAnimations =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedElements.toTypedArray())
        return resourceAnimations.toBundle()?.apply { optionsCompat?.let(::putAll) }
    }

    open fun supplyWithAnimations(
            activity: Activity,
            options: Bundle?,
            animations: Animations
    ): Bundle? {
        return when (animations) {
            is SetAnimations -> {
                val unwrappedBundles = animations.set.map { supplyWithAnimations(activity, options, it) }
                val setBundle = Bundle().apply { unwrappedBundles.forEach { it?.let(::putAll) } }
                if (setBundle.isEmpty) null else setBundle
            }
            is SharedElementAnimations ->
                setSharedElementAnimations(activity, options, animations)
            is BaseResourceAnimations ->
                setResourceAnimations(activity, options, animations)
            else -> null
        }
    }
}
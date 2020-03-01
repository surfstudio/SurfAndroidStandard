package ru.surfstudio.android.navigation.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.animation.shared.SharedElementAnimations

class ActivityAnimationSupplier() {

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
}
package ru.surfstudio.android.navigation.animation.utils

import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.animation.set.SetAnimations
import ru.surfstudio.android.navigation.animation.shared.SharedElementAnimations

/**
 * Supplier, which is responsible for inflating [FragmentTransaction] with animations.
 */
open class FragmentAnimationSupplier {

    /**
     * Adds animations of a different types to a [FragmentTransaction].
     */
    open fun supplyWithAnimations(
            transaction: FragmentTransaction,
            animations: Animations
    ): FragmentTransaction {
        when (animations) {
            is SetAnimations ->
                animations.set.forEach { supplyWithAnimations(transaction, it) }
            is BaseResourceAnimations ->
                setResourceAnimations(transaction, animations, false)
            is SharedElementAnimations ->
                setSharedElementAnimations(transaction, animations)
        }
        return transaction
    }

    /**
     * Add transition animations for [FragmentTransaction].
     *
     * @param animationBundle bundle with animations
     * @param shouldReverse should we reverse animation (use pop-animations for normal, and normal for pops.)
     *
     * @return [FragmentTransaction]
     */
    fun setResourceAnimations(
            transaction: FragmentTransaction,
            animationBundle: BaseResourceAnimations,
            shouldReverse: Boolean = false
    ): FragmentTransaction {
        val enterAnimation: Int
        val exitAnimation: Int
        val popEnterAnimation: Int
        val popExitAnimation: Int

        if (shouldReverse) {
            enterAnimation = animationBundle.popEnterAnimation
            exitAnimation = animationBundle.popExitAnimation
            popEnterAnimation = animationBundle.enterAnimation
            popExitAnimation = animationBundle.exitAnimation
        } else {
            enterAnimation = animationBundle.enterAnimation
            exitAnimation = animationBundle.exitAnimation
            popEnterAnimation = animationBundle.popEnterAnimation
            popExitAnimation = animationBundle.popExitAnimation
        }

        return transaction.setCustomAnimations(
                enterAnimation,
                exitAnimation,
                popEnterAnimation,
                popExitAnimation
        )
    }

    /**
     * Add [SharedElementAnimations] to [FragmentTransaction].
     *
     * @param animations bundle with animations
     *
     * @return [FragmentTransaction]
     */
    fun setSharedElementAnimations(
            transaction: FragmentTransaction,
            animations: SharedElementAnimations
    ): FragmentTransaction {
        val sharedElements = animations.sharedElements
        if (sharedElements.isNotEmpty()) {
            transaction.setReorderingAllowed(true)
            sharedElements.forEach { element ->
                transaction.addSharedElement(element.sharedView, element.transitionName)
            }
            sharedElements.clear()
        }
        return transaction
    }
}
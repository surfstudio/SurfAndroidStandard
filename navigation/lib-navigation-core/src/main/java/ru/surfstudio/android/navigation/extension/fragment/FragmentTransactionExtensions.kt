package ru.surfstudio.android.navigation.extension.fragment

import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.BaseScreenAnimations
import ru.surfstudio.android.navigation.animation.shared.SharedElementScreenAnimations

/**
 * Add all screen animations from [BaseScreenAnimations].
 *
 * @param animationBundle bundle with animations
 * @param shouldReverse should we reverse animation (use pop-animations for normal, and normal for pops.)
 *
 * @return [FragmentTransaction]
 */
fun FragmentTransaction.setAnimations(
        animationBundle: BaseScreenAnimations,
        shouldReverse: Boolean = false
): FragmentTransaction {
    return this
            .setCustomAnimations(animationBundle, shouldReverse)
            .setSharedElementTransition(animationBundle)
}

/**
 * Add transition animations for [FragmentTransaction].
 *
 * @param animationBundle bundle with animations
 * @param shouldReverse should we reverse animation (use pop-animations for normal, and normal for pops.)
 *
 * @return [FragmentTransaction]
 */
fun FragmentTransaction.setCustomAnimations(
        animationBundle: BaseScreenAnimations,
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

    return setCustomAnimations(
            enterAnimation,
            exitAnimation,
            popEnterAnimation,
            popExitAnimation
    )
}

/**
 * Add [SharedElementScreenAnimations] to [FragmentTransaction].
 *
 * @param animationBundle bundle with animations
 *
 * @return [FragmentTransaction]
 */
fun FragmentTransaction.setSharedElementTransition(
        animationBundle: BaseScreenAnimations
): FragmentTransaction {
    if (animationBundle is SharedElementScreenAnimations) {
        // Добавляем параметр только один раз, пока sharedElements не пустые
        if (animationBundle.sharedElements.isNotEmpty()) {
            setReorderingAllowed(true)
        }
        animationBundle.sharedElements.forEach { element ->
            addSharedElement(element.sharedView, element.transitionName)
        }
        // Очищаем элементы, как только добавили в transaction
        animationBundle.sharedElements.clear()
    }
    return this
}

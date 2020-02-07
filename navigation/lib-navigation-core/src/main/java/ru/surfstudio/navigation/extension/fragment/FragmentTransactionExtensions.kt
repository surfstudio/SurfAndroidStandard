package ru.surfstudio.navigation.extension.fragment

import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.navigation.animation.BaseScreenAnimations
import ru.surfstudio.navigation.animation.shared.SharedElementScreenAnimations

/**
 * Добавление всех возможных анимаций из бандла [ScreenAnimationBundle]
 *
 * @param animationBundle бандл с анимациями
 * @param shouldReverse необходимо ли использовать реверс анимаций (использовать pop* для обычных, и обычные для *pop)
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
 * Добавление анимаций перехода к FragmentTransaction.
 *
 * @param animationBundle бандл с анимациями
 * @param shouldReverse необходимо ли использовать реверс анимаций (использовать pop* для обычных, и обычные для *pop)
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
 * Добавление SharedElementTransaction к FragmentTransaction.
 *
 * @param animationBundle бандл с анимациями
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

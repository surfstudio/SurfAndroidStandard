package ru.surfstudio.android.navigation.sample.app.utils.animations

import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.sample.R

/**
 * Набор анимаций Slide In/Out для переключения фрагментов.
 */
object SlideAnimations : BaseResourceAnimations(
        enterAnimation = R.anim.slide_in_right,
        exitAnimation = R.anim.slide_out_left,
        popEnterAnimation = R.anim.slide_in_left,
        popExitAnimation = R.anim.slide_out_right
)

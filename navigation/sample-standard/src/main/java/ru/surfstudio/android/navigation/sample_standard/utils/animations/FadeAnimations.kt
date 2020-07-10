package ru.surfstudio.android.navigation.sample_standard.utils.animations

import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.sample_standard.R


/**
 * Набор анимаций Fade In/Out для переключения фрагментов.
 */
class FadeAnimations : BaseResourceAnimations(
        enterAnimation = R.anim.fade_in_fast,
        exitAnimation = R.anim.fade_out_fast,
        popEnterAnimation = R.anim.fade_in_fast,
        popExitAnimation = R.anim.fade_out_fast
)

package ru.surfstudio.android.navigation.sample.app.utils.animations

import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.sample.R

object ModalAnimations: BaseResourceAnimations(
        enterAnimation = R.anim.slide_in_from_bottom,
        exitAnimation = R.anim.fade_out_fast,
        popEnterAnimation = R.anim.fade_in_fast,
        popExitAnimation = R.anim.slide_out_to_bottom
)
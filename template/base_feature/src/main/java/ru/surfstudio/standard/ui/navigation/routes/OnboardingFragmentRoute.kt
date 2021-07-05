package ru.surfstudio.standard.ui.navigation.routes

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Роут экрана онбординга
 */
class OnboardingFragmentRoute : FragmentRoute() {

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.standard.f_onboarding.OnboardingFragmentView"
    }
}

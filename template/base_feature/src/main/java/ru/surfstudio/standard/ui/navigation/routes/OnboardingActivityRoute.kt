package ru.surfstudio.standard.ui.navigation.routes

import ru.surfstudio.android.navigation.route.activity.ActivityRoute

class OnboardingActivityRoute : ActivityRoute() {

    override fun getScreenClassPath(): String {
        return "ru.surfstudio.standard.f_onboarding.OnboardingActivityView"
    }

}

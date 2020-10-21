package ru.surfstudio.android.navigation.sample.app.screen.splash

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class SplashRoute : FragmentRoute() {

    override fun getScreenClass(): Class<out Fragment> = SplashFragment::class.java
}
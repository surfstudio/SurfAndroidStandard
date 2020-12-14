package ru.surfstudio.android.navigation.sample.app.screen.auth

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class AuthRoute : FragmentRoute() {
    override fun getScreenClass(): Class<out Fragment>? = AuthFragment::class.java
}
package ru.surfstudio.android.navigation.sample.app.screen.main

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class MainRoute : FragmentRoute() {

    override fun getScreenClassPath(): String? {
        return "ru.surfstudio.android.navigation.sample.app.screen.main.MainFragment"
    }
}
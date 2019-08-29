package ru.surfstudio.android.core.ui.sample.ui.screen.fragment_1

import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute

class Fragment1Route : FragmentRoute() {
    override fun getFragmentClass(): Class<out Fragment> = Fragment1View::class.java
}
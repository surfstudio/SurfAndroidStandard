package ru.surfstudio.android.core.ui.sample.ui.screen.main_fragment

import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute

class MainFragmentRoute : FragmentRoute() {
    override fun getFragmentClass(): Class<out Fragment> = MainFragmentView::class.java
}
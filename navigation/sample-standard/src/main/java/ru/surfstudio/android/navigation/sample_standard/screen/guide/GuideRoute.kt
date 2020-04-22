package ru.surfstudio.android.navigation.sample_standard.screen.guide

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

class GuideRoute : FragmentRoute() {

    override fun getScreenClass(): Class<out Fragment>? = GuideFragmentView::class.java
}
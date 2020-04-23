package ru.surfstudio.standard.ui.navigation

import ru.surfstudio.android.core.ui.navigation.feature.route.feature.FragmentCrossFeatureRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.RootFragmentRoute

class FeedFragmentRoute : FragmentCrossFeatureRoute(), RootFragmentRoute {

    override fun targetClassPath(): String {
        return "ru.surfstudio.standard.f_feed.FeedFragmentView"
    }
}
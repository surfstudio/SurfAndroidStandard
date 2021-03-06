package ru.surfstudio.android.navigation.sample.app.screen.main.gallery

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute

class GalleryTabRoute : FragmentRoute(), TabHeadRoute {
    override fun getScreenClass(): Class<out Fragment>? = GalleryTabFragment::class.java
}
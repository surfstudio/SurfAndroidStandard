package ru.surfstudio.android.navigation.sample.app.screen.main.gallery

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute

sealed class GalleryRoute : FragmentRoute() {

    override fun getScreenClass(): Class<out Fragment>? = GalleryFragment::class.java

    class Tab : GalleryRoute(), TabHeadRoute

    class FullScreen : GalleryRoute()
}
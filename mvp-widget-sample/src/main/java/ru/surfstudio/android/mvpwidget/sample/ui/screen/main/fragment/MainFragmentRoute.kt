package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.fragment

import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute

/**
 * Роут главного фрагмента
 */
class MainFragmentRoute : FragmentRoute() {
    override fun getFragmentClass(): Class<out Fragment> = MainFragmentView::class.java
}
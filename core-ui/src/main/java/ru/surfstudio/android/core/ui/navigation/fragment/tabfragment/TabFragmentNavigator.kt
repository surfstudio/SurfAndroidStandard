package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment

import android.os.Bundle
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider

/**
 * Навигатор для фрагментов в табах
 */
class TabFragmentNavigator(val activityProvider: ActivityProvider)
    : FragmentNavigator(activityProvider),
        OnBackPressedDelegate,
        OnRestoreStateDelegate,
        OnSaveStateDelegate {

    private var activeTabRootRoute: RootFragmentRoute? = null
    private val rootFragmetsRoutesSet


    override fun onBackPressed(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveState(outState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
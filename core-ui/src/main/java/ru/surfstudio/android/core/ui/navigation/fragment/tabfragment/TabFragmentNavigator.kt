package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import java.util.*

/**
 * Навигатор для фрагментов в табах
 */
class TabFragmentNavigator(val activityProvider: ActivityProvider)
    : OnBackPressedDelegate,
        OnRestoreStateDelegate,
        OnSaveStateDelegate {

    private var activeTabTag: String? = null
    private val rootFragmentsSet: HashMap<String, Stack<Fragment>> = hashMapOf()
    private val fragmentNavigator: FragmentNavigator = FragmentNavigator(activityProvider)

    fun switchTab(tab: TabType) {

    }

    fun add(fragmentRoute: FragmentRoute, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
        if (fragmentRoute is RootFragmentRoute) {
            fragmentNavigator.add(fragmentRoute, true, transition)
            activeTabTag = fragmentRoute.tag

            val stack = Stack<Fragment>()
            stack.add(fragmentRoute.createFragment())
            rootFragmentsSet[fragmentRoute.tag] = stack
        }
    }


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

enum class TabType {

}

package ru.surfstudio.android.navigation.di

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.tab.view.ViewTabFragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.view.ViewFragmentNavigator

class FragmentNavigationSupplier : FragmentManager.FragmentLifecycleCallbacks() {

    val holders = hashMapOf<Int, MutableList<FragmentNavigatorHolder>>()
    var currentLevel = 1
    var currentId: String? = null

    val currentLevelHolders: List<FragmentNavigatorHolder>?
        get() = holders[currentLevel]

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        val id = getFragmentId(f)

        holders.forEach { entry ->
            val currentHolder = entry.value.find { it.id == id }
            currentHolder?.run {
                fragmentNavigator.onSaveState(outState)
                tabFragmentNavigator.onSaveState(outState)
            }
        }
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        val level = getLevel(f)
        val id = getFragmentId(f)
        val containerId = getContainerId(f) ?: return
        addHolder(level, containerId, id, fm, savedInstanceState)
    }

    private fun addHolder(
            level: Int,
            containerId: Int,
            id: String,
            fm: FragmentManager,
            savedInstanceState: Bundle?
    ) {
        val fragmentNavigator = ViewFragmentNavigator(fm, containerId, savedInstanceState)
        val tabFragmentNavigator = ViewTabFragmentNavigator(fm, containerId, savedInstanceState)
        val newHolder = FragmentNavigatorHolder(id, fragmentNavigator, tabFragmentNavigator)
        val levelHolders = holders[level] ?: mutableListOf()
        levelHolders.add(newHolder)
        holders[level] = levelHolders
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        val id = getFragmentId(f)
        if (id == currentId) {
            currentId = null
        }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        val id = getFragmentId(f)
        val currentLevelHolders = holders[currentLevel]
        val existsOnCurrentLevel = currentLevelHolders?.find { it.id == id } != null
        if (existsOnCurrentLevel) {
            currentId = id
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        removeHolder(f)
    }

    private fun removeHolder(fragment: Fragment) {
        val id = getFragmentId(fragment)
        holders.forEach { entry ->
            val holder = entry.value.find { it.id == id }
            entry.value.remove(holder)
        }
    }

    private fun getLevel(fragment: Fragment): Int {
        var level = 0
        var currentFragment: Fragment? = fragment
        do {
            level++
            currentFragment = currentFragment?.parentFragment
        } while (currentFragment?.parentFragment != null)
        return level
    }

    private fun getFragmentId(fragment: Fragment): String {
        return if (fragment is IdentifiableScreen) fragment.screenId else fragment.id.toString()
    }

    private fun getContainerId(fragment: Fragment): Int? {
        return if (fragment is FragmentContainer) fragment.containerId else null
    }
}
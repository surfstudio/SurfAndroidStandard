package ru.surfstudio.android.navigation.provider.callbacks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.provider.container.FragmentNavigationContainer
import ru.surfstudio.android.navigation.provider.holder.FragmentNavigationHolder
import ru.surfstudio.android.navigation.provider.FragmentNavigationProvider
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigator

//@PerAct
open class FragmentNavigationProviderCallbacks(
        activity: AppCompatActivity,
        savedState: Bundle?
) : FragmentManager.FragmentLifecycleCallbacks(), FragmentNavigationProvider {

    /**
     * Holders with fragment navigators for each [FragmentNavigationContainer]
     */
    private val navigationHolders = hashMapOf<String, FragmentNavigationHolder>()

    init {
        addZeroLevelHolder(activity, savedState)
    }

    /**
     * List of active (created and not yet destroyed) fragments
     */
    private val activeFragments = mutableListOf<Fragment>()

    override fun provide(sourceTag: String): FragmentNavigationHolder {
        val sourceFragment = activeFragments.find { getFragmentId(it) == sourceTag }
        return obtainFragmentHolderRecursive(sourceFragment) ?: navigationHolders.values.first()
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        val id = getFragmentId(f)
        require(activeFragments.all { getFragmentId(it) != id }) { "You must specify unique tag for each fragment!" }
        activeFragments.add(f)
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        val id = getFragmentId(f)
        val containerId = getContainerId(f) ?: return
        addHolder(id, containerId, f.childFragmentManager, savedInstanceState)
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        val id = getFragmentId(f)

        navigationHolders[id]?.run {
            fragmentNavigator.onSaveState(outState)
            tabFragmentNavigator.onSaveState(outState)
        }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        navigationHolders.remove(getFragmentId(f))
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        activeFragments.remove(f)
    }

    fun onActivitySaveState(outState: Bundle?) {
        navigationHolders[FragmentNavigationCommand.ACTIVITY_NAVIGATION_TAG]?.run {
            fragmentNavigator.onSaveState(outState)
            tabFragmentNavigator.onSaveState(outState)
        }
    }

    /**
     * Добавление холдера на 0-ой уровень, т.е. на уровень Activity, которая управляет фрагментами.
     */
    private fun addZeroLevelHolder(activity: FragmentActivity, savedInstanceState: Bundle?) {
        if (activity !is FragmentNavigationContainer) return

        val fragmentManager = activity.supportFragmentManager
        val containerId = activity.containerId
        val id = FragmentNavigationCommand.ACTIVITY_NAVIGATION_TAG

        addHolder(id, containerId, fragmentManager, savedInstanceState)
    }

    /**
     * Добавление холдера с навигаторами в список холдеров на определенный уровень
     *
     * @param level уровень вложенности, на который будет добавлен холдер
     * @param containerId идентификатор контейнера, в котором будет происходить навигация
     * @param id уникальный идентификатор фрагмента
     * @param fm менеджер, который будет осуществлять навигацию
     * @param savedInstanceState состояние, используемое для восстановления бекстека
     */
    private fun addHolder(
            id: String,
            containerId: Int,
            fm: FragmentManager,
            savedInstanceState: Bundle?
    ) {
        val oldHolder = navigationHolders[id]
        require(oldHolder == null) { "You must specify unique tag for each FragmentNavigationContainer!" }
        navigationHolders[id] = createHolder(id, containerId, fm, savedInstanceState)
    }

    protected open fun createHolder(
            id: String,
            containerId: Int,
            fm: FragmentManager,
            savedInstanceState: Bundle?
    ): FragmentNavigationHolder {
        val fragmentNavigator = FragmentNavigator(fm, containerId, savedInstanceState)
        val tabFragmentNavigator = TabFragmentNavigator(fm, containerId, savedInstanceState)
        return FragmentNavigationHolder(fragmentNavigator, tabFragmentNavigator)
    }

    private fun obtainFragmentHolderRecursive(fragment: Fragment?): FragmentNavigationHolder? {
        if (fragment == null) return null
        val id = getFragmentId(fragment)
        val fragmentHolder = navigationHolders[id]
        return fragmentHolder ?: obtainFragmentHolderRecursive(fragment.parentFragment)
    }


    private fun getFragmentId(fragment: Fragment): String {
        return fragment.tag ?: error("Fragment tag must always be specified!")
    }

    private fun getContainerId(fragment: Fragment): Int? {
        return if (fragment is FragmentNavigationContainer) fragment.containerId else null
    }
}
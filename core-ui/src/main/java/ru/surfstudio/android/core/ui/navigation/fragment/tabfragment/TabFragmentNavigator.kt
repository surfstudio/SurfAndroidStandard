package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import org.json.JSONArray
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.core.ui.navigation.Navigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.logger.Logger
import java.util.*

/**
 * Навигатор для фрагментов в табах
 */
class TabFragmentNavigator(val activityProvider: ActivityProvider)
    : Navigator,
        OnBackPressedDelegate,
        OnRestoreStateDelegate,
        OnSaveStateDelegate {

    private val EXTRA_CURRENT_TAB_TAG: String = TabFragmentNavigator::class.toString() + "CURRENT_TAB_TAG"

    private val EXTRA_CURRENT_FRAGMENT: String = TabFragmentNavigator::class.toString() + "CURRENT_FRAGMENT_TAG"

    private val EXTRA_FRAGMENT_STACK: String = TabFragmentNavigator::class.toString() + "FRAGMENT_STACK"

    private var activeTabTag: String? = null
    private val fragmentMap: HashMap<String, Stack<Fragment>> = hashMapOf()
    private val fragmentNavigator: FragmentNavigator = FragmentNavigator(activityProvider)
    private val fragmentManager get() = activityProvider.get().supportFragmentManager
    private val activeStack get() = fragmentMap[activeTabTag] ?: Stack<Fragment>()

    private val currentFragment get() = activeStack.peek()

    /**
     * Показ фрагмента
     */
    fun show(route: FragmentRoute) {
        if (route is RootFragmentRoute) {
            showRoot(route)
        } else {
            showChild(route)
        }

    }

    /**
     * Переход к фрагменту на конкретном табе
     */
    fun showAtTab(tabRoute: FragmentRoute, fragmentRoute: FragmentRoute, clearStack: Boolean = false) {
        showRoot(tabRoute)
        showChild(fragmentRoute)
    }

    private fun showChild(route: FragmentRoute) {
        //todo обработка обычного фрагмента при текущем активном руте
        //todo проверить есть ли рутовый фрагмент, если нет -> обрабатываем с помощью обычного fragmentNavigator

        val fragment = route.createFragment()

        if (fragmentMap.isEmpty()) {
            fragmentNavigator.add(route, false, 0)
        } else {
            if (activeStack.contains(fragment)) {
                clearStackTo(fragment)
            } else {
                addToStack(fragment, route)
            }
        }
    }

    fun replace(fragmentRoute: FragmentRoute) {
        if (activeStack.isNotEmpty()) {
            activeStack.pop()
        }
        val fragment = fragmentRoute.createFragment()
        replace(fragment, fragmentRoute.tag)
        activeStack.push(fragment)
    }


    fun clearStackTo(fragment: Fragment?) {
        activeStack.takeLast(activeStack.lastIndex - activeStack.indexOf(fragment))
                .forEach {
                    remove(activeStack.pop().tag)
                }
    }

    private fun addToStack(fragment: Fragment, route: FragmentRoute) {
        add(fragment, route.tag, true)
        activeStack.push(fragment)
    }

    private fun showRoot(route: FragmentRoute) {
        if (fragmentMap.keys.contains(route.tag)) {
            //открывает существующий таб (+ проверка на активность таба -> ex. сброс стека при повторном выборе)
            if (activeTabTag != route.tag) {
                reShow(route.tag)
            } else {
                // do nothing
                //todo callback-лямбда на действие при повторном открытии активного таба
            }
        } else {
            addRoot(route) //добавляем фрагмент в мапу
        }
    }

    /**
     * Добавление рутового фрагмента
     */
    private fun addRoot(fragmentRoute: FragmentRoute, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
        val fragment = fragmentRoute.createFragment()
        add(fragment, fragmentRoute.tag)
        activeTabTag = fragmentRoute.tag


        val stack = Stack<Fragment>()
        stack.add(fragment)
        fragmentMap[fragmentRoute.tag] = stack
    }

    private fun reShow(routeTag: String) {
        val stack = fragmentMap[activeTabTag]
        show(stack?.peek()?.tag)
        activeTabTag = routeTag

        fragmentMap.keys.filter { it != activeTabTag }
                .forEach {
                    hide(it)
                }
    }

    private fun add(fragment: Fragment, routeTag: String, stackable: Boolean = false, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
        val viewContainerId = getViewContainerIdOrThrow()
        fragmentManager.executePendingTransactions()

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(viewContainerId, fragment, routeTag)
        fragmentTransaction.setTransition(transition)
        if (stackable) {
            fragmentTransaction.addToBackStack(routeTag)
        }

        fragmentTransaction.commit()
    }

    private fun replace(fragment: Fragment, routeTag: String, stackable: Boolean = false, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE) {
        val viewContainerId = getViewContainerIdOrThrow()
        fragmentManager.executePendingTransactions()

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(viewContainerId, fragment, routeTag)
        fragmentTransaction.setTransition(transition)
        if (stackable) {
            fragmentTransaction.addToBackStack(routeTag)
        }

        fragmentTransaction.commit()
    }

    private fun remove(routeTag: String?, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_CLOSE): Boolean {
        fragmentManager.executePendingTransactions()

        val fragment = fragmentManager.findFragmentByTag(routeTag) ?: return false

        fragmentManager.beginTransaction()
                .setTransition(transition)
                .remove(fragment)
                .commit()

        return true
    }

    private fun show(routeTag: String?): Boolean {
        return toggleVisibility(routeTag, true)
    }

    private fun hide(routeTag: String?): Boolean {
        return toggleVisibility(routeTag, false)
    }


    private fun toggleVisibility(routeTag: String?, show: Boolean, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE): Boolean {
        fragmentManager.executePendingTransactions()

        val fragment = fragmentManager.findFragmentByTag(routeTag) ?: return false

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setTransition(transition)
        if (show) {
            fragmentTransaction.show(fragment)
        } else {
            fragmentTransaction.hide(fragment)
        }

        fragmentTransaction.commit()
        return true
    }

    @IdRes
    protected fun getViewContainerIdOrThrow(): Int {
        val contentContainerView = activityProvider.get()
        if (contentContainerView is FragmentContainer) {
            val viewContainerId = (contentContainerView as FragmentContainer).contentContainerViewId
            if (viewContainerId > 0) {
                return viewContainerId
            }
        }

        throw IllegalStateException("Container has to have a ContentViewContainer " + "implementation in order to make fragment navigation")
    }

    override fun onBackPressed(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSaveState(outState: Bundle?) {
        Logger.i("onSaveState TabFragmentNavigator bundle = $outState")
        if (outState != null) onSaveInstanceState(outState)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        Logger.i("onRestoreState TabFragmentNavigator bundle = $savedInstanceState")
        restoreFromBundle(savedInstanceState)
    }

    private fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_CURRENT_TAB_TAG, activeTabTag)
        if (currentFragment != null) {
            outState.putString(EXTRA_CURRENT_FRAGMENT, currentFragment.tag)
        }

        try {
            val stackArrays = JSONArray()
            val iterator = fragmentMap.iterator()

            while (iterator.hasNext()) {
                val stack = iterator.next() as Stack<*>
                val stackArray = JSONArray()
                val stackIterator = stack.iterator()

                while (stackIterator.hasNext()) {
                    val fragment = stackIterator.next() as Fragment
                    stackArray.put(fragment.tag)
                }

                stackArrays.put(stackArray)
            }

            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString())
        } catch (t: Throwable) {
            Logger.e(t)
        }

    }

    private fun restoreFromBundle(savedInstanceState: Bundle?): Boolean {
        if (savedInstanceState == null) {
            return false
        } else {
            try {
                val stackArrays = JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK))

                var x: Int = 0
                while (x < stackArrays.length()) {
                    val stackArray = stackArrays.getJSONArray(x)
                    val stack = Stack<Fragment>()
                    if (stackArray.length() != 1) {
                        (0 until stackArray.length())
                                .map { stackArray.getString(it) }
                                .filter { it != null && !"null".equals(it, ignoreCase = true) }
                                .mapNotNull { fragmentManager.findFragmentByTag(it) }
                                .forEach { stack.push(it) }
                    } else {
                        val tag = stackArray.getString(0)
                        val fragment = fragmentManager.findFragmentByTag(tag)

                        if (fragment != null) {
                            stack.add(fragment)
                        }
                    }

                    fragmentMap[stack.firstElement().tag ?: ""] = stack
                    ++x
                }


                val savedCurrentTabTag = savedInstanceState.getString(EXTRA_CURRENT_TAB_TAG)
                show(savedCurrentTabTag)
                return true
            } catch (t: Throwable) {
                Logger.e(t)
                return false
            }

        }
    }
}

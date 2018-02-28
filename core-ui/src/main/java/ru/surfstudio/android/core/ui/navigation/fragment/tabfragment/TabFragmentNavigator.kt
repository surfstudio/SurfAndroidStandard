package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import org.json.JSONArray
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
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
class TabFragmentNavigator(val activityProvider: ActivityProvider,
                           private val eventDelegateManager: ScreenEventDelegateManager)
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
    private val activeStack: Stack<Fragment>
        get() {
            Logger.d("2222 activeStack = ${fragmentMap[activeTabTag]?.joinToString {
                it.tag ?: "null"
            }}")
            return fragmentMap[activeTabTag] ?: Stack<Fragment>()
        }

    private val currentFragment get() = if (!activeStack.empty()) activeStack.peek() else null
    private val currentRoot get() = activeStack.firstElement()

    init {
        eventDelegateManager.registerDelegate(this)
    }

    /**
     * Показ фрагмента
     */
    fun open(route: FragmentRoute) {
        Logger.d("22222 open fragment ${route.tag}")
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

    fun replace(fragmentRoute: FragmentRoute) {
        if (activeStack.isNotEmpty()) {
            activeStack.pop()
        }
        val fragment = fragmentRoute.createFragment()
        replace(fragment, fragmentRoute.tag)
        activeStack.push(fragment)
    }

    fun clearStack() {
        clearStackTo(activeStack.firstElement())
    }

    fun clearStackTo(fragment: Fragment?) {
        Logger.d("2222 clearStackTo $fragment | idx = ${activeStack.indexOf(fragment)} | last idx = ${activeStack.lastIndex}")
        activeStack.takeLast(activeStack.lastIndex - activeStack.indexOf(fragment))
                .forEach {
                    remove(activeStack.pop().tag)
                }

        hideAnotherFragnemts()
    }

    private fun addToStack(fragment: Fragment, route: FragmentRoute) {
        Logger.d("2222 add fragment to stack ${route.tag} ")
        currentRoot.childFragmentManager
        add(fragment, route.tag, true)
        activeStack.push(fragment)
    }

    private fun showRoot(route: FragmentRoute) {
        Logger.d("2222 map : ${fragmentMap.keys.joinToString()}")
        Logger.d("2222 contains this route ${fragmentMap.keys.contains(route.tag)}")
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

    @SuppressLint("WrongConstant")
    private fun showChild(route: FragmentRoute) {
        val fragment = route.createFragment()

        if (fragmentMap.isEmpty()) {
            fragmentNavigator.add(route, false, FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        } else {
            if (activeStack.contains(fragment)) {
                clearStackTo(fragment)
            } else {
                addToStack(fragment, route)
            }
        }
    }

    /**
     * Добавление рутового фрагмента
     */
    private fun addRoot(fragmentRoute: FragmentRoute, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
        val fragment = fragmentRoute.createFragment()
        activeTabTag = fragmentRoute.tag
        add(fragment, fragmentRoute.tag, false, transition)

        val stack = Stack<Fragment>()
        stack.push(fragment)
        fragmentMap[fragmentRoute.tag] = stack

        hideAnotherFragnemts()
    }

    private fun reShow(routeTag: String) {
        Logger.d("2222 reshow existent fragment $routeTag | prev active tag = $activeTabTag")
        activeTabTag = routeTag
        show(activeStack.peek()?.tag)

        hideAnotherFragnemts()
    }

    private fun hideAnotherFragnemts() {
        fragmentMap.keys.filter { it != activeTabTag }
                .forEach {
                    fragmentMap[it]?.forEach {
                        hide(it.tag)
                    }
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
        Logger.i("2222 onBackPressed ${activeStack.joinToString()}")

        if (activeStack.size <= 1) return false
        remove(activeStack.pop().tag)
        show(activeStack.peek().tag)

        Logger.i("2222 onBackPressed ${activeStack.joinToString()}")
        //todo прокинуть реакцию

        return true
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

        currentFragment?.let {
            outState.putString(EXTRA_CURRENT_FRAGMENT, it.tag)
        }

        try {
            val stackArrays = JSONArray()

            fragmentMap.forEach { (_, stack) ->
                val stackArray = JSONArray()
                val stackIterator = stack.iterator()

                while (stackIterator.hasNext()) {
                    val fragment = stackIterator.next() as Fragment
                    stackArray.put(fragment.tag)
                }

                stackArrays.put(stackArray)
            }

            Logger.d("2222 StackArray ${stackArrays.toString(4)}")
            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString())
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    private fun restoreFromBundle(savedInstanceState: Bundle?): Boolean {
        if (savedInstanceState == null) {
            return false
        } else {
            return try {
                val stackArrays = JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK))

                var x = 0
                while (x < stackArrays.length()) {
                    val stackArray = stackArrays.getJSONArray(x)
                    val stack = Stack<Fragment>()
                    val tagList = mutableListOf<String>()

                    (0 until stackArray.length())
                            .map { stackArray.getString(it) }
                            .filter { it != null && !"null".equals(it, ignoreCase = true) }
                            .onEach { tagList.add(it) }
                            .mapNotNull { fragmentManager.findFragmentByTag(it) }
                            .forEach { stack.push(it) }

                    fragmentMap[tagList.first()] = stack
                    ++x
                }

                Logger.i("2222 after restore map = ${fragmentMap.toString()}")

                val savedCurrentTabTag = savedInstanceState.getString(EXTRA_CURRENT_TAB_TAG)
                reShow(savedCurrentTabTag)

                true
            } catch (t: Throwable) {
                Logger.e(t)
                false
            }

        }
    }
}

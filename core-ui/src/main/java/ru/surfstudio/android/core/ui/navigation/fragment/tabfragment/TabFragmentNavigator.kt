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
open class TabFragmentNavigator(val activityProvider: ActivityProvider,
                           eventDelegateManager: ScreenEventDelegateManager)
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
            Logger.i("2222 activeStack = ${fragmentMap[activeTabTag]?.joinToString {
                it.tag ?: "null"
            }}")
            return fragmentMap[activeTabTag] ?: Stack()
        }

    private val activeTagsStack get() = activeStack.map { it.tag }
    private val currentFragment get() = if (!activeStack.empty()) activeStack.peek() else null
    private val currentRoot get() = activeStack.firstElement()

    init {
        eventDelegateManager.registerDelegate(this)
    }

    /**
     * Показ фрагмента
     */
    fun open(route: FragmentRoute) {
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
        popStack(activeStack.size - 1)
    }

    fun clearStackTo(route: FragmentRoute) {
        val popDepth = activeTagsStack.lastIndex - activeTagsStack.indexOf(route.tag)
        popStack(popDepth)
    }

    private fun popStack(popDepth: Int = 0) {
        activeStack.takeLast(popDepth)
                .forEach {
                    Logger.d("1111 ${it.tag}")
                    remove(activeStack.pop().tag)
                    show(activeStack.peek().tag)
                }

        hideAnotherFragments()
    }

    private fun addToStack(route: FragmentRoute) {
        val fragment = route.createFragment()

        Logger.d("2222 add fragment to stack ${route.tag} ")
        add(fragment, route.tag, true)
        activeStack.push(fragment)
    }

    private fun showRoot(route: FragmentRoute) {
        if (fragmentMap.keys.contains(route.tag)) {
            //открывает существующий таб (+ проверка на активность таба -> ex. сброс стека при повторном выборе)
            if (activeTabTag != route.tag) {
                showExistent(route.tag)
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
        if (fragmentMap.isEmpty()) {
            fragmentNavigator.add(route, false, FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        } else {
            if (activeTagsStack.contains(route.tag)) {
                clearStackTo(route)
            } else {
                addToStack(route)
            }
        }
    }

    /**
     * Добавление рутового фрагмента
     */
    private fun addRoot(fragmentRoute: FragmentRoute,
                        transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
        val fragment = fragmentRoute.createFragment()
        activeTabTag = fragmentRoute.tag
        add(fragment, fragmentRoute.tag, false, transition)

        val stack = Stack<Fragment>()
        stack.push(fragment)
        fragmentMap[fragmentRoute.tag] = stack

        hideAnotherFragments()
    }

    private fun showExistent(routeTag: String) {
        activeTabTag = routeTag
        show(activeStack.peek().tag)

        hideAnotherFragments()
    }

    private fun hideAnotherFragments() {
        fragmentMap.keys.filter { it != activeTabTag }
                .forEach {
                    fragmentMap[it]?.forEach {
                        hide(it.tag)
                    }
                }
    }

    private fun restoreStacksWithShowUpper(currentUpTag: String) {
        fragmentMap.forEach {(_, stack) ->
                    stack.forEach{
                        Logger.d("32323 ${it.tag} ${show(it.tag)}")

                    }
                }
        showExistent(currentUpTag)
    }
    private fun add(fragment: Fragment,
                    routeTag: String,
                    stackable: Boolean = false,
                    transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
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

    private fun replace(fragment: Fragment,
                        routeTag: String,
                        stackable: Boolean = false,
                        transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE) {
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
            fragmentTransaction.attach(fragment)
        } else {
            fragmentTransaction.detach(fragment)
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
        popStack(1)

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
                showExistent(savedCurrentTabTag)

                true
            } catch (t: Throwable) {
                Logger.e(t)
                false
            }

        }
    }
}

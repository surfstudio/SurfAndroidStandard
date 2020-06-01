/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.json.JSONArray
import ru.surfstudio.android.core.ui.FragmentContainer
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.back.OnBackPressedDelegate
import ru.surfstudio.android.core.ui.event.back.OnBackPressedEvent
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.core.ui.navigation.Navigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute
import ru.surfstudio.android.core.ui.navigation.fragment.route.RootFragmentRoute
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

    private val fragmentNavigator: FragmentNavigator = FragmentNavigator(activityProvider)

    private var activeTabTag: String? = null
    private val fragmentMap: HashMap<String, Stack<Fragment>> = hashMapOf()

    private val fragmentManager get() = activityProvider.get().supportFragmentManager
    private val activeStack get() = fragmentMap[activeTabTag] ?: Stack()
    private val activeTagsStack get() = activeStack.map { it.tag }
    private val currentFragment get() = if (!activeStack.empty()) activeStack.peek() else null

    private val onBackPressEventSubject = PublishSubject.create<OnBackPressedEvent>()
    val backPressedEventObservable: Observable<OnBackPressedEvent> get() = onBackPressEventSubject

    private val activeTabReOpenSubject = PublishSubject.create<Unit>()
    val activeTabReOpenObservable: Observable<Unit> get() = activeTabReOpenSubject

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
     * @param clearStack флаг очистки стека под открывающимся фрагментом
     */
    fun <T> showAtTab(tabRoute: T, fragmentRoute: FragmentRoute, clearStack: Boolean = false)
            where T : FragmentRoute,
                  T : RootFragmentRoute {
        showRoot(tabRoute)
        //чистим стек под тем фрагментом, который открываем на табе
        if (clearStack) {
            clearStack()
        }

        showChild(fragmentRoute)
    }

    /**
     * Замена текущего фрагмента новым c заменой в стеке
     */
    fun replace(fragmentRoute: FragmentRoute) {
        if (activeStack.isNotEmpty()) {
            activeStack.pop()
        }
        val fragment = fragmentRoute.createFragment()
        replace(fragment, fragmentRoute.tag)
        activeStack.push(fragment)
    }

    /**
     * Чистит стек выбранных табов, и показывает активный
     */
    fun <T> clearTabs(vararg routes: T) where T : FragmentRoute, T : RootFragmentRoute {
        for (r in routes) {
            fragmentMap[r.tag]
                    ?.drop(1) //пропускаем корневой таб
                    ?.forEach {
                        fragmentMap[r.tag]?.pop()
                        replace(activeStack.firstElement(), activeTabTag)
                    }
        }
    }

    /**
     * Чистит стек активного таба
     */
    fun clearStack() {
        popStack(activeStack.size - 1)
    }

    /**
     * Очистка активного стека до определенного фрагмента по его роуту
     */
    fun clearStackTo(route: FragmentRoute) {
        if (!activeTagsStack.contains(route.tag)) {
            throw IllegalStateException("активный таб  $activeTabTag не содержит такого фрагмента ${route.tag}")
        }

        val popDepth = activeTagsStack.lastIndex - activeTagsStack.indexOf(route.tag)

        popStack(popDepth)
    }

    /**
     * Очистка стека на определенную глубину
     * @param popDepth глубина, на которую надо чистить стек (по умолчанию на один фрагмент)
     */
    private fun popStack(popDepth: Int = 1) {
        if (popDepth < 0) return
        activeStack.takeLast(popDepth)
                .forEach {
                    remove(activeStack.pop().tag)
                }
    }

    /**
     * Добавляет фрагмент в стек активного таба
     */
    private fun addToStack(route: FragmentRoute) {
        val fragment = route.createFragment()

        replace(fragment, route.tag)
        activeStack.push(fragment)
    }

    /**
     * Показывает корневой фрагмент
     */
    private fun showRoot(route: FragmentRoute) {
        if (fragmentMap.keys.contains(route.tag)) {
            //открывает существующий таб (+ проверка на активность таба -> ex. сброс стека при повторном выборе)
            if (activeTabTag != route.tag) {
                showExistent(route.tag)
            } else {
                //Оповещаем об повторном тапе на открытый таб
                activeTabReOpenSubject.onNext(Unit)
            }
        } else {
            addRoot(route) //добавляем фрагмент в мапу
        }
    }

    /**
     * Показывает фрагмент на табе
     */
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
        replace(fragment, fragmentRoute.tag, transition)

        val stack = Stack<Fragment>()
        stack.push(fragment)
        fragmentMap[fragmentRoute.tag] = stack
    }

    /**
     * Показывает существующий фрагмент
     */
    private fun showExistent(routeTag: String) {
        activeTabTag = routeTag
        replace(activeStack.peek(), activeStack.peek().tag)
    }

    private fun replace(fragment: Fragment,
                        routeTag: String?,
                        transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_OPEN) {
        fragmentManager.executePendingTransactions()

        val fragmentTransaction = fragmentManager.beginTransaction()
        detachAll(fragmentTransaction)
        if (isFragmentExistInMap(fragment)) {
            fragmentTransaction.attach(fragment)
        } else {
            add(fragment, routeTag, fragmentTransaction, transition)
        }

        fragmentTransaction.commitAllowingStateLoss()
        //commitAllowingStateLoss жёстко решает проблему редкого непонятного крэша
        //https://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html
    }

    private fun add(fragment: Fragment,
                    routeTag: String?,
                    fragmentTransaction: FragmentTransaction,
                    transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_FADE) {
        val viewContainerId = getViewContainerIdOrThrow()
        fragmentTransaction.add(viewContainerId, fragment, routeTag)
        fragmentTransaction.setTransition(transition)
    }

    private fun detachAll(fragmentTransaction: FragmentTransaction) {
        fragmentMap.forEach { (_, stack) ->
            stack.forEach {
                Logger.i("Detach : ${it.tag}")
                fragmentTransaction.detach(it)
            }
        }
    }

    private fun isFragmentExistInMap(fragment: Fragment): Boolean = fragmentMap.values.any { it.any { it.tag == fragment.tag } }

    private fun remove(routeTag: String?, transition: Int = FragmentTransaction.TRANSIT_FRAGMENT_CLOSE): Boolean {
        fragmentManager.executePendingTransactions()

        val fragment = fragmentManager.findFragmentByTag(routeTag) ?: return false

        fragmentManager.beginTransaction()
                .setTransition(transition)
                .remove(fragment)
                .commitAllowingStateLoss()
        //commitAllowingStateLoss жёстко решает проблему редкого непонятного крэша
        //https://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit-state-loss.html

        val fragmentToShow = activeStack.peek()
        replace(fragmentToShow, activeStack.peek().tag)

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
        if (activeStack.size <= 1) {
            onBackPressEventSubject.onNext(OnBackPressedEvent())
        } else {
            popStack()
        }

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

                Logger.i("TabFragmentNavigator restoreFromBundle after restore map = $fragmentMap")

                val savedCurrentTabTag = savedInstanceState.getString(EXTRA_CURRENT_TAB_TAG)

                if (savedCurrentTabTag != null) {
                    showExistent(savedCurrentTabTag)
                } else {
                    Logger.e(NullPointerException("Couldn't get saved current tab tag"))
                }

                true
            } catch (t: Throwable) {
                Logger.e(t)
                false
            }

        }
    }
}

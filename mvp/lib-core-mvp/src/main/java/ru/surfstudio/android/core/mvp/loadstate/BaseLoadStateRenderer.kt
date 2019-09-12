/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
package ru.surfstudio.android.core.mvp.loadstate

import android.view.View
import kotlin.reflect.KClass

/**
 * Базовый класс, от которого предполагается наследовать собственные в проекте
 * хранит Map, в котором каждому стейту соответствует представление
 * хранит список лямбд, которые должны быть выполненными при том или ином стейте
 */
abstract class BaseLoadStateRenderer : LoadStateRendererInterface {

    abstract val defaultState: LoadStateInterface

    private val presentations =
            mutableMapOf<Class<*>, LoadStatePresentation<*>>()

    private val doForStateActions =
            mutableListOf<Pair<
                    (state: LoadStateInterface) -> Boolean,
                    (isSuitableState: Boolean) -> Unit>>()

    private var _currentState: LoadStateInterface? = null
    private val currentState: LoadStateInterface
        get() {
            if (_currentState == null) {
                _currentState = defaultState
            }
            return _currentState!!
        }

    override fun render(loadState: LoadStateInterface?) {
        loadState ?: return

        getPresentation(currentState.javaClass).hideState(currentState, loadState)
        getPresentation(loadState.javaClass).showState(loadState, currentState)
        doForStateActions.forEach { it.second(it.first(loadState)) }

        _currentState = loadState
    }

    /**
     * @return BaseLoadStateRenderer in Fluent interface style
     */
    fun <T : LoadStateInterface> putPresentation(
            loadState: Class<T>,
            presentationStrategy: LoadStatePresentation<T>): BaseLoadStateRenderer {
        presentations[loadState] = presentationStrategy
        return this
    }

    /**
     * @return BaseLoadStateRenderer in Fluent interface style
     */
    fun <T : LoadStateInterface> putPresentation(
            loadState: KClass<T>,
            presentationStrategy: LoadStatePresentation<T>): BaseLoadStateRenderer {
        presentations[loadState.java] = presentationStrategy
        return this
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T : LoadStateInterface> getPresentation(loadStateClass: Class<T>) =
            presentations[loadStateClass] as? LoadStatePresentation<T>
                    ?: throw UnknownLoadStateException(loadStateClass.simpleName)

    @Suppress("UNCHECKED_CAST")
    protected fun <T : LoadStateInterface> getPresentation(loadStateClass: KClass<T>) =
            presentations[loadStateClass.java] as? LoadStatePresentation<T>
                    ?: throw UnknownLoadStateException(loadStateClass.java.simpleName)

    //region forStates

    fun forState(
            loadState: Class<out LoadStateInterface>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null) =
            forStates(
                    listOf(loadState),
                    run,
                    elseRun)

    fun forState(
            loadState: KClass<out LoadStateInterface>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null) =
            forStates(
                    listOf(loadState.java),
                    run,
                    elseRun)

    fun forStates(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null) =
            forStates(
                    listOf(firstLoadState, secondLoadState),
                    run,
                    elseRun)

    fun forStates(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null) =
            forStates(
                    listOf(firstLoadState.java, secondLoadState.java),
                    run,
                    elseRun)

    fun forStates(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null) =
            forStates(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    run,
                    elseRun)

    fun forStates(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null) =
            forStates(
                    listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java),
                    run,
                    elseRun)

    fun forStates(
            loadStates: List<Class<out LoadStateInterface>>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null): BaseLoadStateRenderer {

        val check = { state: LoadStateInterface -> loadStates.contains(state::class.java) }

        val action = { check: Boolean ->
            if (check) {
                run?.invoke()
            } else {
                elseRun?.invoke()
            } ?: Unit
        }

        doForStateActions.add(Pair(check, action))
        return this
    }
    //endregion

    //region doWithCheck

    fun doWithCheck(
            loadState: Class<out LoadStateInterface>,
            action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(loadState), action)
        return this
    }

    fun doWithCheck(
            loadState: KClass<out LoadStateInterface>,
            action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(loadState.java), action)
        return this
    }

    fun doWithCheck(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(firstLoadState, secondLoadState), action)
        return this
    }

    fun doWithCheck(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(firstLoadState.java, secondLoadState.java), action)
        return this
    }

    fun doWithCheck(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(firstLoadState, secondLoadState, thirdLoadState), action)
        return this
    }

    fun doWithCheck(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java), action)
        return this
    }

    fun doWithCheck(loadStates: List<Class<out LoadStateInterface>>,
                    action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doForStateActions.add(Pair(
                { it: LoadStateInterface -> loadStates.contains(it::class.java) },
                { it: Boolean -> action.invoke(it) }))
        return this
    }
    //endregion

    //region setViewsVisibleFor

    fun setViewsVisibleFor(
            loadStates: List<Class<out LoadStateInterface>>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE): BaseLoadStateRenderer {

        setViewsVisibilityFor(loadStates, views, View.VISIBLE, visibilityWhenHidden)
        return this
    }

    fun setViewsVisibleFor(
            loadState: Class<out LoadStateInterface>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(loadState),
                    views,
                    visibilityWhenHidden)

    fun setViewsVisibleFor(
            loadState: KClass<out LoadStateInterface>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(loadState.java),
                    views,
                    visibilityWhenHidden)

    fun setViewsVisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(firstLoadState, secondLoadState),
                    views,
                    visibilityWhenHidden)

    fun setViewsVisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java),
                    views,
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            view: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewVisibleFor(
                    listOf(firstLoadState, secondLoadState),
                    view,
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            view: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewVisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java),
                    view,
                    visibilityWhenHidden)

    fun setViewsVisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    views,
                    visibilityWhenHidden)

    fun setViewsVisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java),
                    views,
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            view: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewVisibleFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    view,
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            view: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewVisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java),
                    view,
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            loadStates: List<Class<out LoadStateInterface>>,
            view: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    loadStates,
                    listOf(view),
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            loadState: Class<out LoadStateInterface>,
            view: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(loadState),
                    listOf(view),
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            loadState: KClass<out LoadStateInterface>,
            view: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(loadState.java),
                    listOf(view),
                    visibilityWhenHidden)
    //endregion

    //region setViewsInvisibleFor

    fun setViewsInvisibleFor(
            loadStates: List<Class<out LoadStateInterface>>,
            views: List<View>): BaseLoadStateRenderer {

        setViewsVisibilityFor(loadStates, views, View.INVISIBLE, View.VISIBLE)
        return this
    }

    fun setViewsInvisibleFor(
            loadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(loadState),
                    views)

    fun setViewsInvisibleFor(
            loadState: KClass<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(loadState.java),
                    views)

    fun setViewsInvisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(firstLoadState, secondLoadState),
                    views)

    fun setViewsInvisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java),
                    views)

    fun setViewInvisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            view: View) =
            setViewInvisibleFor(
                    listOf(firstLoadState, secondLoadState),
                    view)

    fun setViewInvisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            view: View) =
            setViewInvisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java),
                    view)

    fun setViewsInvisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    views)

    fun setViewsInvisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java),
                    views)

    fun setViewInvisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            view: View) =
            setViewInvisibleFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    view)

    fun setViewInvisibleFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            view: View) =
            setViewInvisibleFor(
                    listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java),
                    view)

    fun setViewInvisibleFor(
            loadStates: List<Class<out LoadStateInterface>>,
            view: View) =
            setViewsInvisibleFor(
                    loadStates,
                    listOf(view))

    fun setViewInvisibleFor(
            loadState: Class<out LoadStateInterface>,
            view: View) =
            setViewsInvisibleFor(
                    listOf(loadState),
                    listOf(view))

    fun setViewInvisibleFor(
            loadState: KClass<out LoadStateInterface>,
            view: View) =
            setViewsInvisibleFor(
                    listOf(loadState.java),
                    listOf(view))
    //endregion

    //region setViewsGoneFor

    fun setViewsGoneFor(
            loadStates: List<Class<out LoadStateInterface>>,
            views: List<View>): BaseLoadStateRenderer {

        setViewsVisibilityFor(loadStates, views, View.GONE, View.VISIBLE)
        return this
    }

    fun setViewsGoneFor(
            loadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(loadState),
                    views)

    fun setViewsGoneFor(
            loadState: KClass<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(loadState.java),
                    views)

    fun setViewsGoneFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(firstLoadState, secondLoadState),
                    views)

    fun setViewsGoneFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(firstLoadState.java, secondLoadState.java),
                    views)

    fun setViewGoneFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            view: View) =
            setViewGoneFor(
                    listOf(firstLoadState, secondLoadState),
                    view)

    fun setViewGoneFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            view: View) =
            setViewGoneFor(
                    listOf(firstLoadState.java, secondLoadState.java),
                    view)

    fun setViewsGoneFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    views)

    fun setViewsGoneFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java),
                    views)

    fun setViewGoneFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            view: View) =
            setViewGoneFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    view)

    fun setViewGoneFor(
            firstLoadState: KClass<out LoadStateInterface>,
            secondLoadState: KClass<out LoadStateInterface>,
            thirdLoadState: KClass<out LoadStateInterface>,
            view: View) =
            setViewGoneFor(
                    listOf(firstLoadState.java, secondLoadState.java, thirdLoadState.java),
                    view)

    fun setViewGoneFor(
            loadStates: List<Class<out LoadStateInterface>>,
            view: View) =
            setViewsGoneFor(
                    loadStates,
                    listOf(view))

    fun setViewGoneFor(
            loadState: Class<out LoadStateInterface>,
            view: View) =
            setViewsGoneFor(
                    listOf(loadState),
                    listOf(view))

    fun setViewGoneFor(
            loadState: KClass<out LoadStateInterface>,
            view: View) =
            setViewsGoneFor(
                    listOf(loadState.java),
                    listOf(view))
    //endregion

    private fun setViewsVisibilityFor(
            loadStates: List<Class<out LoadStateInterface>>,
            views: List<View>,
            ifVisibility: Int,
            elseVisibility: Int): BaseLoadStateRenderer {

        forStates(
                loadStates,
                { views.forEach { it.visibility = ifVisibility } },
                { views.forEach { it.visibility = elseVisibility } })

        return this
    }
}
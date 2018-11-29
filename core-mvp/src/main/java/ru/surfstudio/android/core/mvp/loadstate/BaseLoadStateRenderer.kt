package ru.surfstudio.android.core.mvp.loadstate

import android.view.View

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

    @Suppress("UNCHECKED_CAST")
    protected fun <T : LoadStateInterface> getPresentation(loadStateClass: Class<T>) =
            presentations[loadStateClass] as? LoadStatePresentation<T>
                    ?: throw UnknownLoadStateException(loadStateClass.simpleName)

    //region forStates

    fun forState(
            loadState: Class<out LoadStateInterface>,
            run: (() -> Unit)? = null,
            elseRun: (() -> Unit)? = null) =
            forStates(
                    listOf(loadState),
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
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            action: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(firstLoadState, secondLoadState), action)
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
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            views: List<View>,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(firstLoadState, secondLoadState),
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
            loadStates: List<Class<out LoadStateInterface>>,
            views: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    loadStates,
                    listOf(views),
                    visibilityWhenHidden)

    fun setViewVisibleFor(
            loadState: Class<out LoadStateInterface>,
            views: View,
            visibilityWhenHidden: Int = View.INVISIBLE) =
            setViewsVisibleFor(
                    listOf(loadState),
                    listOf(views),
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
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(firstLoadState, secondLoadState),
                    views)

    fun setViewInvisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            view: View) =
            setViewInvisibleFor(
                    listOf(firstLoadState, secondLoadState),
                    view)

    fun setViewsInvisibleFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsInvisibleFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
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
            loadStates: List<Class<out LoadStateInterface>>,
            views: View) =
            setViewsInvisibleFor(
                    loadStates,
                    listOf(views))

    fun setViewsInvisibleFor(
            loadState: Class<out LoadStateInterface>,
            views: View) =
            setViewsInvisibleFor(
                    listOf(loadState),
                    listOf(views))
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
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(firstLoadState, secondLoadState),
                    views)

    fun setViewGoneFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            view: View) =
            setViewGoneFor(
                    listOf(firstLoadState, secondLoadState),
                    view)

    fun setViewsGoneFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            views: List<View>) =
            setViewsGoneFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
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
            loadStates: List<Class<out LoadStateInterface>>,
            views: View) =
            setViewsGoneFor(
                    loadStates,
                    listOf(views))

    fun setViewsGoneFor(
            loadState: Class<out LoadStateInterface>,
            views: View) =
            setViewsGoneFor(
                    listOf(loadState),
                    listOf(views))
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


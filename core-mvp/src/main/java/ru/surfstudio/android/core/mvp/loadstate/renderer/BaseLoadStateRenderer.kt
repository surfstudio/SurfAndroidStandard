package ru.surfstudio.android.core.mvp.loadstate.renderer

import android.view.View
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

abstract class BaseLoadStateRenderer : LoadStateRendererInterface {

    private val strategies = mutableMapOf<Class<*>, LoadStatePresentation<*>>()

    private var currentState: LoadStateInterface? = null

    abstract fun getDefaultState(): LoadStateInterface

    override fun render(loadState: LoadStateInterface) {

        //state to hide
        getPresentation(getCurrentState().javaClass).apply {
            hideLoadStatePresentation(getCurrentState(), loadState)
            afterThisStateActions.forEach { it.invoke() }
        }

        //state to show
        getPresentation(loadState.javaClass).apply {
            showLoadStatePresentation(getCurrentState(), loadState)
            thisStateActions.forEach { it.invoke() }
        }

        currentState = loadState
    }

    /**
     * @return BaseLoadStateRenderer in Fluent interface style
     */
    fun <T : LoadStateInterface> putPresentation(
            loadState: Class<T>,
            presentationStrategy: LoadStatePresentation<T>): BaseLoadStateRenderer {
        strategies[loadState] = presentationStrategy
        return this
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T : LoadStateInterface> getPresentation(loadStateClass: Class<T>) =
            strategies[loadStateClass] as? LoadStatePresentation<T>
                    ?: throw UnknownLoadStateException(loadStateClass.simpleName)

    private fun getCurrentState(): LoadStateInterface {
        if (currentState == null) {
            currentState = getDefaultState()
        }
        return currentState!!
    }

    //region forStates

    fun forState(
            loadState: Class<out LoadStateInterface>,
            ifDo: (() -> Unit)? = null,
            elseDo: (() -> Unit)? = null) =
            forStates(
                    listOf(loadState),
                    ifDo,
                    elseDo)

    fun forStates(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            ifDo: (() -> Unit)? = null,
            elseDo: (() -> Unit)? = null) =
            forStates(
                    listOf(firstLoadState, secondLoadState),
                    ifDo,
                    elseDo)

    fun forStates(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            ifDo: (() -> Unit)? = null,
            elseDo: (() -> Unit)? = null) =
            forStates(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    ifDo,
                    elseDo)

    fun forStates(
            loadStates: List<Class<out LoadStateInterface>>,
            ifDo: (() -> Unit)? = null,
            elseDo: (() -> Unit)? = null): BaseLoadStateRenderer {

        loadStates.forEach { loadStateClass ->
            getPresentation(loadStateClass).apply {
                ifDo?.let { thisStateActions.add(it) }
                elseDo?.let { afterThisStateActions.add(it) }
            }
        }
        return this
    }
    //endregion

    //region doWithCheck
    fun doWithCheck(
            loadState: Class<out LoadStateInterface>,
            actionWithCheck: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(loadState), actionWithCheck)
        return this
    }

    fun doWithCheck(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            actionWithCheck: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(firstLoadState, secondLoadState), actionWithCheck)
        return this
    }

    fun doWithCheck(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            actionWithCheck: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        doWithCheck(listOf(firstLoadState, secondLoadState, thirdLoadState), actionWithCheck)
        return this
    }

    fun doWithCheck(loadStates: List<Class<out LoadStateInterface>>,
                    actionWithCheck: (check: Boolean) -> Unit): BaseLoadStateRenderer {

        loadStates.forEach { loadStateClass ->
            getPresentation(loadStateClass).apply {
                thisStateActions.add { actionWithCheck.invoke(true) }
                afterThisStateActions.add { actionWithCheck.invoke(false) }
            }
        }
        return this
    }
    //endregion

    //region setViewsVisibilityFor
    fun setViewsVisibilityFor(
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

    fun setViewsVisibilityFor(
            loadState: Class<out LoadStateInterface>,
            views: List<View>,
            ifVisibility: Int,
            elseVisibility: Int) =
            setViewsVisibilityFor(
                    listOf(loadState),
                    views,
                    ifVisibility,
                    elseVisibility)

    fun setViewsVisibilityFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            views: List<View>,
            ifVisibility: Int,
            elseVisibility: Int) =
            setViewsVisibilityFor(
                    listOf(firstLoadState, secondLoadState),
                    views,
                    ifVisibility,
                    elseVisibility)

    fun setViewVisibilityFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            view: View,
            ifVisibility: Int,
            elseVisibility: Int) =
            setViewVisibilityFor(
                    listOf(firstLoadState, secondLoadState),
                    view,
                    ifVisibility,
                    elseVisibility)

    fun setViewsVisibilityFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            views: List<View>,
            ifVisibility: Int,
            elseVisibility: Int) =
            setViewsVisibilityFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    views,
                    ifVisibility,
                    elseVisibility)

    fun setViewVisibilityFor(
            firstLoadState: Class<out LoadStateInterface>,
            secondLoadState: Class<out LoadStateInterface>,
            thirdLoadState: Class<out LoadStateInterface>,
            view: View,
            ifVisibility: Int,
            elseVisibility: Int) =
            setViewVisibilityFor(
                    listOf(firstLoadState, secondLoadState, thirdLoadState),
                    view,
                    ifVisibility,
                    elseVisibility)

    fun setViewVisibilityFor(
            loadStates: List<Class<out LoadStateInterface>>,
            views: View,
            ifVisibility: Int,
            elseVisibility: Int) =
            setViewsVisibilityFor(
                    loadStates,
                    listOf(views),
                    ifVisibility,
                    elseVisibility)

    fun setViewVisibilityFor(
            loadState: Class<out LoadStateInterface>,
            views: View,
            ifVisibility: Int,
            elseVisibility: Int) =
            setViewsVisibilityFor(
                    listOf(loadState),
                    listOf(views),
                    ifVisibility,
                    elseVisibility)
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
}


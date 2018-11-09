package ru.surfstudio.android.core.mvp.loadstate.renderer

import android.view.View
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

abstract class BaseLoadStateRenderer : LoadStateRendererInterface {

    private val strategies = mutableMapOf<Class<*>, LoadStatePresentationStrategy<*>>()

    private val thisStateActionsMap = mutableMapOf<Class<out LoadStateInterface>, MutableList<() -> Unit>>()
    private val notThisStateActionsList = mutableListOf<Pair<(state: LoadStateInterface) -> Boolean, () -> Unit>>()

    override fun render(loadState: LoadStateInterface) {
        getStrategy(loadState.javaClass).renderLoadState(loadState)

        thisStateActionsMap[loadState::class.java]?.forEach { it.invoke() }
        notThisStateActionsList.forEach { if (it.first.invoke(loadState)) it.second.invoke() }
    }


    /**
     * @return BaseLoadStateRenderer in Fluent interface style
     */
    fun <T : LoadStateInterface> putStrategy(
            loadState: Class<T>,
            presentationStrategy: LoadStatePresentationStrategy<T>): BaseLoadStateRenderer {
        strategies[loadState] = presentationStrategy
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : LoadStateInterface> getStrategy(loadStateClass: Class<T>) =
            strategies[loadStateClass] as? LoadStatePresentationStrategy<T>
                    ?: throw UnknownLoadStateException(loadStateClass.simpleName)

    fun addStateAction(
            loadStates: List<Class<out LoadStateInterface>>,
            thisStateAction: (() -> Unit)? = null,
            notThisStateAction: (() -> Unit)? = null): BaseLoadStateRenderer {

        thisStateAction?.let {
            loadStates.forEach { state ->
                thisStateActionsMap.getOrPut(state) { mutableListOf(it) }.add(it)
            }
        }

        notThisStateAction?.let {
            notThisStateActionsList.add(Pair(
                    { state: LoadStateInterface -> loadStates.contains(state::class.java).not() },
                    it))
        }
        return this
    }

    fun addStateAction(
            state: Class<out LoadStateInterface>,
            thisStateAction: (() -> Unit)? = null,
            notThisStateAction: (() -> Unit)? = null) =
            addStateAction(
                    listOf(state),
                    thisStateAction,
                    notThisStateAction)

    fun setViewsVisibilityForState(
            loadStates: List<Class<out LoadStateInterface>>,
            views: List<View>,
            thisStatesVisibility: Int,
            otherStatesVisibility: Int): BaseLoadStateRenderer {

        addStateAction(
                loadStates,
                { views.forEach { it.visibility = thisStatesVisibility } },
                { views.forEach { it.visibility = otherStatesVisibility } })

        return this
    }

    fun setViewsVisibilityForState(
            loadState: Class<out LoadStateInterface>,
            views: List<View>,
            thisStatesVisibility: Int,
            otherStatesVisibility: Int) =
            setViewsVisibilityForState(
                    listOf(loadState),
                    views,
                    thisStatesVisibility,
                    otherStatesVisibility)

    fun setViewsVisibilityForState(
            loadStates: List<Class<out LoadStateInterface>>,
            views: View,
            thisStatesVisibility: Int,
            otherStatesVisibility: Int) =
            setViewsVisibilityForState(
                    loadStates,
                    listOf(views),
                    thisStatesVisibility,
                    otherStatesVisibility)

    fun setViewsVisibilityForState(
            loadState: Class<out LoadStateInterface>,
            views: View,
            thisStatesVisibility: Int,
            otherStatesVisibility: Int) =
            setViewsVisibilityForState(
                    listOf(loadState),
                    listOf(views),
                    thisStatesVisibility,
                    otherStatesVisibility)
}


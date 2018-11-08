package ru.surfstudio.android.core.mvp.loadstate.renderer

import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

abstract class BaseLoadStateRenderer : LoadStateRendererInterface {

    private val strategies = mutableMapOf<Class<*>, LoadStatePresentationStrategy<*>>()

    private val actionsMap = mutableMapOf<Class<out LoadStateInterface>, MutableList<() -> Unit>>()
    private val ifNotActionList = mutableListOf<Pair<(state: LoadStateInterface) -> Boolean, () -> Unit>>()

    /**
     * @return BaseLoadStateRenderer in Fluent interface style
     */
    fun <T : LoadStateInterface> putStrategy(
            loadState: Class<T>,
            presentationStrategy: LoadStatePresentationStrategy<T>): BaseLoadStateRenderer {
        strategies[loadState] = presentationStrategy
        return this
    }

    fun addStateAction(
            states: List<Class<out LoadStateInterface>>,
            action: () -> Unit = {},
            ifNotAction: () -> Unit = {}): BaseLoadStateRenderer {

        states.forEach {
            actionsMap.getOrPut(it) { mutableListOf(action) }.add(action)
        }

        val predicate = { it: LoadStateInterface -> states.contains(it::class.java).not() }
        ifNotActionList.add(Pair(predicate, ifNotAction))

        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : LoadStateInterface> getStrategy(loadStateClass: Class<T>) =
            strategies[loadStateClass] as? LoadStatePresentationStrategy<T>
                    ?: throw UnknownLoadStateException(loadStateClass.simpleName)

    override fun render(loadState: LoadStateInterface) {
        getStrategy(loadState.javaClass).renderLoadState(loadState)

        actionsMap[loadState::class.java]?.forEach { it.invoke() }
        ifNotActionList.forEach { if (it.first.invoke(loadState)) it.second.invoke() }
    }
}
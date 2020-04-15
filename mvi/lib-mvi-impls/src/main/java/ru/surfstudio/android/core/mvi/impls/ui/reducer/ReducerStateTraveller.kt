package ru.surfstudio.android.core.mvi.impls.ui.reducer

import ru.surfstudio.android.core.mvi.impls.BuildConfig
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.logger.Logger

/**
 * Experimental mechanism for travelling between states in `MVI-Reducer` approach.
 */
object ReducerStateTraveller : StateEmitter {

    private val stateMap: HashMap<String, MutableList<TravelEntry<*>>?> = hashMapOf()

    private val isEnabled: Boolean = BuildConfig.DEBUG

    /**
     * Добавление стейта и холдера в список для хранения
     */
    fun <S : Any> accept(state: S, holder: State<S>) {
        if (!isEnabled) return
        val holderName = extractHolderName(holder)
        val list = stateMap[holderName] ?: mutableListOf()
        list.add(TravelEntry(state, holder))
        stateMap[holderName] = list
    }

    /**
     * Очистка хранилища холдера с именем holderName
     */
    fun erase(holderName: String) {
        if (!isEnabled) return
        stateMap[holderName] = null
    }

    /**
     * Очистка хранилища холдера
     */
    fun erase(holder: State<*>) {
        if (!isEnabled) return
        erase(extractHolderName(holder))
    }

    /**
     * Перемещение по стейтам
     *
     * @param holderName имя холдера, для которого переключаем стейт
     * @param index индекс стейта
     */
    fun travel(holderName: String, index: Int) {
        if (!isEnabled) return
        val list = stateMap[holderName] ?: return
        val entry = list.getOrNull(index) ?: return
        val holder = entry.holder as State<Any>
        holder.accept(entry.state)
    }

    /**
     * Печать всех стейтов из Reducer
     */
    fun print(holderName: String) {
        if (!isEnabled) return
        val list = stateMap[holderName] ?: return
        list.forEachIndexed { index, entry -> print(index, entry.state) }
    }

    private fun print(index: Int, state: Any) {
        if (!isEnabled) return
        Logger.d("State #$index: $state")
    }

    private fun extractHolderName(holder: State<*>) = holder::class.java.simpleName

    private data class TravelEntry<S : Any>(val state: S, val holder: State<S>)
}

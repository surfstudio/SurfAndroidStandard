package ru.surfstudio.standard.ui.placeholder.loadstate.state

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class ErrorLoadState(val action: () -> Unit = {}) : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is ErrorLoadState
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
package ru.surfstudio.standard.base_ui.loadstate.state

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class MainLoadingState : LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is MainLoadingState
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
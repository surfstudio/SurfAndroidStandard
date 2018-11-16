package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states

import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface

class CustomLoadState() : LoadStateInterface{
    override fun equals(other: Any?): Boolean {
        return other is CustomLoadState
    }
}
package ru.surfstudio.android.sample.common.ui.base.loadstate

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class NoneLoadState : LoadStateInterface
class ErrorLoadState : LoadStateInterface
class EmptyLoadState : LoadStateInterface
class MainLoadingState : LoadStateInterface

object LoadState {
    val NONE = NoneLoadState()
    val MAIN_LOADING = MainLoadingState()
    val ERROR = ErrorLoadState()
    val EMPTY = EmptyLoadState()
}
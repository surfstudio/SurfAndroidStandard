package ru.surfstudio.standard.base_ui.loadstate.state

object LoadState {
    val none = NoneState()
    val empty = EmptyLoadState()
    val error = ErrorLoadState()
    val mainLoading = MainLoadingState()
    val transparentLoading = TransparentLoadingState()
}
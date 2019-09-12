package ru.surfstudio.standard.ui.placeholder

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class NoneLoadState : LoadStateInterface
class ErrorLoadState : LoadStateInterface
class EmptyLoadState : LoadStateInterface
class NotFoundLoadState : LoadStateInterface
class MainLoadingState : LoadStateInterface
class TransparentLoadingState : LoadStateInterface
class NoInternetLoadState : LoadStateInterface

object LoadState {
    val NONE = NoneLoadState()
    val MAIN_LOADING = MainLoadingState()
    val TRANSPARENT_LOADING = TransparentLoadingState()
    val ERROR = ErrorLoadState()
    val EMPTY = EmptyLoadState()
    val NOT_FOUND = NotFoundLoadState()
    val NO_INTERNET = NoInternetLoadState()
}
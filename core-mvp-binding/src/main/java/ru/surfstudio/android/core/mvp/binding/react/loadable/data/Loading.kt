package ru.surfstudio.android.core.mvp.binding.react.loadable.data

interface Loading {
    val isLoading: Boolean
}

class MainLoading(override val isLoading: Boolean) : Loading
class TransparentLoading(override val isLoading: Boolean) : Loading
class SwipeRefreshLoading(override val isLoading: Boolean) : Loading
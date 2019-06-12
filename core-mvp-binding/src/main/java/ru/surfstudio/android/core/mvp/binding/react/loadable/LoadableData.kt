package ru.surfstudio.android.core.mvp.binding.react.loadable

import ru.surfstudio.android.core.mvp.binding.react.optional.Optional

data class LoadableData<T>(
        val data: Optional<T> = Optional.Empty,
        val isLoading: Boolean = false,
        val error: Optional<Throwable> = Optional.Empty
)
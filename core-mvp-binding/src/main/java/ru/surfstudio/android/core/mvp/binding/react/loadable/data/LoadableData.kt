package ru.surfstudio.android.core.mvp.binding.react.loadable.data

import ru.surfstudio.android.core.mvp.binding.react.optional.Optional

data class LoadableData<T>(
        val data: Optional<T> = Optional.Empty,
        val load: Loading = MainLoading(false),
        val error: Throwable = EmptyErrorException()
)
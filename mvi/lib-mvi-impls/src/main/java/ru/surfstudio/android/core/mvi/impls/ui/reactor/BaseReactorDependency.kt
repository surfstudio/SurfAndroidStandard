package ru.surfstudio.android.core.mvi.impls.ui.reactor

import ru.surfstudio.android.core.mvp.error.ErrorHandler

/**
 * Dependency for [BaseReactor].
 * */
class BaseReactorDependency(
        val errorHandler: ErrorHandler
)
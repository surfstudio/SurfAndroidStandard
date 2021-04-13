package ru.surfstudio.android.core.mvp.binding.test

import ru.surfstudio.android.core.mvp.error.ErrorHandler

/**
 * Тестовая реализация [ErrorHandler]
 */
class TestErrorHandler : ErrorHandler {

    override fun handleError(err: Throwable?) {
        // do nothing
    }
}

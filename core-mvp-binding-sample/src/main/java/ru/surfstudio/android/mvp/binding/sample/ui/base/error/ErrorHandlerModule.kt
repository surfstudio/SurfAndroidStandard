package ru.surfstudio.android.mvp.binding.sample.ui.base.error

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.sample.ui.base.error.StandardErrorHandler

@Module
class ErrorHandlerModule {

    @Provides
    @PerScreen
    internal fun provideNetworkErrorHandler(standardErrorHandler: StandardErrorHandler): ErrorHandler {
        return standardErrorHandler
    }
}

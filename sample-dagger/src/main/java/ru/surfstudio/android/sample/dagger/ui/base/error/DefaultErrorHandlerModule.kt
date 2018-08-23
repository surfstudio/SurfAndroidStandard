package ru.surfstudio.android.sample.dagger.ui.base.error

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.dagger.scope.PerScreen

@Module
class DefaultErrorHandlerModule {

    @Provides
    @PerScreen
    internal fun provideNetworkErrorHandler(standardErrorHandler: DefaultStandardErrorHandler): ErrorHandler {
        return standardErrorHandler
    }
}

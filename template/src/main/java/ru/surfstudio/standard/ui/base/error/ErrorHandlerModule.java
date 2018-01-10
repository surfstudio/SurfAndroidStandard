package ru.surfstudio.standard.ui.base.error;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.error.ErrorHandler;

@Module
public class ErrorHandlerModule {

    @Provides
    @PerScreen
    ErrorHandler provideNetworkErrorHandler(StandardErrorHandler standardErrorHandler){
        return standardErrorHandler;
    }
}

package ru.surfstudio.android.navigation.sample_standard.dagger

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.bus.ScreenResultBus
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.navigation.observer.storage.ScreenResultStorage
import ru.surfstudio.android.navigation.observer.storage.file.FileScreenResultStorage
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.sample_standard.App

@Module
class AppNavigationModule {

    @Provides
    @PerApplication
    fun provideScreenResultStorage(context: Context): ScreenResultStorage {
        val filesDir = ContextCompat.getNoBackupFilesDir(context)?.absolutePath ?: ""
        return FileScreenResultStorage(filesDir)
    }

    @Provides
    @PerApplication
    fun provideScreenResultBus(storage: ScreenResultStorage): ScreenResultBus {
        return ScreenResultBus(storage)
    }

    @Provides
    @PerApplication
    fun provideScreenResultObserver(screenResultBus: ScreenResultBus): ScreenResultObserver {
        return screenResultBus
    }

    @Provides
    @PerApplication
    fun provideScreenResultEmitter(screenResultBus: ScreenResultBus): ScreenResultEmitter {
        return screenResultBus
    }

    @Provides
    @PerApplication
    fun provideActivityNavigationProvider(
            activityNavigationProviderCallbacks: ActivityNavigationProviderCallbacks
    ): ActivityNavigationProvider {
        return activityNavigationProviderCallbacks
    }


    @Provides
    @PerApplication
    fun provideActivityNavigationProviderCallbacks(context: Context): ActivityNavigationProviderCallbacks {
        return ActivityNavigationProviderCallbacks()
    }

    @Provides
    @PerApplication
    fun provideAppCommandExecutor(
            screenResultEmitter: ScreenResultEmitter,
            activityNavigationProvider: ActivityNavigationProvider
    ): AppCommandExecutor {
        return AppCommandExecutorWithResult(screenResultEmitter, activityNavigationProvider)
    }
}
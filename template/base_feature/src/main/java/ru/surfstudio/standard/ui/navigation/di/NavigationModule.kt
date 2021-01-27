package ru.surfstudio.standard.ui.navigation.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.utils.AppDirectoriesProvider
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.observer.executor.screen.activity.ActivityCommandWithResultExecutor
import ru.surfstudio.android.navigation.executor.screen.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.fragment.FragmentCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.bus.ScreenResultBus
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.navigation.observer.storage.ScreenResultStorage
import ru.surfstudio.android.navigation.observer.storage.file.FileScreenResultStorage
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks

@Module
class NavigationModule {

    @Provides
    @PerApplication
    fun provideScreenResultStorage(context: Context): ScreenResultStorage {
        val filesDir = AppDirectoriesProvider.provideNoBackupStorageDir(context)
        return FileScreenResultStorage(filesDir)
    }

    @Provides
    @PerApplication
    fun provideScreenResultBus(storage: ScreenResultStorage): ScreenResultBus {
        return ScreenResultBus(storage)
    }

    @Provides
    @PerApplication
    fun provideActivityNavigationProviderCallbacks(context: Context): ActivityNavigationProviderCallbacks {
        return ActivityNavigationProviderCallbacks()
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
    fun provideAppCommandExecutor(
            screenResultEmitter: ScreenResultEmitter,
            activityNavigationProvider: ActivityNavigationProvider
    ): AppCommandExecutor {
        return AppCommandExecutorWithResult(
                screenResultEmitter,
                activityNavigationProvider,
                ActivityCommandWithResultExecutor(activityNavigationProvider, screenResultEmitter),
                FragmentCommandExecutor(activityNavigationProvider),
                DialogCommandExecutor(activityNavigationProvider)
        )
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
}

package ru.surfstudio.standard.application.app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.core.ui.provider.resource.ResourceProviderImpl
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult
import ru.surfstudio.android.shared.pref.NO_BACKUP_SHARED_PREF
import javax.inject.Named

@Module
class AppModule(
    private val app: Application,
    private val activeActivityHolder: ActiveActivityHolder
) {

    @Provides
    @PerApplication
    internal fun provideActiveActivityHolder(): ActiveActivityHolder = activeActivityHolder

    @Provides
    @PerApplication
    internal fun provideContext(): Context = app

    @Provides
    @PerApplication
    internal fun provideApp(): Application = app

    @Provides
    @PerApplication
    internal fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Provides
    @PerApplication
    internal fun provideGlobalNavigator(
        context: Context,
        activityHolder: ActiveActivityHolder
    ): GlobalNavigator {
        return GlobalNavigator(context, activityHolder)
    }

    @Provides
    @PerApplication
    internal fun provideSchedulerProvider(): SchedulersProvider = SchedulersProviderImpl()

    @Provides
    @PerApplication
    internal fun provideConnectionQualityProvider(context: Context): ConnectionProvider {
        return ConnectionProvider(context)
    }

    @Provides
    @PerApplication
    internal fun providePermissionManager(
        activeActivityHolder: ActiveActivityHolder,
        appCommandExecutorWithResult: AppCommandExecutorWithResult,
        @Named(NO_BACKUP_SHARED_PREF) sharedPreferences: SharedPreferences,
        screenResultObserver: ScreenResultObserver
    ): PermissionManager {
        return PermissionManager(
            activeActivityHolder,
            appCommandExecutorWithResult,
            sharedPreferences,
            screenResultObserver
        )
    }
}
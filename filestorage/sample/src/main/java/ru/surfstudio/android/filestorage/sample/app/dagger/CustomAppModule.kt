package ru.surfstudio.android.filestorage.sample.app.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigatorImpl
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.filestorage.sample.app.CustomApp
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl
import ru.surfstudio.android.core.ui.provider.resource.ResourceProvider
import ru.surfstudio.android.core.ui.provider.resource.ResourceProviderImpl

@Module
class CustomAppModule(private val coreApp: CustomApp) {

    @PerApplication
    @Provides
    internal fun provideActiveActivityHolder(): ActiveActivityHolder {
        return coreApp.activeActivityHolder
    }

    @PerApplication
    @Provides
    internal fun provideContext(): Context {
        return coreApp
    }

    @PerApplication
    @Provides
    internal fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @PerApplication
    @Provides
    internal fun provideGlobalNavigator(context: Context,
                                        activityHolder: ActiveActivityHolder): GlobalNavigator {
        return GlobalNavigatorImpl(context, activityHolder)
    }

    @Provides
    @PerApplication
    internal fun provideSchedulerProvider(): SchedulersProvider {
        return SchedulersProviderImpl()
    }

    @Provides
    @PerApplication
    internal fun provideConnectionQualityProvider(context: Context): ConnectionProvider {
        return ConnectionProvider(context)
    }
}
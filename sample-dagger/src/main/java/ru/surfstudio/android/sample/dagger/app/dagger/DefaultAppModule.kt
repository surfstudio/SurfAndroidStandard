package ru.surfstudio.android.sample.dagger.app.dagger

import android.app.Application
import android.content.Context

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.activity.holder.ActiveActivityHolder
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl
import ru.surfstudio.android.sample.dagger.ui.base.StringsProvider

@Module
class DefaultAppModule(
        private val app: Application,
        private val activeActivityHolder: ActiveActivityHolder
) {

    @PerApplication
    @Provides
    internal fun provideActiveActivityHolder(): ActiveActivityHolder = activeActivityHolder

    @PerApplication
    @Provides
    internal fun provideContext(): Context = app

    @PerApplication
    @Provides
    internal fun provideStringsProvider(context: Context): StringsProvider = StringsProvider(context)

    @PerApplication
    @Provides
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
}

package ru.surfstudio.standard.f_debug.injector

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.standard.base.util.StringsProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl

@Module
class DebugAppModule(
        private val app: Application,
        private val activeActivityHolder: ru.surfstudio.android.activity.holder.ActiveActivityHolder
) {

    @PerApplication
    @Provides
    internal fun provideActiveActivityHolder(): ru.surfstudio.android.activity.holder.ActiveActivityHolder {
        return activeActivityHolder
    }

    @PerApplication
    @Provides
    internal fun provideContext(): Context {
        return app
    }

    @PerApplication
    @Provides
    internal fun provideApplication(): Application {
        return app
    }

    @PerApplication
    @Provides
    internal fun provideStringsProvider(context: Context): StringsProvider {
        return StringsProvider(context)
    }

    @PerApplication
    @Provides
    internal fun provideGlobalNavigator(context: Context,
                                        activityHolder: ru.surfstudio.android.activity.holder.ActiveActivityHolder): GlobalNavigator {
        return GlobalNavigator(context, activityHolder)
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
package ru.surfstudio.standard.app_injector

import android.content.ClipboardManager
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.app.ActiveActivityHolder
import ru.surfstudio.android.core.app.CoreApp
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.ui.navigation.activity.navigator.GlobalNavigator
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProviderImpl

@Module
class AppModule(private val coreApp: CoreApp) {

    @Provides
    @PerApplication
    fun provideActiveActivityHolder(): ActiveActivityHolder {
        return coreApp.activeActivityHolder
    }

    @Provides
    @PerApplication
    fun provideContext(): Context {
        return coreApp
    }

    @Provides
    @PerApplication
    internal fun provideStringsProvider(context: Context): StringsProvider {
        return StringsProvider(context)
    }

    @Provides
    @PerApplication
    fun provideGlobalNavigator(context: Context,
                               activityHolder: ActiveActivityHolder): GlobalNavigator {
        return GlobalNavigator(context, activityHolder)
    }

    @Provides
    @PerApplication
    fun provideSchedulerProvider(): SchedulersProvider {
        return SchedulersProviderImpl()
    }

    @Provides
    @PerApplication
    fun provideClipboardManager(context: Context): ClipboardManager {
        return context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
}
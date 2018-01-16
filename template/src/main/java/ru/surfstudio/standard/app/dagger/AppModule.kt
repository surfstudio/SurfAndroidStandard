package ru.surfstudio.standard.app.dagger

import android.content.Context

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.app.dagger.CoreAppModule
import ru.surfstudio.android.core.app.dagger.scope.PerApplication

@Module(includes = [(CoreAppModule::class)])
class AppModule(private val appContext: Context) {

    @Provides
    @PerApplication
    internal fun provideContext(): Context {
        return appContext
    }
}

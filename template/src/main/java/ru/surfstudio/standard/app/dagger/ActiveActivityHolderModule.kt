package ru.surfstudio.standard.app.dagger

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.util.ActiveActivityHolder
import ru.surfstudio.android.dagger.scope.PerApplication

@Module
class ActiveActivityHolderModule(private val activeActivityHolder: ActiveActivityHolder) {

    @Provides
    @PerApplication
    internal fun provideActiveActivityHolder(): ActiveActivityHolder {
        return activeActivityHolder
    }
}

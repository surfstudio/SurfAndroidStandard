package ru.surfstudio.standard.app.dagger

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.app.dagger.scope.PerApplication
import ru.surfstudio.android.core.util.ActiveActivityHolder

@Module
class ActiveActivityHolderModule(private val activeActivityHolder: ActiveActivityHolder) {

    @Provides
    @PerApplication
    internal fun provideActiveActivityHolder(): ActiveActivityHolder {
        return activeActivityHolder
    }
}

package ru.surfstudio.standard.app.dagger;

import dagger.Module;
import dagger.Provides;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.util.ActiveActivityHolder;

@Module
public class ActiveActivityHolderModule {
    private ActiveActivityHolder activeActivityHolder;

    public ActiveActivityHolderModule(ActiveActivityHolder activeActivityHolder) {
        this.activeActivityHolder = activeActivityHolder;
    }

    @Provides
    @PerApplication
    ActiveActivityHolder provideActiveActivityHolder() {
        return activeActivityHolder;
    }
}

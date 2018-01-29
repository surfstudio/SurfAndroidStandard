package ru.surfstudio.android.core.ui.base.dagger;

import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.base.screen.state.ActivityScreenState;

/**
 * Created by makstuev on 29.01.2018.
 */

public interface BaseCoreActivityComponent {

    ActivityProvider activityProvider();

    ActivityPersistentScope activityPersistentScope();

    ActivityScreenState activityScreenState();
}

package ru.surfstudio.android.core.ui.base.screen.event;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.surfstudio.android.core.ui.base.screen.event.back.OnBackPressedEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.completely.destroy.OnCompletelyDestroyEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.destroy.OnDestroyEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.pause.OnPauseEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.ready.OnViewReadyEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.resume.OnResumeEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.start.OnStartEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state.OnRestoreStateEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.state.OnSaveStateStateEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.stop.OnStopEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.lifecycle.view.destroy.OnViewDestroyEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.newintent.NewIntentEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.result.ActivityResultEventResolver;
import ru.surfstudio.android.core.ui.base.screen.event.result.RequestPermissionsResultEventResolver;

/**
 * содержит список стандартных {@link ScreenEventResolver}
 */
public class ScreenEventResolverHelper {

    public static List<ScreenEventResolver> standardEventResolvers(){
        return new ArrayList<>(Arrays.asList(
                new ActivityResultEventResolver(),
                new NewIntentEventResolver(),
                new RequestPermissionsResultEventResolver(),
                new OnRestoreStateEventResolver(),
                new OnSaveStateStateEventResolver(),
                new OnViewReadyEventResolver(),
                new OnStartEventResolver(),
                new OnResumeEventResolver(),
                new OnPauseEventResolver(),
                new OnStopEventResolver(),
                new OnViewDestroyEventResolver(),
                new OnDestroyEventResolver(),
                new OnCompletelyDestroyEventResolver(),
                new OnBackPressedEventResolver()
        ));
    }
}

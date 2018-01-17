package ru.surfstudio.android.core.ui.base.event.delegate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.surfstudio.android.core.ui.base.event.delegate.activity.result.ActivityResultEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.back.OnBackPressedEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.completely.destroy.OnCompletelyDestroyEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.activity.OnCreateActivityEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.common.OnCreateEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.create.fragment.OnCreateFragmentEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.destroy.OnDestroyEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.ready.OnViewReadyEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.resume.OnResumeEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.start.OnStartEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnRestoreStateEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.state.OnSaveStateStateEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.stop.OnStopEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.lifecycle.view.destroy.OnViewDestroyEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.newintent.NewIntentEventResolver;
import ru.surfstudio.android.core.ui.base.event.delegate.permission.result.RequestPermissionsResultEventResolver;

public class ScreenEventResolverHelper {

    public static List<ScreenEventResolver> standardEventResolvers(){
        return new ArrayList<ScreenEventResolver>(Arrays.asList(
                new ActivityResultEventResolver(),
                new NewIntentEventResolver(),
                new RequestPermissionsResultEventResolver(),
                new OnCreateEventResolver(),
                new OnCreateActivityEventResolver(),
                new OnCreateFragmentEventResolver(),
                new OnViewReadyEventResolver(),
                new OnRestoreStateEventResolver(),
                new OnSaveStateStateEventResolver(),
                new OnStartEventResolver(),
                new OnResumeEventResolver(),
                new OnViewReadyEventResolver(),
                new OnStopEventResolver(),
                new OnViewDestroyEventResolver(),
                new OnDestroyEventResolver(),
                new OnCompletelyDestroyEventResolver(),
                new OnBackPressedEventResolver()
        ));
    }
}

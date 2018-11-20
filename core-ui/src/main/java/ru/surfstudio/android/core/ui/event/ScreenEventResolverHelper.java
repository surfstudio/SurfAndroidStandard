/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.core.ui.event;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.surfstudio.android.core.ui.event.back.OnBackPressedEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateStateEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopEventResolver;
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyEventResolver;
import ru.surfstudio.android.core.ui.event.newintent.NewIntentEventResolver;
import ru.surfstudio.android.core.ui.event.result.ActivityResultEventResolver;
import ru.surfstudio.android.core.ui.event.result.RequestPermissionsResultEventResolver;

/**
 * содержит список стандартных {@link ScreenEventResolver}
 */
public class ScreenEventResolverHelper {

    public static List<ScreenEventResolver> standardEventResolvers() {
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
                new OnCompletelyDestroyEventResolver(),
                new OnBackPressedEventResolver()
        ));
    }
}

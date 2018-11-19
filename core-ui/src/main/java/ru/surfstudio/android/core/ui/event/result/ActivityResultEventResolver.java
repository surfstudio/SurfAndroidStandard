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
package ru.surfstudio.android.core.ui.event.result;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.ScreenEventResolverHelper;
import ru.surfstudio.android.core.ui.event.base.ScreenEvent;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.SingleScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.Storeable;

/**
 * см {@link ScreenEventResolver}
 */
public class ActivityResultEventResolver extends SingleScreenEventResolver<ActivityResultEvent, ActivityResultDelegate>
        implements Storeable<ActivityResultDelegate, ActivityResultEvent> {

    private ActivityResultEvent unhandledEvent;

    @Override
    public Class<ActivityResultDelegate> getDelegateType() {
        return ActivityResultDelegate.class;
    }

    @Override
    public Class<ActivityResultEvent> getEventType() {
        return ActivityResultEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_AND_FRAGMENT_TYPES;
    }

    @Override
    protected boolean resolve(ActivityResultDelegate delegate, ActivityResultEvent event) {
        return tryToResolve(delegate, event);
    }

    @NotNull
    @Override
    public List<ScreenEvent> getStoredEvents() {
        List<ScreenEvent> events = new ArrayList<>();
        events.add(unhandledEvent);
        return events;
    }

    @Override
    public boolean tryToResolve(@NotNull ActivityResultDelegate delegate, @NotNull ActivityResultEvent event) {
        boolean resolved = delegate.onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());

        if (!resolved) {
            ScreenEventResolverHelper.storedEvents.add(event);
        } else {
            ScreenEventResolverHelper.storedEvents.remove(event);
        }

        return resolved;
    }
}

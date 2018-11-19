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


import java.util.ArrayList;
import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.SingleScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class ActivityResultEventResolver extends SingleScreenEventResolver<ActivityResultEvent, ActivityResultDelegate> {

    //буффер неразрешенных событий
    private List<ActivityResultEvent> storedEvents = new ArrayList<>();

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
        boolean resolved = delegate.onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());

        if(!resolved) {
            storedEvents.add(event);
        } else {
            storedEvents.remove(event);
        }
        return resolved;
    }

    public List<ActivityResultEvent> getStoredEvents() {
        return storedEvents;
    }
}

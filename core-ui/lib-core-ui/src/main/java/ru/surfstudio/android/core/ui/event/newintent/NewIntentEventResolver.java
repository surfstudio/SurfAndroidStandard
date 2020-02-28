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
package ru.surfstudio.android.core.ui.event.newintent;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.SingleScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class NewIntentEventResolver extends SingleScreenEventResolver<NewIntentEvent, NewIntentDelegate> {
    @Override
    public Class<NewIntentDelegate> getDelegateType() {
        return NewIntentDelegate.class;
    }

    @Override
    public Class<NewIntentEvent> getEventType() {
        return NewIntentEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ACTIVITY_TYPES;
    }

    @Override
    protected boolean resolve(NewIntentDelegate delegate, NewIntentEvent event) {
        return delegate.onNewIntent(event.getIntent());
    }
}

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
package ru.surfstudio.android.core.ui.event.lifecycle.state;


import java.util.List;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.event.base.resolver.MultipleScreenEventResolver;
import ru.surfstudio.android.core.ui.event.base.resolver.ScreenEventResolver;

/**
 * см {@link ScreenEventResolver}
 */
public class OnSaveStateStateEventResolver extends MultipleScreenEventResolver<OnSaveStateEvent, OnSaveStateDelegate, Void> {


    @Override
    public Class<OnSaveStateDelegate> getDelegateType() {
        return OnSaveStateDelegate.class;
    }

    @Override
    public Class<OnSaveStateEvent> getEventType() {
        return OnSaveStateEvent.class;
    }

    @Override
    public List<ScreenType> getEventEmitterScreenTypes() {
        return ALL_TYPES;
    }


    @Override
    protected Void resolve(OnSaveStateDelegate delegate, OnSaveStateEvent event) {
        delegate.onSaveState(event.getOutState());
        return null;
    }
}

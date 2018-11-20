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
package ru.surfstudio.android.mvp.widget.scope;


import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.event.WidgetLifecycleManager;
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState;

/**
 * {@link PersistentScope} для WidgetView
 */
public class WidgetViewPersistentScope extends ScreenPersistentScope {

    private WidgetLifecycleManager lifecycleManager;

    public WidgetViewPersistentScope(ScreenEventDelegateManager screenEventDelegateManager,
                                     WidgetScreenState screenState,
                                     BaseWidgetViewConfigurator configurator,
                                     String scopeId, WidgetLifecycleManager lifecycleManager) {
        super(screenEventDelegateManager, screenState, configurator, scopeId);
        this.lifecycleManager = lifecycleManager;
    }

    public WidgetLifecycleManager getLifecycleManager() {
        return lifecycleManager;
    }

    @Override
    public BaseWidgetViewConfigurator getConfigurator() {
        return (BaseWidgetViewConfigurator) super.getConfigurator();
    }

    @Override
    public WidgetScreenState getScreenState() {
        return (WidgetScreenState) super.getScreenState();
    }
}

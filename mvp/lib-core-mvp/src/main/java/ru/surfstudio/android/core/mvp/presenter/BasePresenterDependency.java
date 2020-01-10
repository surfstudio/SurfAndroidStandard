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
package ru.surfstudio.android.core.mvp.presenter;

import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.core.mvp.error.ErrorHandler;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider;

/**
 * зависимости базового презентера
 * Обернуты в отдельный обьект чтобы не занимать много места в конструкторах классов наследников {@link BasePresenter}
 */
public class BasePresenterDependency {

    private SchedulersProvider schedulersProvider;
    private ScreenState screenState;
    private ScreenEventDelegateManager eventDelegateManager;

    private ErrorHandler errorHandler;

    private ActivityNavigator activityNavigator;
    private ConnectionProvider connectionProvider;

    public BasePresenterDependency(SchedulersProvider schedulersProvider,
                                   ScreenState screenState,
                                   ScreenEventDelegateManager eventDelegateManager,
                                   ErrorHandler errorHandler,
                                   ConnectionProvider connectionProvider,
                                   ActivityNavigator activityNavigator) {
        this.schedulersProvider = schedulersProvider;
        this.screenState = screenState;
        this.eventDelegateManager = eventDelegateManager;
        this.errorHandler = errorHandler;
        this.connectionProvider = connectionProvider;
        this.activityNavigator = activityNavigator;
    }

    public SchedulersProvider getSchedulersProvider() {
        return schedulersProvider;
    }

    public ScreenEventDelegateManager getEventDelegateManager() {
        return eventDelegateManager;
    }

    public ActivityNavigator getActivityNavigator() {
        return activityNavigator;
    }

    public ConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

    public ScreenState getScreenState() {
        return screenState;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }
}

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
package ru.surfstudio.android.core.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;

import ru.surfstudio.android.core.ui.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.core.ui.scope.HasPersistentScope;

/**
 * интерфейс базовой активити, см {@link ActivityDelegate}
 */
public interface CoreActivityInterface extends
        HasConfigurator,
        HasPersistentScope {

    @Override
    BaseActivityConfigurator createConfigurator();

    @Override
    ActivityPersistentScope getPersistentScope();

    ActivityDelegate createActivityDelegate();

    /**
     * @param viewRecreated render whether view created in first time or recreated after
     *                      changing configuration
     */
    void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState, boolean viewRecreated);

    /**
     * Используется для только логирования (Может быть не уникальным)
     * @return возвращает имя для логгирования
     */
    String getScreenName();
}

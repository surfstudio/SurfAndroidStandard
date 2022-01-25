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
package ru.surfstudio.android.core.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.configurator.HasConfigurator;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope;
import ru.surfstudio.android.core.ui.scope.HasPersistentScope;

/**
 * Интерфес для базового фрагмента, см {@link FragmentDelegate}
 */
public interface CoreFragmentInterface extends
        HasConfigurator,
        HasPersistentScope{

    @Override
    BaseFragmentConfigurator createConfigurator();

    @Override
    FragmentPersistentScope getPersistentScope();

    FragmentDelegate createFragmentDelegate();

    /**
     * @param viewRecreated showSimpleDialog whether view created in first time or recreated after
     *                      changing configuration
     */
    void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState, boolean viewRecreated);

    /**
     * Используется для только логирования (Может быть не уникальным)
     * @return возвращает имя для логов
     */
    String getScreenName();
}

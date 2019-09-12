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
package ru.surfstudio.android.core.mvp.state;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.fragment.CoreFragmentInterface;
import ru.surfstudio.android.core.ui.state.FragmentScreenState;

/**
 * Предоставляет текущее состояние экрана и живой фрагмент - вью
 */

public class FragmentViewScreenState extends FragmentScreenState {
    private CoreFragmentViewInterface coreFragmentView;

    public void onDestroy() {
        super.onDestroy();
        coreFragmentView = null;
    }

    public void onCreate(Fragment fragment, CoreFragmentViewInterface coreFragmentView, @Nullable Bundle savedInstanceState) {
        super.onCreate(fragment, coreFragmentView, savedInstanceState);
        this.coreFragmentView = coreFragmentView;
    }

    @Override
    @Deprecated
    public void onCreate(Fragment fragment, CoreFragmentInterface coreFragment, @Nullable Bundle savedInstanceState) {
        throw new UnsupportedOperationException("call another onCreate");
    }

    public CoreFragmentViewInterface getCoreFragmentView() {
        return coreFragmentView;
    }
}

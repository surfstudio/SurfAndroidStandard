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
package ru.surfstudio.android.core.mvp.delegate.factory;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.delegate.ActivityViewDelegate;
import ru.surfstudio.android.core.mvp.delegate.FragmentViewDelegate;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;

/**
 * Фабрика делегатов MVP экранов, создана для того чтобы была возможность применить некоторую
 * логику ко всем MVP экранам конкретного приложения
 */

public interface MvpScreenDelegateFactory {
    <A extends FragmentActivity & CoreActivityViewInterface> ActivityViewDelegate createActivityViewDelegate(A activity);

    <F extends Fragment & CoreFragmentViewInterface> FragmentViewDelegate createFragmentViewDelegate(F fragment);
}

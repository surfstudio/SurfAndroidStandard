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

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ru.surfstudio.android.core.ui.configurator.BaseFragmentConfigurator;
import ru.surfstudio.android.core.ui.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.delegate.fragment.FragmentDelegate;
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.Logger;

/**
 * базовый фрагмент для всего приложения
 * см {@link FragmentDelegate}
 */
public abstract class CoreFragment extends Fragment implements CoreFragmentInterface {

    private FragmentDelegate fragmentDelegate;

    @Override
    public FragmentDelegate createFragmentDelegate() {
        return ScreenDelegateFactoryContainer.get().createFragmentDelegate(this);
    }

    @Override
    public BaseFragmentConfigurator createConfigurator() {
        //используется базовый конфигуратор
        return new BaseFragmentConfigurator();
    }

    @Override
    public FragmentPersistentScope getPersistentScope() {
        return fragmentDelegate.getPersistentScope();
    }

    @Override
    public String getScreenName() {
        //уникальное имя по умолчанию для фрагмента контейнера
        return this.getClass().getCanonicalName() + getId();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentDelegate = createFragmentDelegate();
        fragmentDelegate.initialize(savedInstanceState);
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentDelegate.onCreate(savedInstanceState, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(String.format(LogConstants.LOG_SCREEN_RESUME_FORMAT, getScreenName()));
        fragmentDelegate.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d(String.format(LogConstants.LOG_SCREEN_RESUME_FORMAT, getScreenName()));
        fragmentDelegate.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        fragmentDelegate.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentDelegate.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDelegate.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        fragmentDelegate.onOnSaveInstantState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragmentDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        fragmentDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

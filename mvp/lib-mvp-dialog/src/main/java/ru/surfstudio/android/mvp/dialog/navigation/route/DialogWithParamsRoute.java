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
package ru.surfstudio.android.mvp.dialog.navigation.route;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/**
 * см {@link DialogRoute}
 */
public abstract class DialogWithParamsRoute extends DialogRoute {

    @Override
    protected abstract Class<? extends DialogFragment> getFragmentClass();

    public abstract Bundle prepareBundle();

    @Override
    public DialogFragment createFragment() {
        DialogFragment fragment = super.createFragment();
        fragment.setArguments(prepareBundle());
        return fragment;
    }
}
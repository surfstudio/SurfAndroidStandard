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


import androidx.fragment.app.DialogFragment;

import ru.surfstudio.android.core.ui.navigation.fragment.route.FragmentRoute;

/**
 * см {@link FragmentRoute}
 */
public abstract class DialogRoute extends FragmentRoute {

    @Override
    protected abstract Class<? extends DialogFragment> getFragmentClass();

    @Override
    public DialogFragment createFragment() {
        return (DialogFragment) super.createFragment();
    }
}

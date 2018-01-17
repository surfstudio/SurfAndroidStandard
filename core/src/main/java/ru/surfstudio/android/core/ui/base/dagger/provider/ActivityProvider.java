/*
 * Copyright 2016 Maxim Tuev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.core.ui.base.dagger.provider;

import android.support.v7.app.AppCompatActivity;

import ru.surfstudio.android.core.ui.base.scope.PersistentScope;


/**
 * Provider for Activity
 * every call {@link this#get()} return actual Activity
 */
public class ActivityProvider extends Provider<AppCompatActivity> {
    public ActivityProvider(PersistentScope screenScope) {
        super(screenScope);
    }

    /**
     * @return actual Activity
     */
    @Override
    public AppCompatActivity get() {
        return (AppCompatActivity) screenScope.getActivity();
    }
}

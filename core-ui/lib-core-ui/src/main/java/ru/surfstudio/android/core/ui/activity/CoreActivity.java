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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ru.surfstudio.android.core.ui.delegate.activity.ActivityDelegate;
import ru.surfstudio.android.core.ui.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.locale.LocaleHelper;
import ru.surfstudio.android.core.ui.scope.ActivityPersistentScope;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.Logger;

/**
 * базовая активити для всего приложения
 * см {@link ActivityDelegate}
 */
public abstract class CoreActivity extends AppCompatActivity implements CoreActivityInterface {

    private ActivityDelegate activityDelegate;

    @Override
    public ActivityDelegate createActivityDelegate() {
        return ScreenDelegateFactoryContainer.get().createActivityDelegate(this);
    }

    @Override
    public ActivityPersistentScope getPersistentScope() {
        return activityDelegate.getPersistentScope();
    }

    @Override
    public String getScreenName() {
        return this.getClass().getCanonicalName();
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        activityDelegate = createActivityDelegate();
        activityDelegate.initialize(savedInstanceState);
        super.onCreate(savedInstanceState);

        onPreCreate(savedInstanceState);
        setContentView(getContentView());
        activityDelegate.onCreate(savedInstanceState, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState,
                         boolean viewRecreated) {

        //empty
    }

    /**
     * @return activity rootView
     */
    protected View getRootView() {
        return ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * @return layout resource of the screen
     */
    @LayoutRes
    abstract protected int getContentView();

    /**
     * Called before Presenter is bound to the View and content view is created
     *
     * @param savedInstanceState
     */
    protected void onPreCreate(Bundle savedInstanceState) {
        //empty
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityDelegate.onStart();
        if (LocaleHelper.INSTANCE.isLocaleChanged(this)) {
            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(String.format(LogConstants.LOG_SCREEN_RESUME_FORMAT, getScreenName()));
        activityDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(String.format(LogConstants.LOG_SCREEN_PAUSE_FORMAT, getScreenName()));
        activityDelegate.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityDelegate.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityDelegate.onDestroyView();
        activityDelegate.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.INSTANCE.localize(newBase));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityDelegate.onOnSaveInstantState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        activityDelegate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        activityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        activityDelegate.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (!activityDelegate.onBackPressed()) { //поддерживается пока только один обработчик
            super.onBackPressed();
        }
    }
}

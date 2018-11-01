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
package ru.surfstudio.android.mvp.widget.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import ru.surfstudio.android.core.mvp.presenter.CorePresenter;
import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate;
import ru.surfstudio.android.mvp.widget.delegate.factory.MvpWidgetDelegateFactoryContainer;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;

/**
 * базовый класс для кастомной вьюшки с презентером, основанном на FrameLayout
 * <p>
 * !!!ВАЖНО!!!
 * Пока нельзя использовать в ресайклере
 */


public abstract class CoreLinearLayoutView extends LinearLayout implements CoreWidgetViewInterface {

    private WidgetViewDelegate widgetViewDelegate;

    public CoreLinearLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoreLinearLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public CoreLinearLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected abstract CorePresenter[] getPresenters();

    @Override
    public void bindPresenters() {
        for (CorePresenter presenter : getPresenters()) {
            presenter.attachView(this);
        }
    }

    @Override
    public WidgetViewDelegate createWidgetViewDelegate() {
        return MvpWidgetDelegateFactoryContainer.get().createWidgetViewDelegate(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected Parcelable onSaveInstanceState() {
        return widgetViewDelegate.onSaveInstanceState();
    }

    @Override
    public Parcelable superSavedInstanceState() {
        return super.onSaveInstanceState();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        widgetViewDelegate.onRestoreState(state);
    }

    @Override
    public void superRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        switch (visibility) {
            case View.INVISIBLE:
            case View.GONE:
                widgetViewDelegate.onPause();
                break;
            case View.VISIBLE:
                widgetViewDelegate.onResume();
                break;
        }
    }

    @Override
    public final void init() {
        widgetViewDelegate = createWidgetViewDelegate();
        widgetViewDelegate.onCreate();
    }

    @Override
    public void onCreate() {
        //empty
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        widgetViewDelegate.onDestroy();
    }

    @Override
    public WidgetViewPersistentScope getPersistentScope() {
        return widgetViewDelegate.getPersistentScope();
    }
}

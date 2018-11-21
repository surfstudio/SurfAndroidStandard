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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import ru.surfstudio.android.core.mvp.presenter.Presenter;
import ru.surfstudio.android.mvp.widget.R;
import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate;
import ru.surfstudio.android.mvp.widget.delegate.factory.MvpWidgetDelegateFactoryContainer;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;

/**
 * базовый класс для кастомной вьюшки с презентером, основанном на FrameLayout
 * <p>
 * !!!ВАЖНО!!!
 * Пока нельзя использовать в ресайклере
 */


public abstract class CoreFrameLayoutView extends FrameLayout implements CoreWidgetViewInterface {

    private WidgetViewDelegate widgetViewDelegate;
    private boolean isManualInitEnabled;

    public CoreFrameLayoutView(Context context, boolean isManualInitEnabled) {
        super(context, null);
        this.isManualInitEnabled = isManualInitEnabled;

        if (!isManualInitEnabled) {
            widgetViewDelegate = createWidgetViewDelegate();
        }
    }

    public CoreFrameLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CoreFrameLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(21)
    public CoreFrameLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        obtainAttrs(attrs);

        if (!isManualInitEnabled) {
            widgetViewDelegate = createWidgetViewDelegate();
        }
    }


    private void obtainAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CoreConstraintLayoutView, -1, -1);
        isManualInitEnabled = ta.getBoolean(R.styleable.CoreConstraintLayoutView_enableManualInit, false);
        ta.recycle();
    }

    protected abstract Presenter[] getPresenters();

    @Override
    public void bindPresenters() {
        for (Presenter presenter : getPresenters()) {
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
        if (!isManualInitEnabled) {
            widgetViewDelegate.onCreate();
        }
    }

    @Override
    public void init() {}

    @Override
    public void init(String scopeId) {
        widgetViewDelegate = createWidgetViewDelegate();
        widgetViewDelegate.setScopeId(scopeId);
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

    /**
     * В ручную вызывает onCompletelyDestroy у виджета
     */
    public void manualCompletelyDestroy() {
        widgetViewDelegate.onCompletelyDestroy();
    }
}

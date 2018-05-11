/*
  Copyright (c) 2018-present, SurfStudio LLC.

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

import android.content.Context;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import ru.surfstudio.android.core.mvp.presenter.CorePresenter;
import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate;
import ru.surfstudio.android.mvp.widget.delegate.factory.MvpWidgetDelegateFactoryContainer;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;

/**
 * базовый класс для кастомной вьюшки с презентером, основанном на FrameLayout
 * <p>
 * !!!ВАЖНО!!!
 * 1) Необходимо вызвать метод init во время onCreate() Activity или onActivityCreated() Fragment
 * 2) кастомная вьюшка с презентером может быть только в статической иерархии вью,
 * то есть должна создаваться при старте экрана, и не может быть использована при
 * динамическом создании вью, в том числе внутри элементов RecyclerView
 */


public abstract class CoreConstraintLayoutView extends ConstraintLayout implements CoreWidgetViewInterface {

    private WidgetViewDelegate widgetViewDelegate;

    public CoreConstraintLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoreConstraintLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract CorePresenter[] getPresenters();

    @Override
    public void bindPresenters() {
        for (CorePresenter presenter : getPresenters()) {
            presenter.attachView(this);
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public WidgetViewDelegate createWidgetViewDelegate() {
        return MvpWidgetDelegateFactoryContainer.get().createWidgetViewDelegate(this);
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

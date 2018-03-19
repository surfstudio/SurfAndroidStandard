package ru.surfstudio.android.mvp.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

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


public abstract class CoreFrameLayoutView extends FrameLayout implements CoreWidgetViewInterface {

    private WidgetViewDelegate widgetViewDelegate;

    public CoreFrameLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoreFrameLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public CoreFrameLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

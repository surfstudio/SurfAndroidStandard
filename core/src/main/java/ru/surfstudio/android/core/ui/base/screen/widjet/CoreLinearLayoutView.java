package ru.surfstudio.android.core.ui.base.screen.widjet;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ru.surfstudio.android.core.ui.base.screen.delegate.factory.ScreenDelegateFactoryContainer;
import ru.surfstudio.android.core.ui.base.screen.delegate.widget.WidgetViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;

/**
 * базовый класс для кастомной вьюшки с презентером, основанном на FrameLayout
 *
 * !!!ВАЖНО!!!
 * 1) Необходимо вызвать метод init во время onCreate() Activity или onActivityCreated() Fragment
 * 2) кастомная вьюшка с презентером может быть только в статической иерархии вью,
 *      то есть должна создаваться при старте экрана, и не может быть использована при
 *      динамическом создании вью, в том числе внутри элементов RecyclerView
 */


public abstract class CoreLinearLayoutView extends LinearLayout implements CoreWidgetViewInterface {

    WidgetViewDelegate widgetViewDelegate;

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
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public WidgetViewDelegate createWidgetViewDelegate() {
        return ScreenDelegateFactoryContainer.get().createWidgetViewDelegate(this);
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
}

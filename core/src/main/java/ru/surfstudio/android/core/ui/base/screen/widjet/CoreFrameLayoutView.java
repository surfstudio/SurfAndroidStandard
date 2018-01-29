package ru.surfstudio.android.core.ui.base.screen.widjet;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import ru.surfstudio.android.core.CoreApp;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.delegate.widget.WidgetViewDelegate;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;

/**
 * Created by makstuev on 29.01.2018.
 */

public abstract class CoreFrameLayoutView extends FrameLayout implements CoreWidgetViewInterface {

    WidgetViewDelegate widgetViewDelegate;

    public CoreFrameLayoutView(Context context) {
        super(context);
    }

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
    public BaseWidgetViewConfigurator getConfigurator() {
        return widgetViewDelegate.getConfigurator();
    }

    @Override
    public WidgetViewDelegate createWidgetViewDelegate() {
        return CoreApp.getScreenDelegateFactory(this.getContext()).createWidgetViewDelegate(this);
    }

    @Override
    public void init() {
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

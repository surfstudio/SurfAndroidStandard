package ru.surfstudio.android.sample.dagger.ui.base.configurator;

import ru.surfstudio.android.core.ui.activity.CoreActivityInterface;
import ru.surfstudio.android.mvp.widget.configurator.BaseWidgetViewConfigurator;
import ru.surfstudio.android.mvp.widget.delegate.ViewContextUnwrapper;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent;
import ru.surfstudio.android.sample.dagger.ui.base.dagger.widget.DefaultWidgetScreenModule;

/**
 * Base configurator for Widget
 */
public abstract class DefaultWidgetScreenConfigurator
        extends BaseWidgetViewConfigurator<DefaultActivityComponent, DefaultWidgetScreenModule> {

    @Override
    protected DefaultWidgetScreenModule getWidgetScreenModule() {
        return new DefaultWidgetScreenModule(getPersistentScope());
    }

    @Override
    protected DefaultActivityComponent getParentComponent() {
        return (DefaultActivityComponent) (
                ViewContextUnwrapper.unwrapContext(
                        getTargetWidgetView().getContext(), CoreActivityInterface.class
                )
        )
                .getPersistentScope()
                .getConfigurator()
                .getActivityComponent();
    }
}

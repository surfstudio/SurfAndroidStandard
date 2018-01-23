package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderActivityCoreView;

/**
 * Базовый класс конфигуратора экрана, основанного на Activity, см {@link ScreenConfigurator}
 *
 * @param <P> родительский даггер компонент
 * @param <M> даггер модуль для активити
 */
public abstract class BaseActivityScreenConfigurator<P, A, M>
        extends BaseActivityConfigurator<A, P>
        implements ScreenConfigurator {

    private PresenterHolderActivityCoreView target;
    private Intent intent;


    public <T extends FragmentActivity & PresenterHolderActivityCoreView> BaseActivityScreenConfigurator(
            T target,
            Intent intent) {
        super(target);
        this.target = target;
        this.intent = intent;
    }

    protected abstract ScreenComponent createScreenComponent(A parentActivityComponent,
                                                             M activityScreenModule,
                                                             CoreActivityScreenModule coreActivityScreenModule,
                                                             Intent intent);

    protected abstract M getActivityScreenModule();

    @Override
    public abstract String getName();

    @Override
    public void run() {
        super.run();
        satisfyDependencies(target);
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return getPersistentScope().getObject(ScreenComponent.class);
    }

    private void satisfyDependencies(PresenterHolderActivityCoreView target) {
        ScreenComponent component = getPersistentScope().getObject(ScreenComponent.class);
        if (component == null) {
            component = createScreenComponent();
            getPersistentScope().putObject(component, ScreenComponent.class);
        }
        component.inject(target);
    }

    protected Intent getIntent() {
        return intent;
    }

    public final ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getActivityComponent(),
                getActivityScreenModule(),
                new CoreActivityScreenModule(getPersistentScope()),
                intent);
    }
}

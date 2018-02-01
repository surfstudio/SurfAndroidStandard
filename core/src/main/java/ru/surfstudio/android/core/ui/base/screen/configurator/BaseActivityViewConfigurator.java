package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.ActivityPersistentScope;

/**
 * Базовый класс конфигуратора экрана, основанного на Activity, см {@link ViewConfigurator}
 *
 * @param <P> родительский корневой даггер компонент
 * @param <M> даггер модуль для активити
 * @param <A> родительский ActivityComponent
 */
public abstract class BaseActivityViewConfigurator<P, A, M>
        extends BaseActivityConfigurator<A, P>
        implements ViewConfigurator<ActivityPersistentScope> {

    private CoreActivityViewInterface target;
    private Intent intent;


    public <T extends FragmentActivity & CoreActivityViewInterface> BaseActivityViewConfigurator(
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

    private void satisfyDependencies(CoreActivityViewInterface target) {
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

    private ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getActivityComponent(),
                getActivityScreenModule(),
                new CoreActivityScreenModule(),
                getIntent());
    }
}

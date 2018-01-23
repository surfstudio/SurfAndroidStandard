package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.dagger.CoreFragmentScreenModule;
import ru.surfstudio.android.core.ui.base.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.view.core.PresenterHolderFragmentCoreView;

/**
 * Базовый класс конфигуратора экрана, основанного на Fragment, см {@link ScreenConfigurator}
 * @param <P> родительский даггер компонент
 * @param <M> даггер модуль для активити
 */
public abstract class BaseFragmentScreenConfigurator<P, M>
        extends BaseFragmentConfigurator
        implements ScreenConfigurator{

    private Bundle args;
    private PersistentScope persistentScreenScope;
    private PresenterHolderFragmentCoreView target;

    public <T extends Fragment & PresenterHolderFragmentCoreView> baseFragmentScreenConfigurator(
            T target,
            Bundle args) {
        super(target);
        this.args = args;
        this.target = target;
    }

    protected abstract M getFragmentScreenModule();

    protected abstract P getParentComponent();

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M fragmentScreenModule,
                                                             CoreFragmentScreenModule coreFragmentScreenModule,
                                                             Bundle args);

    @Override
    public abstract String getName();

    @Override
    public void run(){
        super.run();
        satisfyDependencies(target);
    }

    public void setPersistentScope(PersistentScope persistentScreenScope) {
        this.persistentScreenScope = persistentScreenScope;
    }

    protected PersistentScope getPersistentScope() {
        return persistentScreenScope;
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return persistentScreenScope.getObject(ScreenComponent.class);
    }

    private void satisfyDependencies(PresenterHolderFragmentCoreView target) {
        ScreenComponent component = persistentScreenScope.getObject(ScreenComponent.class);
        if (component == null) {
            component = createScreenComponent();
            persistentScreenScope.putObject(component, ScreenComponent.class);
        }
        component.inject(target);
    }

    private final ScreenComponent createScreenComponent() {
        return createScreenComponent(
                getParentComponent(),
                getFragmentScreenModule(),
                new CoreFragmentScreenModule(getPersistentScope()),
                args);
    }

    protected Bundle getArgs(){
        return args;
    }

}

package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.dagger.CoreFragmentScreenModule;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.FragmentPersistentScope;

/**
 * Базовый класс конфигуратора экрана, основанного на Fragment, см {@link ViewConfigurator}
 * @param <P> родительский даггер компонент
 * @param <M> даггер модуль для активити
 */
public abstract class BaseFragmentViewConfigurator<P, M>
        extends BaseFragmentConfigurator
        implements ViewConfigurator<FragmentPersistentScope> {

    private Bundle args;
    private CoreFragmentViewInterface target;

    public <T extends Fragment & CoreFragmentViewInterface> BaseFragmentViewConfigurator(
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

    @Override
    public ScreenComponent getScreenComponent() {
        return getPersistentScope().getObject(ScreenComponent.class);
    }

    private void satisfyDependencies(CoreFragmentViewInterface target) {
        ScreenComponent component = getPersistentScope().getObject(ScreenComponent.class);
        if (component == null) {
            component = createScreenComponent();
            getPersistentScope().putObject(component, ScreenComponent.class);
        }
        component.inject(target);
    }

    private ScreenComponent createScreenComponent() {
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

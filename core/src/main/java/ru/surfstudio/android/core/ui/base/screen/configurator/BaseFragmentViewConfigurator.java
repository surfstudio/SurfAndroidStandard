package ru.surfstudio.android.core.ui.base.screen.configurator;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import ru.surfstudio.android.core.ui.base.dagger.CoreFragmentScreenModule;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.FragmentViewPersistentScope;

/**
 * Базовый класс конфигуратора экрана, основанного на Fragment, см {@link ViewConfigurator}
 * @param <P> родительский даггер компонент (ActivityComponent)
 * @param <M> даггер модуль для фрагмента
 */
public abstract class BaseFragmentViewConfigurator<P, M>
        extends BaseFragmentConfigurator
        implements ViewConfigurator {

    private Bundle args;
    private ScreenComponent screenComponent;
    private FragmentViewPersistentScope persistentScope;

    public BaseFragmentViewConfigurator(Bundle args) {
        this.args = args;
    }

    protected abstract M getFragmentScreenModule();

    protected abstract P getParentComponent();

    protected abstract ScreenComponent createScreenComponent(P parentComponent,
                                                             M fragmentScreenModule,
                                                             CoreFragmentScreenModule coreFragmentScreenModule,
                                                             Bundle args);

    @Override
    public void run() {
        super.run();
        satisfyDependencies(getTargetFragmentView());
    }

    protected <T extends Fragment & CoreFragmentViewInterface> T getTargetFragmentView() {
        return (T)getPersistentScope().getScreenState().getCoreFragmentView();
    }

    @Override
    public ScreenComponent getScreenComponent() {
        return screenComponent;
    }

    private void satisfyDependencies(CoreFragmentViewInterface target) {
        if (screenComponent == null) {
            screenComponent = createScreenComponent();
        }
        screenComponent.inject(target);
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

    protected FragmentViewPersistentScope getPersistentScope() {
        return persistentScope;
    }

    public void setPersistentScope(FragmentViewPersistentScope persistentScope) {
        this.persistentScope = persistentScope;
    }

}

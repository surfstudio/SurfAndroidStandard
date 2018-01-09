package ru.surfstudio.standard.ui.screen.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import ru.surfstudio.android.core.ui.base.screen.activity.BaseRenderableHandleableErrorActivityView;
import ru.surfstudio.android.core.ui.base.screen.configurator.BaseActivityConfigurator;
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.standard.R;
import ru.surfstudio.standard.ui.base.configurator.ActivityConfigurator;

/**
 * Вью главного экрана
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivityView extends BaseRenderableHandleableErrorActivityView<MainScreenModel> {
    @Inject
    MainPresenter presenter;

    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }

    @IdRes
    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public BaseActivityConfigurator createActivityConfigurator() {
        return new ActivityConfigurator(this);
    }

    @Override
    public ScreenConfigurator createScreenConfigurator(Activity activity, Intent intent) {
        return new MainScreenConfigurator(activity, intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState,
                         boolean viewRecreated) {
        super.onCreate(savedInstanceState, persistentState, viewRecreated);
    }

    @Override
    protected void renderInternal(MainScreenModel screenModel) {
    }
}

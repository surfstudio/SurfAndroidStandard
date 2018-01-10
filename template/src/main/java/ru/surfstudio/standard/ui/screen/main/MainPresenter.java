package ru.surfstudio.standard.ui.screen.main;

import javax.inject.Inject;

import ru.surfstudio.android.core.app.dagger.scope.PerScreen;
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenter;
import ru.surfstudio.android.core.ui.base.screen.presenter.BasePresenterDependency;

/**
 * Презентер главного экрана
 */
@PerScreen
class MainPresenter extends BasePresenter<MainActivityView> {

    private final MainScreenModel screenModel;

    @Inject
    MainPresenter(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency);
        this.screenModel = new MainScreenModel();
    }

    @Override
    public void onLoad(boolean viewRecreated) {
        super.onLoad(viewRecreated);
        getView().render(screenModel);
    }
}
package ${packageName};

import ru.surfstudio.android.core.mvp.presenter.BasePresenter;
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency;
import ru.surfstudio.android.dagger.scope.PerScreen;
import javax.inject.Inject;

/**
 * Презентер [${viewClassName}].
 */
@PerScreen
class ${presenterClassName} extends BasePresenter<${viewClassName}> {

    @Inject
    public ${presenterClassName}(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency);
    }

    @Override
    public void onLoad(boolean viewRecreated) {
        super.onLoad(viewRecreated);

    }
}

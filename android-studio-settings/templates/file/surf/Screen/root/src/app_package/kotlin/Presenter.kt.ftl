package ${packageName}

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер [${viewClassName}].
 */
@PerScreen
class ${presenterClassName} @Inject constructor(
        basePresenterDependency: BasePresenterDependency
): BasePresenter<${viewClassName}>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

    }
}
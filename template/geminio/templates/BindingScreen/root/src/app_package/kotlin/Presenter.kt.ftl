package ${packageName}

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер [${viewClassName}].
 */
@PerScreen
class ${presenterClassName} @Inject constructor(
        private val bm: ${bindModelClassName},
        basePresenterDependency: BasePresenterDependency
): BaseRxPresenter(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }
}
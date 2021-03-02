package ru.surfstudio.android.navigation.sample_standard.screen.search.request

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.navigation.command.fragment.RemoveLast
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.sample_standard.screen.base.presenter.CommandExecutionPresenter
import javax.inject.Inject

class SearchRequestPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        override val commandExecutor: NavigationCommandExecutor,
        private val bm: SearchRequestBindModel,
        private val route: SearchRequestRoute
) : BaseRxPresenter(basePresenterDependency), CommandExecutionPresenter {

    override fun onFirstLoad() {
        bm.closeClick.bindTo { RemoveLast().execute() }
        bm.textChanges.bindTo { bm.textState.accept(it) }
        bm.resultClick.bindTo {
            listOf(
                    RemoveLast(),
                    EmitScreenResult(route, bm.textState.value.toString())
            ).execute()
        }
    }
}
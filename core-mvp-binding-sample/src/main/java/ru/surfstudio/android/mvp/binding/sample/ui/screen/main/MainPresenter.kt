package ru.surfstudio.android.mvp.binding.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.binding.BaseBindingPresenter
import ru.surfstudio.android.core.mvp.binding.BindData
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.sample.ui.screen.main.MainActivityView
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BaseBindingPresenter<MainScreenModel, MainActivityView>(basePresenterDependency) {

    override val screenModel: MainScreenModel = MainScreenModel()

    private val relative: Map<BindData<PaneDataModel>, Set<BindData<PaneDataModel>>> = mapOf(
            screenModel.panel1 to setOf(screenModel.panel2, screenModel.panel4, screenModel.panel5),
            screenModel.panel2 to setOf(screenModel.panel1, screenModel.panel3, screenModel.panel4, screenModel.panel5),
            screenModel.panel3 to setOf(screenModel.panel2, screenModel.panel5, screenModel.panel6),
            screenModel.panel4 to setOf(screenModel.panel1, screenModel.panel2, screenModel.panel5, screenModel.panel7, screenModel.panel8),
            screenModel.panel5 to setOf(screenModel.panel1, screenModel.panel2, screenModel.panel3, screenModel.panel4, screenModel.panel6, screenModel.panel7, screenModel.panel8, screenModel.panel9),
            screenModel.panel6 to setOf(screenModel.panel2, screenModel.panel3, screenModel.panel4, screenModel.panel6, screenModel.panel5, screenModel.panel8, screenModel.panel9),
            screenModel.panel7 to setOf(screenModel.panel4, screenModel.panel5, screenModel.panel8),
            screenModel.panel8 to setOf(screenModel.panel4, screenModel.panel5, screenModel.panel6, screenModel.panel7, screenModel.panel9),
            screenModel.panel9 to setOf(screenModel.panel5, screenModel.panel6, screenModel.panel8)
    )

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        observePanel(screenModel.panel1)
        observePanel(screenModel.panel2)
        observePanel(screenModel.panel3)
        observePanel(screenModel.panel4)
        observePanel(screenModel.panel5)
        observePanel(screenModel.panel6)
        observePanel(screenModel.panel7)
        observePanel(screenModel.panel8)
        observePanel(screenModel.panel9)
    }

    private fun observePanel(data: BindData<PaneDataModel>) {
        data.observe(this) {
            relative[data]?.forEach { it.setValue(it.value.copy(state = data.value.state), this) }
            data.setValue(data.value.copy(state = data.value.state.next()), this)
        }
    }
}
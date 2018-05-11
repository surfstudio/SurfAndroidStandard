package ru.surfstudio.android.mvp.binding.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.binding.BaseBindingPresenter
import ru.surfstudio.android.core.mvp.binding.BindData
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BaseBindingPresenter<MainScreenModel, MainActivityView>(basePresenterDependency) {

    override val screenModel: MainScreenModel = MainScreenModel()

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
        observe(data) {
            screenModel.relation[data]
                    ?.forEach { it.setValue(this, it.value.copy(state = it.value.state.next())) }
            checkToWin()
        }
    }

    private fun checkToWin() {
        if (screenModel.relation.keys
                        .filter { it.value.state == State.PRESSED }
                        .count() == screenModel.relation.keys.count()) {
            screenModel.solved.setValue(this, true)
        } else {
            screenModel.solved.setValue(this, false)
        }
    }

    fun onEasyWinClick() {
        screenModel.relation.keys
                .forEach { it.setValue(this, it.value.copy(state = State.PRESSED)) }
        checkToWin()
    }

    fun onUnbindClick() {
        bindsHolder.unObserve()
    }

}
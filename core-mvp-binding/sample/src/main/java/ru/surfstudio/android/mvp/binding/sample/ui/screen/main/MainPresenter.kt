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

    override val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)

        observePanel(sm.panel1)
        observePanel(sm.panel2)
        observePanel(sm.panel3)
        observePanel(sm.panel4)
        observePanel(sm.panel5)
        observePanel(sm.panel6)
        observePanel(sm.panel7)
        observePanel(sm.panel8)
        observePanel(sm.panel9)
    }

    private fun observePanel(data: BindData<PaneDataModel>) {
        observe(data) {
            sm.relation[data]
                    ?.forEach { it.setValue(this, it.value.copy(state = it.value.state.next())) }
            checkToWin()
        }
    }

    private fun checkToWin() {
        if (sm.relation.keys
                .filter { it.value.state == State.PRESSED }
                .count() == sm.relation.keys.count()) {
            sm.solved.setValue(this, true)
        } else {
            sm.solved.setValue(this, false)
        }
    }

    fun onEasyWinClick() {
        sm.relation.keys
                .forEach { it.setValue(this, it.value.copy(state = State.PRESSED)) }
        checkToWin()
    }

    fun onUnbindClick() {
        bindsHolder.unObserve()
    }

}
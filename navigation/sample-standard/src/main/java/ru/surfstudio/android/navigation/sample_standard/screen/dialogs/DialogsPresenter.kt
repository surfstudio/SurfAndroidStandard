package ru.surfstudio.android.navigation.sample_standard.screen.dialogs

import ru.surfstudio.android.core.mvp.binding.rx.ui.BaseRxPresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.animation.styled.StyledAnimations
import ru.surfstudio.android.navigation.command.dialog.Show
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.R
import ru.surfstudio.android.navigation.sample_standard.screen.dialogs.amazing.AmazingDialogRoute
import javax.inject.Inject

@PerScreen
class DialogsPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency,
        private val executor: NavigationCommandExecutor,
        private val bm: DialogsBindModel
) : BaseRxPresenter(basePresenterDependency) {

    override fun onFirstLoad() {
        bm.openDialogButtonClicked bindTo { openDialog() }
        bm.openDialogWithFadeButtonClicked bindTo { openDialogWithFade() }
        bm.openDialogWithSlideButtonClicked bindTo { openDialogWithSlide() }
    }

    private fun openDialog() {
        executor.execute(Show(AmazingDialogRoute()))
    }

    private fun openDialogWithFade() {
        executor.execute(Show(AmazingDialogRoute(), StyledAnimations(R.style.FadeDialogAnimation)))
    }

    private fun openDialogWithSlide() {
        executor.execute(Show(AmazingDialogRoute(), StyledAnimations(R.style.SlideDialogAnimation)))
    }
}
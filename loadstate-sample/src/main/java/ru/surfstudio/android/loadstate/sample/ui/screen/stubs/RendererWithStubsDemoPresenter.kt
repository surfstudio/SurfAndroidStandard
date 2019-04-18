package ru.surfstudio.android.loadstate.sample.ui.screen.stubs

import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.ErrorLoadState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.NoneLoadState
import javax.inject.Inject

/**
 * Презентер экрана для демонстрации работы DefaultLoadStateRenderer с использованием заглушек (шиммеров)
 */
@PerScreen
class RendererWithStubsDemoPresenter @Inject constructor(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<RendererWithStubsDemoActivityView>(basePresenterDependency) {

    private val screenModel = RendererWithStubsDemoScreenModel()

    private val publishSubject: PublishSubject<LoadStateInterface> = PublishSubject.create()

    override fun onFirstLoad() {
        imitateLoading()
    }

    fun mainLoading() {
        screenModel.itemList = emptyList()
        publishSubject.onNext(MainLoadingState())
    }

    fun none() {
        screenModel.itemList = (1..6).toList()
        publishSubject.onNext(NoneLoadState())
    }

    fun error() {
        screenModel.itemList = emptyList()
        publishSubject.onNext(ErrorLoadState())
    }

    private fun imitateLoading() {
        screenModel.loadState = MainLoadingState()
        view.render(screenModel)

        subscribe(publishSubject) {
            screenModel.loadState = it
            view.render(screenModel)
        }
    }
}

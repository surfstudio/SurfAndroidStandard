package ru.surfstudio.android.loadstate.sample.ui.screen.ordinary

import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.*
import javax.inject.Inject

/**
 * Презентер экрана для демострации работы DefaultLoadStateRenderer
 */
@PerScreen
class DefaultRendererDemoPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency
) : BasePresenter<DefaultRendererDemoActivityView>(basePresenterDependency) {

    private val screenModel = DefaultRendererDemoScreenModel()

    private val publishSubject: PublishSubject<LoadStateInterface> = PublishSubject.create()

    override fun onFirstLoad() {
        load()
    }

    fun transparentLoading() {
        publishSubject.onNext(TransparentLoadingState())
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

    fun empty() {
        screenModel.itemList = emptyList()
        publishSubject.onNext(EmptyLoadState())
    }

    fun custom() {
        screenModel.itemList = emptyList()
        publishSubject.onNext(CustomLoadState())
    }

    private fun load() {
        screenModel.loadState = MainLoadingState()
        view.render(screenModel)

        subscribe(publishSubject) {
            screenModel.loadState = it
            view.render(screenModel)
        }
    }

}

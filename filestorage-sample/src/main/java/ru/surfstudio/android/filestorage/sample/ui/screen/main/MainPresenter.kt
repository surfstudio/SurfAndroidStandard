package ru.surfstudio.android.filestorage.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.filestorage.sample.R
import ru.surfstudio.android.filestorage.sample.interactor.ip.IpRepository
import ru.surfstudio.android.message.MessageController
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val repository: IpRepository,
                                                 private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val screenModel: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            tryLoadData()
        } else {
            view.render(screenModel)
        }
    }

    private fun tryLoadData() {
        if (screenModel.loadState == LoadState.ERROR || screenModel.loadState == LoadState.EMPTY) {
            screenModel.loadState = LoadState.MAIN_LOADING
            view.render(screenModel)
        }
        loadData()
    }

    private fun loadData() {
        subscribeIoHandleError(repository.getIp(), { ip ->
            screenModel.ip = ip
            screenModel.loadState = LoadState.NONE
            view.render(screenModel)
        }, {
            screenModel.loadState = LoadState.NONE
            view.render(screenModel)
        })
    }

    fun reloadData() = tryLoadData()

    fun saveIpToCache() = repository.saveIp(screenModel.ip)

    fun loadDataFromCache() {
        val message = repository.getIpFromCache()?.value
        if (message != null) {
            messageController.show(message)
        } else {
            messageController.show(R.string.empty_cache_message)
        }
    }

    fun clearCache() = repository.clearIpStorage()
}
package ru.surfstudio.android.shared.pref.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.shared.pref.sample.interactor.ip.IpRepository
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.android.shared.pref.sample.R
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

    fun saveIpToCache() {
        val messageRes: Int = screenModel.ip?.let {
            repository.saveIp(it)
            R.string.cache_created_message
        } ?: R.string.null_ip_message
        messageController.show(messageRes)
    }

    fun loadDataFromCache() {
        val message = repository.getIpFromStorage()?.value
        if (message.isNullOrEmpty()) {
            messageController.show(R.string.empty_cache_message)
        } else {
            messageController.show(message!!)
        }
    }

    fun clearCache() {
        repository.clearIpStorage()
        messageController.show(R.string.cache_deleted_message)
    }
}
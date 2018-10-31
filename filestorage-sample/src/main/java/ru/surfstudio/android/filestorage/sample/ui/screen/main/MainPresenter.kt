package ru.surfstudio.android.filestorage.sample.ui.screen.main

import android.support.annotation.StringRes
import ru.surfstudio.android.core.app.StringsProvider
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.filestorage.sample.R
import ru.surfstudio.android.filestorage.sample.interactor.ip.IpRepository
import ru.surfstudio.android.message.MessageController
import ru.surfstudio.standard.base_ui.placeholder.LoadState
import javax.inject.Inject

/**
 * Презентер главного экрана
 */
@PerScreen
internal class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency,
                                                 private val repository: IpRepository,
                                                 private val stringsProvider: StringsProvider,
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
        val message: String = screenModel.ip?.let {
            repository.saveIp(it)
            getString(R.string.cache_created_message)
        } ?: getString(R.string.null_ip_message)
        messageController.show(message)
    }

    fun loadDataFromCache() {
        val message = repository.getIpFromCache()?.value
        if (message.isNullOrEmpty()) {
            messageController.show(getString(R.string.empty_cache_message))
        } else {
            messageController.show(message!!)
        }
    }

    fun clearCache() {
        repository.clearIpStorage()
        messageController.show(getString(R.string.cache_deleted_message))
    }

    private fun getString(@StringRes stringId: Int): String = stringsProvider.getString(stringId)
}
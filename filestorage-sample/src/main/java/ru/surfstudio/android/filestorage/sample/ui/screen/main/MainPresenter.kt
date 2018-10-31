package ru.surfstudio.android.filestorage.sample.ui.screen.main

import android.support.annotation.StringRes
import ru.surfstudio.android.core.app.StringsProvider
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
                                                 private val stringsProvider: StringsProvider,
                                                 private val messageController: MessageController
) : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val sm: MainScreenModel = MainScreenModel()

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
        if (!viewRecreated) {
            tryLoadData()
        } else {
            view.render(sm)
        }
    }

    private fun tryLoadData() {
        if (sm.loadState == LoadState.ERROR || sm.loadState == LoadState.EMPTY) {
            sm.loadState = LoadState.MAIN_LOADING
            view.render(sm)
        }
        loadData()
    }

    private fun loadData() {
        subscribeIoHandleError(repository.getIp(), { ip ->
            sm.ip = ip
            sm.loadState = LoadState.NONE
            view.render(sm)
        }, {
            sm.loadState = LoadState.NONE
            view.render(sm)
        })
    }

    fun reloadData() = tryLoadData()

    fun saveIpToCache() {
        val message: String = sm.ip?.let {
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
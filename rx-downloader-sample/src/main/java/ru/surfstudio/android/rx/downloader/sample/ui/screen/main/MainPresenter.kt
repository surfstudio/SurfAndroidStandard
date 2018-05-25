package ru.surfstudio.android.rx.downloader.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.downloader.task.DownloadTask
import javax.inject.Inject

@PerScreen
class MainPresenter @Inject constructor(basePresenterDependency: BasePresenterDependency)
    : BasePresenter<MainActivityView>(basePresenterDependency) {

    private val testDownloadUrl: String = "http://developer.alexanderklimov.ru/android/images/android_cat.jpg"

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }

    fun onDownloadBtnClick() {
        val downloadTask = DownloadTask(testDownloadUrl)

    }
}

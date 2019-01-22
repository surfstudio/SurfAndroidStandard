package ru.surfstudio.android.core.mvp.rx.ui

interface CoreRxPresenter<M: RxModel> {
    fun getRxModel(): M
}
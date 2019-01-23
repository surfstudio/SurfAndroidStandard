package ru.surfstudio.android.core.mvp.rx.ui

interface CoreRxPresenter<M: RxModel> {
    val model: M
}
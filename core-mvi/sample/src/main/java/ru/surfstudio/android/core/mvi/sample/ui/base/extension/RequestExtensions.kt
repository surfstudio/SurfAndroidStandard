package ru.surfstudio.android.core.mvi.sample.ui.base.extension

import ru.surfstudio.android.core.mvp.binding.rx.request.data.MainLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SwipeRefreshLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.data.TransparentLoading
import ru.surfstudio.android.core.mvp.binding.rx.request.state.RequestState

/**
 * Маппинг функции для работы с данными
 *
 * TODO Можно переопределить в проекте, чтобы задействовать свои стратегии работы с данными
 */
fun <T> RequestState<T>.observeMainLoading() = observeLoading()
        .filter { it is MainLoading }
        .map { it.isLoading }

fun <T> RequestState<T>.observeTransparentLoading() = observeLoading()
        .filter { it is TransparentLoading }
        .map { it.isLoading }

fun <T> RequestState<T>.observeSwrLoading() = observeLoading()
        .filter { it is SwipeRefreshLoading }
        .map { it.isLoading }
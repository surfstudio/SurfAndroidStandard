package ru.surfstudio.android.core.mvp.binding.rx.builders

import io.reactivex.*
import io.reactivex.functions.Consumer

/**
 * Интерфейс builder'а для Rx-запросов с поддержкой восстановления соединения при отсутствии интернета.
 */
interface RxBuilderAutoReload {

    fun reloadErrorAction(autoReloadAction: () -> Unit): Consumer<Throwable>

    /**
     * Build-функция для [Single], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    fun <T> Single<T>.autoReload(autoReloadAction: () -> Unit): Single<T> =
            doOnError(reloadErrorAction(autoReloadAction))

    /**
     * Build-функция для [Observable], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    fun <T> Observable<T>.autoReload(autoReloadAction: () -> Unit): Observable<T> =
            doOnError(reloadErrorAction(autoReloadAction))

    /**
     * Build-функция для [Maybe], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    fun <T> Maybe<T>.autoReload(autoReloadAction: () -> Unit): Maybe<T> =
            doOnError(reloadErrorAction(autoReloadAction))

    /**
     * Build-функция для [Completable], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    fun Completable.autoReload(autoReloadAction: () -> Unit): Completable =
            doOnError(reloadErrorAction(autoReloadAction))
}
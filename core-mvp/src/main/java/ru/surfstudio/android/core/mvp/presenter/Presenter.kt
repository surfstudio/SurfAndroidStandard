package ru.surfstudio.android.core.mvp.presenter

import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Базовый интерфейс презентера
 */
interface Presenter<V : CoreView?> {

    val stateRestorer: StateRestorer<*>?
        get() = null

    fun attachView(view: V)

    /**
     * This method is called, when view is ready
     *
     * @param viewRecreated - showSimpleDialog whether view created in first time or recreated after
     * changing configuration
     */
    fun onLoad(viewRecreated: Boolean)

    /**
     * вызывается при первом запуске экрана, если экран восстановлен с диска,
     * то это тоже считается первым запуском
     */
    fun onFirstLoad()

    /**
     * Called when view is started
     */
    fun onStart()

    /**
     * Called when view is resumed
     */
    fun onResume()

    /**
     * Called when view is paused
     */
    fun onPause()

    /**
     * Called when view is stopped
     */
    fun onStop()

    /**
     * Called when view is detached
     */
    fun onViewDetach()

    /**
     * Called when screen is finally destroyed
     */
    fun onDestroy()

    fun detachView()
}
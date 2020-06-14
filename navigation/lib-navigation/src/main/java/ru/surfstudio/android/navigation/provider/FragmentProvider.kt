package ru.surfstudio.android.navigation.provider

import androidx.fragment.app.Fragment

/**
 * Provider, that obtains fragment, associated with current screen scope.
 *
 * Can be used to get fragments inside Presenter.
 */
interface FragmentProvider {

    /**
     * Obtains fragment, associated with current screen scope.
     *
     * If there's no fragment in the current scope (for example, current scope is Activity),
     * provider will return null.
     */
    fun provide(): Fragment?
}
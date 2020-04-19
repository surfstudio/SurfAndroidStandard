package ru.surfstudio.android.navigation.sample_standard.screen.base

import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.navigation.provider.FragmentProvider
import javax.inject.Inject

class FragmentProviderImpl @Inject constructor(
        private val persistentScope: PersistentScope
) : FragmentProvider {
    override fun provide(): Fragment? {
        return when (persistentScope) {
            is FragmentPersistentScope -> persistentScope.screenState.fragment
            else -> null //TODO widget
        }
    }
}
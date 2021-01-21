package ru.surfstudio.standard.ui.provider

import androidx.fragment.app.Fragment
import ru.surfstudio.android.core.ui.scope.FragmentPersistentScope
import ru.surfstudio.android.core.ui.scope.PersistentScope
import ru.surfstudio.android.core.ui.state.FragmentScreenState
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope
import ru.surfstudio.android.navigation.provider.FragmentProvider
import javax.inject.Inject

/**
 * Проектная реализация [FragmentProvider].
 * Вытаскивает фрагмент из скоупа экрана, либо из родительского скоупа, если экран - виджет.
 */
class FragmentProviderImpl @Inject constructor(
        private val persistentScope: PersistentScope
) : FragmentProvider {
    override fun provide(): Fragment? {
        return when (persistentScope) {
            is FragmentPersistentScope -> persistentScope.screenState.fragment
            is WidgetViewPersistentScope -> {
                val parentState = persistentScope.screenState.parentState
                return if (parentState is FragmentScreenState) {
                    return parentState.fragment
                } else {
                    null
                }
            }
            else -> null
        }
    }
}

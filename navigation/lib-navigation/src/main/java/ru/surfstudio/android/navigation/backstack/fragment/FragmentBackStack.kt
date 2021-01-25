package ru.surfstudio.android.navigation.backstack.fragment

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.backstack.BackStack
import ru.surfstudio.android.navigation.backstack.fragment.entry.FragmentBackStackEntry
import java.util.Stack

/**
 * [BackStack] for fragments.
 */
class FragmentBackStack : BackStack<FragmentBackStackEntry>, Stack<FragmentBackStackEntry>() {

    /**
     * Find [FragmentBackStackEntry] with a specific [tag].
     */
    fun find(tag: String): FragmentBackStackEntry? = find { it.tag == tag }

    /**
     * Find [Fragment] with a specific [tag]
     */
    fun findFragment(tag: String): Fragment? = find(tag)?.fragment

    /**
     * Safe peek last fragment from a back stack.
     *
     * @return Last [Fragment] or null if back stack is empty.
     */
    fun peekFragment(): Fragment? = lastOrNull()?.fragment

    /**
     * Copy [FragmentBackStack] to a new entity.
     *
     * Used to pass backStack to a consumer
     * and prevent it from being modified by this consumer.
     */
    fun copy(): FragmentBackStack {
        val stackCopy = FragmentBackStack()
        forEach { entry -> stackCopy.push(entry) }
        return stackCopy
    }
}

package ru.surfstudio.android.core.ui.view_binding

import android.view.View
import androidx.core.app.ComponentActivity
import androidx.viewbinding.ViewBinding

private class ActivityViewBindingProperty<A : ComponentActivity, T : ViewBinding>(
        viewBinder: (A) -> T
) : ViewBindingProperty<A, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A) = thisRef
}

/**
 * Create new [ViewBinding] associated with the [Activity][ComponentActivity] and allow customize how
 * a [View] will be bounded to the view binding.
 */
inline fun <A : ComponentActivity, T : ViewBinding> ComponentActivity.viewBinding(
        crossinline vbFactory: (View) -> T,
        crossinline viewProvider: (A) -> View
): ViewBindingProperty<A, T> {
    return viewBinding { activity: A -> vbFactory(viewProvider(activity)) }
}

/**
 * Create new [ViewBinding] associated with the [Activity][ComponentActivity] and allow customize how
 * a [View] will be bounded to the view binding.
 */
fun <A : ComponentActivity, T : ViewBinding> ComponentActivity.viewBinding(
        viewBinder: (A) -> T
): ViewBindingProperty<A, T> {
    return ActivityViewBindingProperty(viewBinder)
}
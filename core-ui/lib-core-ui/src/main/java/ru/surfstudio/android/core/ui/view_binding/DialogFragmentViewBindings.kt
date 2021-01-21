package ru.surfstudio.android.core.ui.view_binding

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

private class DialogFragmentViewBindingProperty<F : DialogFragment, T : ViewBinding>(
        viewBinder: (F) -> T
) : ViewBindingProperty<F, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        return if (thisRef.view != null) thisRef.viewLifecycleOwner else thisRef
    }
}

/**
 * Create new [ViewBinding] associated with the [DialogFragment]
 */
fun <F : DialogFragment, T : ViewBinding> DialogFragment.dialogViewBinding(
        viewBinder: (F) -> T
): ViewBindingProperty<F, T> {
    return DialogFragmentViewBindingProperty(viewBinder)
}

/**
 * Create new [ViewBinding] associated with the [DialogFragment]
 *
 * @param vbFactory Function that create new instance of [ViewBinding]. `MyViewBinding::bind` can be used
 */
inline fun <F : DialogFragment, T : ViewBinding> DialogFragment.dialogViewBinding(
        crossinline vbFactory: (View) -> T,
        crossinline viewProvider: (F) -> View
): ViewBindingProperty<F, T> {
    return dialogViewBinding { fragment -> vbFactory(viewProvider(fragment)) }
}

/**
 * Create new [ViewBinding] associated with the [DialogFragment][this]
 *
 * @param vbFactory Function that create new instance of [ViewBinding]. `MyViewBinding::bind` can be used
 * @param viewBindingRootId Id of the root view from your custom view
 */
@Suppress("unused")
inline fun <T : ViewBinding> DialogFragment.dialogViewBinding(
        crossinline vbFactory: (View) -> T,
        @IdRes viewBindingRootId: Int
): ViewBindingProperty<DialogFragment, T> {
    return dialogViewBinding(vbFactory) { fragment: DialogFragment ->
        fragment.dialog!!.window!!.decorView.findViewById(viewBindingRootId)
    }
}

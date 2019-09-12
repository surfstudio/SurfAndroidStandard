package ru.surfstudio.android.mvp.dialog.simple.bottomsheet

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope
import ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogInterface
import ru.surfstudio.android.mvp.dialog.simple.SimpleDialogDelegate
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope

/**
 * Имплементация Simple dialog для BottomSheet
 * @see ru.surfstudio.android.mvp.dialog.simple.CoreSimpleDialogFragment
 */
abstract class CoreSimpleBottomSheetDialogFragment : BottomSheetDialogFragment(), CoreSimpleDialogInterface {

    private val delegate = SimpleDialogDelegate(this)

    override fun <A : ActivityViewPersistentScope> show(parentActivityViewPersistentScope: A) {
        delegate.show(parentActivityViewPersistentScope)
    }

    override fun <F : FragmentViewPersistentScope> show(parentFragmentViewPersistentScope: F) {
        delegate.show(parentFragmentViewPersistentScope)
    }

    override fun <W : WidgetViewPersistentScope> show(parentWidgetViewPersistentScope: W) {
        delegate.show(parentWidgetViewPersistentScope)
    }

    override fun <T> getScreenComponent(componentClass: Class<T>): T {
        return delegate.getScreenComponent(componentClass)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        delegate.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }
}
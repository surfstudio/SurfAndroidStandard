package ru.surfstudio.android.core.ui.view_binding

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class ViewBindingProperty<in R : Any, T : ViewBinding>(
        private val viewBinder: (R) -> T
) : ReadOnlyProperty<R, T> {

    private var viewBinding: T? = null
    private val lifecycleObserver = ClearOnDestroyLifecycleObserver()
    private var thisRef: R? = null

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        viewBinding?.let { return it }

        this.thisRef = thisRef
        val lifecycle = getLifecycleOwner(thisRef).lifecycle
        val viewBinding = viewBinder(thisRef)
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            mainHandler.post { this.viewBinding = null }
            // We can access to ViewBinding after on destroy,
            // but don't save it in property to prevent memory leak
        } else {
            lifecycle.addObserver(lifecycleObserver)
            this.viewBinding = viewBinding
        }
        return viewBinding
    }

    @MainThread
    fun clear() {
        thisRef?.let {
            thisRef = null
            getLifecycleOwner(it).lifecycle.removeObserver(lifecycleObserver)
            mainHandler.post { viewBinding = null }
        }
    }

    private inner class ClearOnDestroyLifecycleObserver : DefaultLifecycleObserver {

        @MainThread
        override fun onDestroy(owner: LifecycleOwner): Unit = clear()
    }

    private companion object {

        private val mainHandler = Handler(Looper.getMainLooper())
    }
}

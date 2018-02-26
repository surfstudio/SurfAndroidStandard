package ru.surfstudio.android.animations.behaviors

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * Behavior для view над [Snackbar]
 */
open class ViewSnackbarBehavior<V : View> @JvmOverloads constructor(
        context: Context? = null,
        attrs: AttributeSet? = null
) : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout?, child: V, dependency: View?): Boolean =
            dependency is Snackbar.SnackbarLayout

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: V, dependency: View): Boolean {
        val translationY: Float =  Math.min(0F, dependency.translationY - dependency.height)

        //прерывает предыдущую анимацию
        ViewCompat.animate(child).cancel()

        child.translationY = translationY
        return true
    }

    override fun onDependentViewRemoved(parent: CoordinatorLayout?, child: V, dependency: View?) {
        ViewCompat.animate(child).translationY(0F).start()
    }
}
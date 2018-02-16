package ru.surfstudio.android.animations.behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.widget.Button

@Suppress("unused")
/**
 * Behavior для сокрытия кнопки при скролле NestedScrollView
 * TODO: переделать в зависимость от скролл вью , а не AppBarLayout
 * TODO: + добавить зависимость от SnackBar(наследоваться от [ViewSnackbarBehavior])
 */
class BottomButtonBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<Button>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: Button, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: Button, dependency: View): Boolean {
        if (dependency is AppBarLayout) {
            val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            val bottomMargin = layoutParams.bottomMargin
            val distanceToScroll = child.height + bottomMargin
            val bottomToolbarHeight = child.height + bottomMargin
            val ratio = dependency.getY() / bottomToolbarHeight.toFloat() * 0.5
            child.translationY = (-distanceToScroll * ratio).toFloat()
        }
        return true
    }
}
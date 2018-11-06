package ru.surfstudio.android.easyadapter.holder.async

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator

/**
 * Определяет необходимые поля для работы асинхроного инфлейта у ViewHolder
 */
interface AsyncViewHolder {
    /**
     * Показывает был ли выполнен инфлейт основной вью
     */
    var isItemViewInflated: Boolean

    /**
     * Определяет длительность анимации появления
     */
    var fadeInDuration: Long

    /**
     * Определяет длительность анимации изменения размеров
     */
    var resizeDuration: Long

    val holderKey: String
        get() = this::class.java.name

    /**
     * Определяет функцию, которая сразобает после инфлейта основной вью
     */
    fun inflateFinish(view: View) {}

    /**
     * Определяет функцию, которая будет выполнена после появления основной вью
     */
    fun fadeInFinish() {}

    /**
     * Определяет функцию, которая будет выполнена после изменения размеров основной вью
     */
    fun resizeFinish() {}

    fun toSize(itemView: ViewGroup,
               oldHeight: Int,
               height: Int): AnimatorSet? {
        if (resizeDuration == 0L || height == oldHeight) return null

        val animator = ValueAnimator.ofInt(oldHeight, height).apply {
            duration = resizeDuration

            addUpdateListener {
                itemView.layoutParams.height = it.animatedValue as Int
                itemView.requestLayout()
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    resizeFinish()
                }
            })
        }

        return AnimatorSet().apply {
            play(animator)
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }
}
package ru.surfstudio.standard.ui.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

open class AnimListenerImpl : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {}
    override fun onAnimationEnd(animation: Animator?) {}
    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationStart(animation: Animator?) {}
}

/**
 * Функция запускает [ValueAnimator] по заданным параметрам
 *
 * @param from от какого значения нужно начинать
 * @param to до какого значения нужно продолжать
 * @param duration продолжительность анимации в миллисекундах
 * @param startDelay задержка до начало анимации
 * @param update функция которая вызовется при каждом обновлении значения
 * @param complete функция которая вызовется при заканчивании анимации
 *
 * @return запущенную анимацию
 */
inline fun <reified V> changeValue(
        from: V,
        to: V,
        duration: Long,
        startDelay: Long = 0,
        crossinline update: (value: V) -> Unit,
        crossinline complete: () -> Unit
): ValueAnimator {
    val vH: PropertyValuesHolder = when (from) {
        is Int -> PropertyValuesHolder.ofInt("prop", from as Int, to as Int)
        is Float -> PropertyValuesHolder.ofFloat("prop", from as Float, to as Float)
        else -> throw UnsupportedOperationException("$from type not supported")
    }

    return ValueAnimator.ofPropertyValuesHolder(vH).apply {
        this.duration = duration
        this.startDelay = startDelay
        addUpdateListener {
            update(this.getAnimatedValue("prop") as V)
        }
        addListener(object : AnimListenerImpl() {
            override fun onAnimationEnd(animation: Animator?) {
                complete()
            }
        })
        start()
    }
}

/**
 * @see [changeValue]
 */
inline fun <reified V> changeValue(
        from: V,
        to: V,
        duration: Long,
        startDelay: Long = 0,
        crossinline update: (value: V) -> Unit
): ValueAnimator {
    val vH: PropertyValuesHolder = when (from) {
        is Int -> PropertyValuesHolder.ofInt("prop", from as Int, to as Int)
        is Float -> PropertyValuesHolder.ofFloat("prop", from as Float, to as Float)
        else -> throw UnsupportedOperationException("$from and $to types are not supported")
    }

    return ValueAnimator.ofPropertyValuesHolder(vH).apply {
        this.duration = duration
        this.startDelay = startDelay
        addUpdateListener {
            update(this.getAnimatedValue("prop") as V)
        }
        start()
    }
}
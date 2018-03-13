package ru.surfstudio.android.animations.anim

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.view.View
import android.view.animation.LinearInterpolator

/**
 * Extension для анимации View
 */

/**
 * Появление вью с изменением прозрачности
 */
fun View.fadeIn(duration: Long = AnimationUtil.ANIM_ENTERING, endAction: (() -> Unit)? = null) =
        AnimationUtil.fadeIn(this, duration, endAction)

/**
 * Сокрытие вью с изменением прозрачности
 */
fun View.fadeOut(duration: Long = AnimationUtil.ANIM_LEAVING,
                 visibility: Int = View.GONE,
                 endAction: (() -> Unit)? = null) =
        AnimationUtil.fadeOut(this, duration, visibility, endAction)

/**
 * Появление вью с эффектом "слайда" в зависимости от gravity
 */
fun View.slideIn(gravity: Int,
                 duration: Long = AnimationUtil.ANIM_ENTERING,
                 interpolator: TimeInterpolator = LinearInterpolator(),
                 startAction: ((View) -> Unit)? = null,
                 endAction: ((View) -> Unit)? = null) =
        AnimationUtil.slideIn(this, gravity, duration, interpolator, startAction, endAction)

/**
 * Исчезновение вью с эффектом "слайда" в зависимости от gravity
 */
fun View.slideOut(gravity: Int,
                  duration: Long = AnimationUtil.ANIM_LEAVING,
                  interpolator: TimeInterpolator = LinearInterpolator(),
                  startAction: ((View) -> Unit)? = null,
                  endAction: ((View) -> Unit)? = null) =
        AnimationUtil.slideOut(this, gravity, duration, interpolator, startAction, endAction)

/**
 * Анимация типа "пульс"
 */
fun View.pulseAnimation(scale: Float = 1.15f,
                        duration: Long = AnimationUtil.ANIM_PULSATION,
                        repeatCount: Int = ObjectAnimator.INFINITE,
                        repeatMode: Int = ObjectAnimator.REVERSE,
                        interpolator: TimeInterpolator = FastOutLinearInInterpolator()) =
        AnimationUtil.pulseAnimation(this, scale, duration, repeatCount, repeatMode, interpolator)

/**
 * Изменение ширины и высоты вью
 */
fun View.toSize(newWidth: Int, newHeight: Int,
                duration: Long = AnimationUtil.ANIM_TRANSITION) =
        AnimationUtil.newSize(this, newWidth, newHeight, duration)
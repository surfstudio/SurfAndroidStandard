package ru.surfstudio.android.animations.anim

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.support.transition.*
import android.support.v4.view.ViewCompat
import android.support.v4.view.animation.FastOutLinearInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

/**
 * Утилиты для работы с анимацией
 */
object AnimationUtil {

    /**
     * Смена двух вью с эффектом fadeIn/fadeOut
     */
    fun crossfadeViews(inView: View, outView: View,
                       duration: Long = 1000L,
                       endAction: (() -> Unit)? = null) {
        fadeIn(inView, duration, endAction)
        fadeOut(outView, duration, endAction)
    }

    /**
     * Сокрытие вью с изменением прозрачности
     */
    fun fadeOut(outView: View, duration: Long = 1000L, endAction: (() -> Unit)? = null) {
        ViewCompat.animate(outView)
                .alpha(0f)
                .setDuration(duration)
                .setInterpolator(LinearOutSlowInInterpolator())
                .withEndAction {
                    outView.visibility = View.GONE
                    endAction?.invoke()
                }
    }

    /**
     * Появление вью с изменением прозрачности
     */
    fun fadeIn(inView: View, duration: Long = 1000L, endAction: (() -> Unit)? = null) {
        inView.alpha = 0f
        inView.visibility = View.VISIBLE
        ViewCompat.animate(inView)
                .alpha(1f)
                .setDuration(duration)
                .setInterpolator(FastOutLinearInInterpolator())
                .withEndAction {
                    endAction?.invoke()
                }
    }


    /**
     * Анимация типа "пульс"
     */
    fun pulseAnimation(view: View,
                       scale: Float = 1.15f,
                       duration: Long = 600L,
                       repeatCount: Int = ObjectAnimator.INFINITE,
                       repeatMode: Int = ObjectAnimator.REVERSE,
                       interpolator: TimeInterpolator = FastOutLinearInInterpolator()): ObjectAnimator {
        val animation = ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat(View.SCALE_X, scale),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, scale))
        animation.duration = duration

        animation.repeatCount = repeatCount
        animation.repeatMode = repeatMode
        animation.interpolator = interpolator

        animation.start()

        return animation
    }

    /**
     * Изменение ширины и высоты вью
     */
    fun newSize(view: View,
                parentViewGroup: ViewGroup,
                newWidth: Int, newHeight: Int,
                duration: Long = 1000L) {
        val transition = TransitionSet()
                .addTransition(ChangeBounds())
        transition.duration = duration
        TransitionManager.beginDelayedTransition(parentViewGroup, transition)

        val lp = view.layoutParams
        lp.width = newWidth
        lp.height = newHeight
        view.layoutParams = lp
    }

    /**
     * Появление вью с эффектом "слайда" в зависимости от gravity
     */
    fun slideIn(view: View, container: ViewGroup, gravity: Int,
                duration: Long = 1000L,
                interpolator: TimeInterpolator = LinearInterpolator(),
                startAction: ((View) -> Unit)? = null,
                endAction: ((View) -> Unit)? = null) {
        if (view.visibility == View.GONE) {
            val slide = Slide(gravity)
            slide.addTarget(view)
            slide.duration = duration
            slide.interpolator = interpolator
            slide.addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: android.support.transition.Transition) {
                    endAction?.invoke(view)
                }

                override fun onTransitionResume(transition: android.support.transition.Transition) {
                }

                override fun onTransitionPause(transition: android.support.transition.Transition) {
                }

                override fun onTransitionCancel(transition: android.support.transition.Transition) {
                }

                override fun onTransitionStart(transition: android.support.transition.Transition) {
                    startAction?.invoke(view)
                }
            })

            TransitionManager.beginDelayedTransition(container, slide)
            view.visibility = View.VISIBLE
        }
    }

    /**
     * Исчезновение вью с эффектом "слайда" в зависимости от gravity
     */
    fun slideOut(view: View, container: ViewGroup, gravity: Int,
                 duration: Long = 1000L,
                 interpolator: TimeInterpolator = LinearInterpolator(),
                 startAction: ((View) -> Unit)? = null,
                 endAction: ((View) -> Unit)? = null) {
        if (view.visibility == View.VISIBLE) {
            val slide = Slide(gravity)
            slide.addTarget(view)
            slide.duration = duration
            slide.interpolator = interpolator
            slide.addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: android.support.transition.Transition) {
                    endAction?.invoke(view)
                }

                override fun onTransitionResume(transition: android.support.transition.Transition) {
                }

                override fun onTransitionPause(transition: android.support.transition.Transition) {
                }

                override fun onTransitionCancel(transition: android.support.transition.Transition) {
                }

                override fun onTransitionStart(transition: android.support.transition.Transition) {
                    startAction?.invoke(view)
                }
            })

            TransitionManager.beginDelayedTransition(container, slide)
            view.visibility = View.GONE
        }
    }
}
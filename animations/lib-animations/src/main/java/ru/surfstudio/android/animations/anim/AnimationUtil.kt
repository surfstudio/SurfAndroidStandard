/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.animations.anim

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.ViewCompat
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.*

/**
 * Утилиты для работы с анимацией
 */
object AnimationUtil {

    const val ANIM_ENTERING = 225L
    const val ANIM_LEAVING = 195L
    const val ANIM_LARGE_TRANSITION = 375L
    const val ANIM_TRANSITION = 300L
    const val ANIM_PULSATION = 600L

    /**
     * Смена двух вью с эффектом fadeIn/fadeOut
     */
    fun crossfadeViews(inView: View, outView: View,
                       duration: Long = ANIM_LARGE_TRANSITION,
                       visibility: Int = View.GONE,
                       endAction: (() -> Unit)? = null) {
        fadeIn(inView, duration, endAction = endAction)
        fadeOut(outView, duration, visibility, endAction = endAction)
    }


    /**
     * Сокрытие вью с изменением прозрачности
     * @param outView целевой view
     * @param duration длительность анимации (в мс)
     * @param endVisibility конечное значение видимости элемента
     * @param defaultAlpha  дефолтная прозрачность (следует переопределять, когда анимации вызываются часто,
     *                      и необходимо статичное значение)
     * @param endAction конечное действие
     */

    fun fadeOut(
            outView: View,
            duration: Long = ANIM_LEAVING,
            endVisibility: Int = View.GONE,
            defaultAlpha: Float = outView.alpha,
            endAction: (() -> Unit)? = null
    ) {
        outView.clearAnimation()
        ViewCompat.animate(outView)
                .alpha(0f)
                .setDuration(duration)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setListener(DefaultViewPropertyAnimatorListener())
                .withEndAction {
                    outView.visibility = endVisibility
                    outView.alpha = defaultAlpha
                    endAction?.invoke()
                }
    }

    /**
     * Появление вью с изменением прозрачности
     *
     * @param inView целевой view
     * @param duration длительность анимации (в мс)
     * @param defaultAlpha  дефолтная прозрачность (следует переопределять, когда анимации вызываются часто,
     *                      и необходимо статичное значение)
     * @param endAction заключительное действие
     */
    fun fadeIn(
            inView: View,
            duration: Long = ANIM_ENTERING,
            delay: Long = 0L,
            defaultAlpha: Float = inView.alpha,
            endAction: (() -> Unit)? = null
    ) {
        val animatorListener = DefaultViewPropertyAnimatorListener(inView.alpha, inView.visibility)

        inView.alpha = 0f
        inView.visibility = View.VISIBLE

        inView.clearAnimation()
        ViewCompat.animate(inView)
                .alpha(defaultAlpha)
                .setDuration(duration)
                .setStartDelay(delay)
                .setInterpolator(FastOutLinearInInterpolator())
                .setListener(animatorListener)
                .withEndAction {
                    endAction?.invoke()
                }
    }

    /**
     * Анимация типа "пульс"
     */
    fun pulseAnimation(view: View,
                       scale: Float = 1.15f,
                       duration: Long = ANIM_PULSATION,
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
    fun newSize(
            view: View,
            newWidth: Int,
            newHeight: Int,
            duration: Long = ANIM_TRANSITION,
            delay: Long = 0L
    ) {
        val transition: ChangeBounds = ChangeBounds().apply { startDelay = delay }
        val transitionSet: TransitionSet = TransitionSet().addTransition(transition)
        transitionSet.duration = duration
        beginTransitionSafe(view, transitionSet)

        val lp = view.layoutParams
        lp.width = newWidth
        lp.height = newHeight
        view.layoutParams = lp
    }

    /**
     * Появление вью с эффектом "слайда" в зависимости от gravity
     */
    fun slideIn(view: View,
                gravity: Int,
                duration: Long = ANIM_ENTERING,
                interpolator: TimeInterpolator = LinearInterpolator(),
                startAction: ((View) -> Unit)? = null,
                endAction: ((View) -> Unit)? = null) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            slide(view, gravity, duration, interpolator, endAction, startAction)
        }
    }

    /**
     * Исчезновение вью с эффектом "слайда" в зависимости от gravity
     */
    fun slideOut(view: View,
                 gravity: Int,
                 duration: Long = ANIM_LEAVING,
                 interpolator: TimeInterpolator = LinearInterpolator(),
                 startAction: ((View) -> Unit)? = null,
                 endAction: ((View) -> Unit)? = null) {
        if (view.visibility == View.VISIBLE) {
            slide(view, gravity, duration, interpolator, endAction, startAction)
            view.visibility = View.GONE
        }
    }

    private fun slide(view: View,
                      gravity: Int,
                      duration: Long,
                      interpolator: TimeInterpolator,
                      endAction: ((View) -> Unit)?,
                      startAction: ((View) -> Unit)?) {
        val slide = TransitionSet()
                .addTransition(Slide(gravity))
                .addTransition(ChangeBounds())
        slide.duration = duration
        slide.interpolator = interpolator
        slide.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition) {
                endAction?.invoke(view)
            }

            override fun onTransitionResume(transition: Transition) {
            }

            override fun onTransitionPause(transition: Transition) {
            }

            override fun onTransitionCancel(transition: Transition) {
            }

            override fun onTransitionStart(transition: Transition) {
                startAction?.invoke(view)
            }
        })

        beginTransitionSafe(view, slide)
    }

    private fun beginTransitionSafe(view: View, transition: TransitionSet) {
        if (view.parent !is ViewGroup) {
            throw ClassCastException("View.parent is not ViewGroup!")
        }
        TransitionManager.beginDelayedTransition(view.parent as ViewGroup, transition)
    }
}
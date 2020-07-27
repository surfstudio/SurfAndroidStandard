/*
  Copyright (c) 2020-present, SurfStudio LLC.

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
package ru.surfstudio.android.recycler.extension.slide

import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.core.view.isInvisible
import kotlinx.android.synthetic.main.layout_bindable_sliding_view_holder.view.*
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import ru.surfstudio.android.recycler.extension.slide.BindableSlidingViewHolder.InteractionState.*
import ru.surfstudio.android.recycler.extension.util.dpToPx
import ru.surfstudio.easyadapter.carousel.R
import kotlin.math.abs
import kotlin.math.roundToLong

/**
 * [BindableViewHolder] with possibility of horizontal sliding to reveal and fixate side-buttons.
 *
 * ## General features:
 *
 * * You can add buttons to left or/and right side of your `ViewHolder` by
 * overriding [leftButtons] and [rightButtons] fields (You can add as many buttons as you like,
 * but recommended limit of buttons for single `ViewHolder` is not more than 3 buttons in a row);
 *
 * * Those side-buttons accessible through swipe-gesture or horizontal scrolling to the edges;
 *
 * * You can bind data to your side-buttons just as you would bind it to your `ViewHolder`.
 * Use [onBindButton] method;
 *
 * * You can handle side-buttons `onClick` events in [onButtonClicked] method;
 *
 * * You can use [onSlidingStarted], [onSliding] and [onSlidingEnded] callback-methods to
 * override or customize sliding of `ViewHolder`;
 *
 * * You can override [onInterceptTouchEvent] to decide by yourself
 * when and why you want to intercept user's touch events;
 *
 * * If buttons not yet collapsed - they're gonna collapse in following conditions:
 *    * `ViewHolder` is detached from window;
 *    * User scrolling list vertically;
 *    * User clicked on content of current item;
 *    * User clicked on other item in list;
 *
 * **Note: [BindableSlidingViewHolder] won't work by itself!
 * To enable sliding functionality, please, use [SlidingHelper].**
 * */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BindableSlidingViewHolder<T : Any>(parent: ViewGroup, @LayoutRes layoutRes: Int) :
        BindableViewHolder<T>(parent, R.layout.layout_bindable_sliding_view_holder) {

    // Shortcuts
    private val currentSlideTranslation get() = contentContainer.translationX
    private val isReachedLeftContainer get() = currentSlideTranslation >= leftContainerTriggerPosition
    private val isReachedRightContainer get() = currentSlideTranslation <= rightContainerTriggerPosition
    private val hasLeftButtons get() = leftButtons.isNotEmpty()
    private val hasRightButtons get() = rightButtons.isNotEmpty()
    protected val context get() = itemView.context

    // Views
    private val leftButtonsContainer = itemView.sliding_left_buttons_container
    private val rightButtonsContainer = itemView.sliding_right_buttons_container
    private val contentContainer = itemView.sliding_content_container
    private val contentBlockerView = itemView.sliding_content_blocker_view

    // View groups
    private val slidingViews = listOf(contentContainer, leftButtonsContainer, rightButtonsContainer, contentBlockerView)
    private val buttonViews by lazy {
        listOf(leftButtonsContainer, rightButtonsContainer).flatMap { it.children.toList() }
    }

    /**
     *  Width of left buttons container. Also defines how far buttons should scroll to the right.
     *
     * **If we're using just `width`, not `width - 1` - we're getting little of empty space
     * between start of the screen and container. Code below resolves this issue.**
     * */
    private val leftContainerWidth by lazy { leftButtonsContainer.width.toFloat() - context.dpToPx(1) }

    /** Width of right buttons container. Also defines how far buttons should scroll to the left. */
    private val rightContainerWidth by lazy { rightButtonsContainer.width.toFloat() }

    /** Width of content container. */
    private val contentContainerWidth by lazy { contentContainer.width.toFloat() }

    /** Amount of translation used to decide is current state is [InteractionState.LEFT_BUTTONS] or not. */
    private val leftContainerTriggerPosition by lazy { leftContainerWidth / 2 }

    /** Amount of translation used to decide is current state is [InteractionState.RIGHT_BUTTONS] or not. */
    private val rightContainerTriggerPosition by lazy { -rightContainerWidth / 2 }

    /** Flag: are this `ViewHolder` has initialized it's side-buttons? */
    private var isInitialized = false

    /** Flag: are this `ViewHolder` is currently sliding by animation? */
    private var isAnimatingSlide = false

    /** Internal interaction state. */
    protected var interactionState = MAIN_CONTENT
        private set(value) {
            field = value
            contentBlockerView.isInvisible = value == MAIN_CONTENT
        }

    /** Animation duration of [InteractionState] changing (milliseconds). */
    protected open val slideAnimationDuration = SLIDE_ANIM_DURATION

    /** Amount of horizontal velocity (pixels), needed to change [InteractionState]. */
    protected open val swipeVelocityThreshold = context.dpToPx(SWIPE_VELOCITY_THRESHOLD_DP).toFloat()

    /** Amount of horizontal scrolled distance (pixels), needed to intercept user's touch. */
    protected open val touchInterceptThreshold = context.dpToPx(TOUCH_INTERCEPT_THRESHOLD_DP).toFloat()

    /** List of buttons to inflate into the left container. */
    protected open val leftButtons: List<View> = emptyList()

    /** List of buttons to inflate into the right container. */
    protected open val rightButtons: List<View> = emptyList()

    init {
        View.inflate(context, layoutRes, contentContainer)
        contentBlockerView.setOnClickListener { onContentBlockerClicked() }
    }

    /**
     * Callback, used to bind [data] to this `ViewHolder`.
     *
     * **Call `super.bind()` to bind data to buttons.**
     * */
    @CallSuper
    override fun bind(data: T) {
        bindButtons(data)
    }

    /** Callback, used to decide: are we intercepting this touch event of [RecyclerView]? */
    open fun onInterceptTouchEvent(touchTranslationX: Float): Boolean {
        return when (interactionState) {
            MAIN_CONTENT -> when {
                hasLeftButtons && touchTranslationX >= touchInterceptThreshold -> true
                hasRightButtons && touchTranslationX <= -touchInterceptThreshold -> true
                else -> false
            }
            RIGHT_BUTTONS -> when {
                touchTranslationX >= touchInterceptThreshold -> true
                else -> false
            }
            LEFT_BUTTONS -> when {
                touchTranslationX <= -touchInterceptThreshold -> true
                else -> false
            }
        }
    }

    /** Callback, used to response on just started user sliding gesture. */
    open fun onSlidingStarted() {
        /* empty body */
    }

    /** Callback, used to handle user sliding gesture. */
    open fun onSliding(touchTranslationX: Float) {
        relativeMoveContainersTo(touchTranslationX, shouldAnimate = false)
    }

    /** Callback, used to decide what to do when user ended his sliding gesture. */
    open fun onSlidingEnded(touchVelocityX: Float) {
        when (interactionState) {
            MAIN_CONTENT -> when {
                touchVelocityX >= swipeVelocityThreshold || isReachedLeftContainer -> showLeftButtons()
                touchVelocityX <= -swipeVelocityThreshold || isReachedRightContainer -> showRightButtons()
                else -> hideButtons()
            }
            RIGHT_BUTTONS -> when {
                touchVelocityX >= swipeVelocityThreshold && isReachedLeftContainer -> showLeftButtons()
                touchVelocityX >= swipeVelocityThreshold -> hideButtons()
                isReachedRightContainer -> showRightButtons()
                else -> hideButtons()
            }
            LEFT_BUTTONS -> when {
                touchVelocityX <= -swipeVelocityThreshold && isReachedRightContainer -> showRightButtons()
                touchVelocityX <= -swipeVelocityThreshold -> hideButtons()
                isReachedLeftContainer -> showLeftButtons()
                else -> hideButtons()
            }
        }
    }

    /** Reveal left buttons container and fixate it. */
    fun showLeftButtons(shouldAnimate: Boolean = true) {
        interactionState = LEFT_BUTTONS
        moveContainersTo(leftContainerWidth, shouldAnimate)
    }

    /** Reveal right buttons container and fixate it. */
    fun showRightButtons(shouldAnimate: Boolean = true) {
        interactionState = RIGHT_BUTTONS
        moveContainersTo(-rightContainerWidth, shouldAnimate)
    }

    /** Hide all button containers and focus content. */
    fun hideButtons(shouldAnimate: Boolean = true) {
        interactionState = MAIN_CONTENT
        moveContainersTo(0f, shouldAnimate)
    }

    /** Callback, used to bind [data] to the particular [buttonView]. */
    protected open fun onBindButton(data: T, buttonView: View) {
        /* empty body */
    }

    /**
     * Callback, used to response to user's [buttonView] click.
     *
     * Default implementation call's `hideButtons()` method.
     * */
    protected open fun onButtonClicked(buttonView: View) {
        hideButtons()
    }

    /**
     * Callback, used to response to user's click on [contentBlockerView].
     *
     * Default implementation call's `hideButtons()` method.
     * */
    protected open fun onContentBlockerClicked() {
        hideButtons()
    }

    protected fun relativeMoveContainersTo(relativeTranslation: Float, shouldAnimate: Boolean) {
        val translationOrigin = when (interactionState) {
            MAIN_CONTENT -> 0f
            LEFT_BUTTONS -> leftContainerWidth
            RIGHT_BUTTONS -> -rightContainerWidth
        }
        val shiftedTranslation = translationOrigin + relativeTranslation
        val normalizedTranslation = when {
            shiftedTranslation <= -rightContainerWidth -> -rightContainerWidth
            shiftedTranslation >= leftContainerWidth -> leftContainerWidth
            else -> shiftedTranslation
        }
        moveContainersTo(normalizedTranslation, shouldAnimate)
    }

    protected fun moveContainersTo(absoluteTranslation: Float, shouldAnimate: Boolean) {
        if (isAnimatingSlide) {
            slidingViews.forEach(View::clearAnimation)
            isAnimatingSlide = false
        }
        when {
            !shouldAnimate -> slidingViews.forEach { it.translationX = absoluteTranslation }
            else -> {
                val focusedContainerWidth = when {
                    absoluteTranslation > 0f -> leftContainerWidth
                    absoluteTranslation < 0f -> rightContainerWidth
                    else -> when {
                        currentSlideTranslation > 0f -> leftContainerWidth
                        currentSlideTranslation < 0f -> rightContainerWidth
                        else -> contentContainerWidth
                    }
                }
                val animDuration = calculateSlideAnimDuration(
                        containerWidth = focusedContainerWidth,
                        from = currentSlideTranslation,
                        to = absoluteTranslation,
                        duration = slideAnimationDuration
                )
                isAnimatingSlide = true
                slidingViews.forEach { slidingView ->
                    slidingView.animateSlideTo(
                            targetTranslation = absoluteTranslation,
                            duration = animDuration,
                            interpolator = DecelerateInterpolator(),
                            endAction = { isAnimatingSlide = false }
                    )
                }
            }
        }
    }

    private fun bindButtons(data: T) {
        if (!isInitialized) {
            leftButtons.forEach(leftButtonsContainer::addView)
            rightButtons.forEach(rightButtonsContainer::addView)
            buttonViews.forEach { button -> button.setOnClickListener { onButtonClicked(button) } }
            isInitialized = true
        }
        buttonViews.forEach { button -> onBindButton(data, button) }
    }

    private fun calculateSlideAnimDuration(
            containerWidth: Float,
            from: Float,
            to: Float,
            duration: Long
    ): Long {
        val distanceToTravel = abs(from - to)
        return (distanceToTravel / containerWidth * duration).roundToLong()
    }

    private fun View.animateSlideTo(
            targetTranslation: Float,
            duration: Long,
            interpolator: Interpolator,
            endAction: () -> Unit = { }
    ) {
        clearAnimation()
        animate().translationX(targetTranslation)
                .setDuration(duration)
                .setInterpolator(interpolator)
                .withEndAction(endAction)
                .start()
    }

    /**
     * Internal interaction state.
     *
     * @property MAIN_CONTENT User interact's with main content of `ViewHolder`.
     * @property LEFT_BUTTONS User interact's with left buttons (`ViewHolder` translated to the right).
     * @property RIGHT_BUTTONS User interact's with right buttons (`ViewHolder` translated to the left).
     * */
    protected enum class InteractionState {
        MAIN_CONTENT,
        LEFT_BUTTONS,
        RIGHT_BUTTONS
    }

    private companion object {
        const val SLIDE_ANIM_DURATION = 300L
        const val SWIPE_VELOCITY_THRESHOLD_DP = 600f
        const val TOUCH_INTERCEPT_THRESHOLD_DP = 40f
    }
}
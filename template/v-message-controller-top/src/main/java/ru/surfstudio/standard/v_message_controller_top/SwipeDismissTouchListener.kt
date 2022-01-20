package ru.surfstudio.standard.v_message_controller_top

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.*
import kotlin.math.abs

/**
 * [View.OnTouchListener] делающий [View] смахиваемой свайпом.
 *
 * @param mView вью для свайпа.
 * @param mCallbacks колбек который будет вызван после смахивания вью.
 */
internal class SwipeDismissTouchListener(
    private val mView: View,
    private val mCallbacks: DismissCallbacks
) : View.OnTouchListener {

    // Cached ViewConfiguration and system-wide constant values
    private val mSlop: Int
    private val mMinFlingVelocity: Int
    private val mAnimationTime: Long
    private var mViewWidth = 1 // 1 and not 0 to prevent dividing by zero
    private var mViewHeight = 1

    // Transient properties
    private var mDownX: Float = 0f
    private var mDownY: Float = 0f
    private var mSwiping: Boolean = false
    private var mSwipingSlop: Int = 0
    private var mVelocityTracker: VelocityTracker? = null
    private var mTranslationX: Float = 0f
    private var mTranslationY: Float = 0f

    init {
        val vc = ViewConfiguration.get(mView.context)
        mSlop = vc.scaledTouchSlop
        mMinFlingVelocity = vc.scaledMinimumFlingVelocity * 16
        mAnimationTime = mView.context.resources.getInteger(
            android.R.integer.config_shortAnimTime
        ).toLong()
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        // offset because the view is translated during swipe
        motionEvent.offsetLocation(mTranslationX, mTranslationY)

        if (mViewWidth < 2) {
            mViewWidth = mView.width
        }
        if (mViewHeight < 2) {
            mViewHeight = mView.height
        }

        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mDownX = motionEvent.rawX
                mDownY = motionEvent.rawY
                if (mCallbacks.canDismiss()) {
                    mVelocityTracker = VelocityTracker.obtain()
                    mVelocityTracker?.addMovement(motionEvent)
                }
                mCallbacks.onTouch(view, true)
                return false
            }
            MotionEvent.ACTION_UP -> {
                val deltaX = motionEvent.rawX - mDownX
                val deltaY = motionEvent.rawY - mDownY
                mVelocityTracker?.let { tracker: VelocityTracker ->
                    tracker.addMovement(motionEvent)
                    tracker.computeCurrentVelocity(1000)
                    val velocityX = tracker.xVelocity
                    val velocityY = tracker.yVelocity
                    val absVelocityX = abs(velocityX)
                    val absVelocityY = abs(tracker.yVelocity)
                    val isSwipedEnoughX = abs(deltaX) > mViewWidth / 4 && mSwiping
                    val isSwipedInTheDirectionOfFlingX = mMinFlingVelocity <= absVelocityX && absVelocityY < absVelocityX && mSwiping
                    val isSwipedHorizontally = isSwipedEnoughX || isSwipedInTheDirectionOfFlingX

                    val isSwipedEnoughY = abs(deltaY) > mViewHeight / 4 && mSwiping
                    val isSwipedInTheDirectionOfFlingY = mMinFlingVelocity <= absVelocityY && absVelocityY > absVelocityX && mSwiping
                    val isSwipedVertically = isSwipedEnoughY || isSwipedInTheDirectionOfFlingY // cancel

                    // dismiss only if flinging in the same direction as dragging
                    val dismiss = isSwipedEnoughX ||
                        velocityX < 0 == deltaX < 0 ||
                        isSwipedEnoughY ||
                        velocityY < 0 == deltaY < 0

                    if (dismiss) {
                        mView.animate().apply {
                            when {
                                isSwipedHorizontally -> {
                                    val dismissRight = deltaX > 0 || velocityX > 0
                                    val translationX = if (dismissRight) mViewWidth else -mViewWidth
                                    translationX(translationX.toFloat())
                                }
                                isSwipedVertically -> {
                                    val dismissUp = deltaY < 0 || velocityY < 0 == deltaX < 0
                                    val translationY = if (dismissUp) -mViewHeight else 0
                                    translationY(translationY.toFloat())
                                }
                            }
                        }
                            .alpha(0f)
                            .setDuration(mAnimationTime)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    performDismiss()
                                }
                            })
                    } else if (mSwiping) {
                        // cancel
                        animateSwipeCancelling()
                        mCallbacks.onTouch(view, false)
                    }
                    tracker.recycle()
                    resetTouchData()
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                mVelocityTracker?.let { tracker: VelocityTracker ->
                    animateSwipeCancelling()
                    tracker.recycle()
                    resetTouchData()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker?.run {
                    this.addMovement(motionEvent)
                    val deltaX = motionEvent.rawX - mDownX
                    val deltaY = motionEvent.rawY - mDownY
                    val isAlreadySwipedHorizontally = mTranslationX != 0f
                    val isAlreadySwipedVertically = mTranslationY != 0f
                    val isHorizontalSwipe = abs(deltaY) < abs(deltaX) / 2

                    mSwiping = abs(deltaX) > mSlop || abs(deltaY) > mSlop

                    if (mSwiping) {
                        mSwipingSlop = if (deltaX > 0) mSlop else -mSlop
                        mView.parent.requestDisallowInterceptTouchEvent(true)

                        // Cancel touch
                        val cancelEvent = MotionEvent.obtain(motionEvent)
                        cancelEvent.action = MotionEvent.ACTION_CANCEL or (motionEvent.actionIndex shl MotionEvent.ACTION_POINTER_INDEX_SHIFT)
                        mView.onTouchEvent(cancelEvent)
                        cancelEvent.recycle()

                        if (isHorizontalSwipe && !isAlreadySwipedVertically) {
                            mTranslationX = deltaX
                            mView.translationX = deltaX - mSwipingSlop
                        } else if (!isAlreadySwipedHorizontally) {
                            val isSwipeUp = deltaY < 0
                            mTranslationY = if (isSwipeUp) deltaY else 0f
                        }
                        return true
                    }
                }
            }
            else -> {
                view.performClick()
                return false
            }
        }
        return false
    }

    private fun animateSwipeCancelling() {
        mView.animate()
            .translationY(0f)
            .translationX(0f)
            .alpha(1f)
            .setDuration(mAnimationTime)
            .setListener(null)
    }

    private fun resetTouchData() {
        mVelocityTracker = null
        mTranslationX = 0f
        mTranslationY = 0f
        mDownX = 0f
        mDownY = 0f
        mSwiping = false
    }

    private fun performDismiss() {
        // Animate the dismissed view to zero-height and then fire the dismiss callback.
        // This triggers layout on each animation frame; in the future we may want to do something
        // smarter and more performant.

        val lp = mView.layoutParams
        val originalHeight = mView.height

        val animator = ValueAnimator.ofInt(originalHeight, 1).setDuration(mAnimationTime)

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mCallbacks.onDismiss(mView)
                // Reset view presentation
                mView.alpha = 1f
                mView.translationX = 0f
                lp.height = originalHeight
                mView.layoutParams = lp
            }
        })

        animator.addUpdateListener { valueAnimator ->
            lp.height = valueAnimator.animatedValue as Int
            mView.layoutParams = lp
        }

        animator.start()
    }

    /**
     * The callback interface used by [SwipeDismissTouchListener] to inform its client
     * about a successful dismissal of the view for which it was created.
     */
    internal interface DismissCallbacks {
        /**
         * Called to determine whether the view can be dismissed.
         *
         * @return boolean The view can dismiss.
         */
        fun canDismiss(): Boolean

        /**
         * Called when the user has indicated they she would like to dismiss the view.
         *
         * @param view The originating [View]
         */
        fun onDismiss(view: View)

        /**
         * Called when the user touches the view or release the view.
         *
         * @param view The originating [View]
         * @param touch The view is being touched.
         */
        fun onTouch(view: View, touch: Boolean)
    }
}

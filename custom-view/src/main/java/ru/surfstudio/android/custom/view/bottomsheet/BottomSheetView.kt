/*
  Copyright (c) 2019-present, SurfStudio LLC.

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
package ru.surfstudio.android.custom.view.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import ru.surfstudio.android.custom.view.R
import kotlin.properties.Delegates

/**
 * BottomSheet Persistent. To implement:
 * parent container must be the [CoordinatorLayout]
 * specify [BottomSheetBehavior] (i.e.: app:layout_behavior="...")
 *
 * @param defaultBehaviorState - default [BottomSheetBehavior] state
 */
open class BottomSheetView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defaultBehaviorState: Int = STATE_COLLAPSED
) : LinearLayout(context, attributeSet) {

    /**
     * Returns true if the view is expanded, else returns false
     */
    val isExpanded: Boolean get() = behavior?.state == STATE_EXPANDED

    /**
     * Returns true if the view is collapsed, else returns false
     */
    val isCollapsed: Boolean get() = behavior?.state == STATE_COLLAPSED

    /**
     * Returns true if the view is hidden, else returns false
     */
    val isHidden: Boolean get() = behavior?.state == STATE_HIDDEN

    /**
     * Callback for state expanded
     * will be called on maximum height of the view
     */
    var onExpandListener: (() -> Unit)? = null

    /**
     * Callback for state collapsed
     * will be called on default height of the view
     */
    var onCollapseListener: (() -> Unit)? = null

    /**
     * Callback for state hidden
     * will be called on view hidden
     */
    var onHideListener: (() -> Unit)? = null

    /**
     * Callback for initializing behavior of the view
     */
    var behaviorInitCallback: (() -> Unit)? = null

    /**
     * The behavior of the view
     */
    var behavior: BottomSheetBehavior<View>? = null
        private set

    private var state: Int
            by Delegates.observable(defaultBehaviorState) { _, _, newValue ->
                behavior?.let { it.state = newValue }
            }

    init {
        background = ColorDrawable(ContextCompat.getColor(context, android.R.color.white))
        orientation = VERTICAL
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
            elevation = dip(16)
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        initViews()
    }

    /**
     * Makes the view default height
     */
    @CallSuper
    open fun collapse() {
        state = STATE_COLLAPSED
    }

    /**
     * Makes the view maximum height
     */
    @CallSuper
    open fun expand() {
        state = STATE_EXPANDED
    }

    @CallSuper
    open fun onExpand() {
        onExpandListener?.invoke()
    }

    @CallSuper
    open fun onHide() {
        onHideListener?.invoke()
    }

    @CallSuper
    open fun onCollapse() {
        onCollapseListener?.invoke()
    }

    /**
     * Hides the view
     */
    @CallSuper
    open fun hide() {
        state = STATE_HIDDEN
    }

    /**
     * Specify default height of the view for state [STATE_COLLAPSED]
     * @param peekHeight - height in px
     */
    fun setPeekHeight(peekHeight: Int) {
        behavior?.peekHeight = peekHeight
    }

    private fun initViews() {
        initBottomSheetBehavior()
    }

    private fun initBottomSheetBehavior() {
        if (behavior == null) {
            behavior = from<View>(this)
                    .also { it.state = state }

            behavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    //do nothing
                }

                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        STATE_HIDDEN -> onHide()
                        STATE_EXPANDED -> onExpand()
                        STATE_COLLAPSED -> onCollapse()
                        else -> {
                            //do nothing
                        }
                    }
                    state = newState
                }
            })
            behaviorInitCallback?.invoke()
        }
    }

    private fun dip(dp: Int): Float {
        return dp * context.resources.displayMetrics.density
    }
}
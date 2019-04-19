package ru.surfstudio.android.custom.view.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.getDrawable
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import ru.surfstudio.android.template.base_feature.R
import kotlin.properties.Delegates

/**
 * Базовый BottomSheet Persistent. Для реализации необходимо:
 * * чтобы родительский контейнер на экране был [CoordinatorLayout]
 * * наследнику добавить [BottomSheetBehavior] (пр.: app:layout_behavior="...")
 *
 * @param defaultBehaviorState - состояние [BottomSheetBehavior] по умолчанию
 */
open class BottomSheetView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        defaultBehaviorState: Int = STATE_COLLAPSED
) : LinearLayout(context, attributeSet) {

    val isExpanded: Boolean get() = behavior?.state == STATE_EXPANDED
    val isCollapsed: Boolean get() = behavior?.state == STATE_COLLAPSED
    val isHidden: Boolean get() = behavior?.state == STATE_HIDDEN

    var onExpandListener: (() -> Unit)? = null //вью на максимальной высоте
    var onCollapseListener: (() -> Unit)? = null //вью на стандартной высоте
    var onHideListener: (() -> Unit)? = null //вью скрыто

    var behaviorInitCallback: (() -> Unit)? = null //callback на инициализацию behavior'а

    var behavior: BottomSheetBehavior<View>? = null
        private set

    private var state: Int
            by Delegates.observable(defaultBehaviorState) { _, _, newValue ->
                behavior?.let { it.state = newValue }
            }

    init {
        background = getDrawable(context, R.drawable.bg_light_rounded_top_w_shadow)
        orientation = VERTICAL
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        initViews()
    }

    /**
     * разворачивает вью до стандартной высоты
     */
    @CallSuper
    open fun collapse() {
        state = STATE_COLLAPSED
    }

    /**
     * полностью разворачивает вью до максимальной высоты
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
     * скрывает вью
     */
    @CallSuper
    open fun hide() {
        state = STATE_HIDDEN
    }

    /**
     * Устанавливает стандартную высоту для view
     * * необходимо для сост. [STATE_COLLAPSED]
     * @param peekHeight - высота в dp
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
}
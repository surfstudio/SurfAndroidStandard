package ru.surfstudio.android.custom.view.placeholder

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.animations.anim.fadeIn
import ru.surfstudio.android.animations.anim.fadeOut
import ru.surfstudio.android.utilktx.util.SdkUtils
import java.util.concurrent.TimeUnit

const val ALPHA_FULL = 1f
const val DEFAULT_DURATION = 0L
const val STATE_TOGGLE_DELAY_MS = 250L

/**
 * Контейнер для смены вью, представляющих определенный LoadState
 */
class PlaceHolderViewContainer(
        context: Context,
        attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    private var loadStateSubject: PublishSubject<StatePresentation> = PublishSubject.create()

    private var stateDisposable = Disposables.disposed()

    init {
        layoutTransition = LayoutTransition()
        moveOnTop()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        stateDisposable = loadStateSubject.debounce(STATE_TOGGLE_DELAY_MS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe {
                    removeAllViews()
                    addView(it.stateView)
                }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        if (!stateDisposable.isDisposed) {
            stateDisposable.dispose()
        }
    }

    fun changeViewTo(view: View) = loadStateSubject.onNext(StatePresentation(view))

    fun show() = fadeIn(DEFAULT_DURATION, ALPHA_FULL)

    fun hide() = fadeOut(DEFAULT_DURATION, View.GONE, ALPHA_FULL)

    /**
     * Этот метод нужен для того чтобы placeholder всегда отображался выше всех остальных элементов
     */
    @SuppressLint("NewApi")
    private fun moveOnTop() {
        SdkUtils.runOnLollipop {
            z = Float.MAX_VALUE
        }
    }

    /**
     * Сущность, представляющая состояние в виде вью или пустого объекта
     */
    data class StatePresentation(val stateView: View)
}

/**
 * Метод используется в тех случаях, когда PlaceHolderViewContainer должен перехватывать клики
 */
fun PlaceHolderViewContainer.setClickableAndFocusable(value: Boolean) {
    isClickable = value
    isFocusable = value
    isFocusableInTouchMode = value
}
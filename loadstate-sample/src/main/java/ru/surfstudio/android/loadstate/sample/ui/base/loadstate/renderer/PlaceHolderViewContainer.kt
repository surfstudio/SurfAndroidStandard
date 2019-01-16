package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import ru.surfstudio.android.animations.anim.fadeIn
import ru.surfstudio.android.animations.anim.fadeOut
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Контейнер для смены вью, представляющих определенный LoadState
 */
class PlaceHolderViewContainer(
        context: Context,
        attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    companion object {
        const val ALPHA_FULL = 1f
        const val ALPHA_TRANSPARENT = 0f
        const val DEFAULT_DURATION = 300L
        const val STATE_TOGGLE_DELAY_MS = 250L
    }

    private var loadStateSubject: PublishSubject<StatePresentation> = PublishSubject.create()

    init {
        layoutTransition = LayoutTransition()

        val isFirstEmission = AtomicBoolean(true)
        loadStateSubject.debounce { t ->
            Observable.just(t)?.apply {
                if (isFirstEmission.getAndSet(false).not()) {
                    debounce(STATE_TOGGLE_DELAY_MS, TimeUnit.MILLISECONDS)
                }
            }
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    removeAllViews()
                    it.stateView?.let { addView(it) }
                }
    }

    fun changeViewTo(view: View? = null) {
        loadStateSubject.onNext(StatePresentation(view))
    }

    fun changeVisibility(visibilityTo: Int) {
        if (isVisible && (visibilityTo != View.VISIBLE)) {
            fadeOut(DEFAULT_DURATION, visibilityTo, ALPHA_TRANSPARENT)
            return
        }

        if ((visibility != View.VISIBLE) && (visibilityTo == View.VISIBLE)) {
            fadeIn(DEFAULT_DURATION, ALPHA_FULL)
        }
    }

    /**
     * Сущность, представляющая состояние в виде вью или пустого объекта
     */
    class StatePresentation(val stateView: View?)
}
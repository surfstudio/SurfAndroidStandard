package ru.surfstudio.android.sample.common.ui.base.loadstate.renderer

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
import ru.surfstudio.android.logger.Logger
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

class PlaceHolderViewContainer(
        context: Context,
        attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    companion object {
        const val DEFAULT_DURATION = 300L
        const val STATE_TOGGLE_DELAY_MS: Long = 250
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
            fadeOut(DEFAULT_DURATION, visibilityTo)
            return
        }

        if ((visibility != View.VISIBLE) && (visibilityTo == View.VISIBLE)) {
            fadeIn(DEFAULT_DURATION)
        }
    }

    class StatePresentation(val stateView: View?)
}
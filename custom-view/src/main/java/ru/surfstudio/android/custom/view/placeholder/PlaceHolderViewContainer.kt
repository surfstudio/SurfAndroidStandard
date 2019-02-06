/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    fun show() = fadeIn(DEFAULT_DURATION)

    fun hide() = fadeOut(DEFAULT_DURATION, View.GONE)

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
    class StatePresentation(val stateView: View)
}

/**
 * Метод используется в тех случаях, когда PlaceHolderViewContainer должен перехватывать клики
 */
fun PlaceHolderViewContainer.setClickableAndFocusable(value: Boolean) {
    isClickable = value
    isFocusable = value
    isFocusableInTouchMode = value
}
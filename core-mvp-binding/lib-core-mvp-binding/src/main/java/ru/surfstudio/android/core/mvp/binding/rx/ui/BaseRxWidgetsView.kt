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

package ru.surfstudio.android.core.mvp.binding.rx.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.CallSuper
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.mvp.widget.view.CoreConstraintLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreFrameLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreLinearLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreRelativeLayoutView
import ru.surfstudio.android.rx.extension.scheduler.MainThreadImmediateScheduler

/**
 * Базовый класс для Widget с поддержкой rx-биндингов на основе [FrameLayout] .
 * <p>
 * Для использования виджетов в RecyclerView, необходимо переопределить метод getWidgetId так,
 * чтобы он получал значение из данных, получаемых в методе bind() у ViewHolder.
 */
abstract class CoreRxFrameLayoutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    CoreFrameLayoutView(context, attrs, defStyleAttr), BindableRxView {

    private val viewDisposable = CompositeDisposable()

    abstract fun onBind()

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onBind()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        viewDisposable.clear()
        super.onDetachedFromWindow()
    }

    override fun <T> subscribe(
        observable: Observable<out T>,
        onNext: Consumer<T>,
        onError: (Throwable) -> Unit
    ): Disposable =
        observable.observeOn(MainThreadImmediateScheduler)
            .subscribe(onNext, Consumer(onError))
            .also { viewDisposable.add(it) }
}

/**
 * Базовый класс для Widget с поддержкой rx-биндингов на основе [LinearLayout] .
 * <p>
 * Для использования виджетов в RecyclerView, необходимо переопределить метод getWidgetId так,
 * чтобы он получал значение из данных, получаемых в методе bind() у ViewHolder.
 */
abstract class CoreRxLinearLayoutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    CoreLinearLayoutView(context, attrs, defStyleAttr), BindableRxView {

    private val viewDisposable = CompositeDisposable()

    abstract fun onBind()

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onBind()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        viewDisposable.clear()
        super.onDetachedFromWindow()
    }

    override fun <T> subscribe(
        observable: Observable<out T>,
        onNext: Consumer<T>,
        onError: (Throwable) -> Unit
    ): Disposable =
        observable.observeOn(MainThreadImmediateScheduler)
            .subscribe(onNext, Consumer(onError))
            .also { viewDisposable.add(it) }
}

/**
 * Базовый класс для Widget с поддержкой rx-биндингов на основе [RelativeLayout] .
 * <p>
 * Для использования виджетов в RecyclerView, необходимо переопределить метод getWidgetId так,
 * чтобы он получал значение из данных, получаемых в методе bind() у ViewHolder.
 */
abstract class CoreRxRelativeLayoutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    CoreRelativeLayoutView(context, attrs, defStyleAttr), BindableRxView {

    private val viewDisposable = CompositeDisposable()

    abstract fun onBind()

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onBind()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        viewDisposable.clear()
        super.onDetachedFromWindow()
    }

    override fun <T> subscribe(
        observable: Observable<out T>,
        onNext: Consumer<T>,
        onError: (Throwable) -> Unit
    ): Disposable =
        observable.observeOn(MainThreadImmediateScheduler)
            .subscribe(onNext, Consumer(onError))
            .also { viewDisposable.add(it) }
}

/**
 * Базовый класс для Widget с поддержкой rx-биндингов на основе [ConstraintLayout] .
 * <p>
 * Для использования виджетов в RecyclerView, необходимо переопределить метод getWidgetId так,
 * чтобы он получал значение из данных, получаемых в методе bind() у ViewHolder.
 */
abstract class CoreRxConstraintLayoutView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    CoreConstraintLayoutView(context, attrs, defStyleAttr), BindableRxView {

    private val viewDisposable = CompositeDisposable()

    abstract fun onBind()

    @CallSuper
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        onBind()
    }

    @CallSuper
    override fun onDetachedFromWindow() {
        viewDisposable.clear()
        super.onDetachedFromWindow()
    }

    override fun <T> subscribe(
        observable: Observable<out T>,
        onNext: Consumer<T>,
        onError: (Throwable) -> Unit
    ): Disposable =
        observable.observeOn(MainThreadImmediateScheduler)
            .subscribe(onNext, Consumer(onError))
            .also { viewDisposable.add(it) }
}
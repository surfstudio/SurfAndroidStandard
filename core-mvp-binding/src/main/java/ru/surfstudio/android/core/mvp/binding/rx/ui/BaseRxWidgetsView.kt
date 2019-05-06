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
import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.mvp.widget.view.CoreConstraintLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreFrameLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreLinearLayoutView
import ru.surfstudio.android.mvp.widget.view.CoreRelativeLayoutView

/**
 * Классы для работы с виджетами через биндинг
 */
abstract class CoreRxFrameLayoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
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

    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable =
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext, Consumer(onError))
                    .also { viewDisposable.add(it) }
}

abstract class CoreRxLinearLayoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
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

    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable =
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext, Consumer(onError))
                    .also { viewDisposable.add(it) }
}

abstract class CoreRxRelativeLayoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
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

    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable =
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext, Consumer(onError))
                    .also { viewDisposable.add(it) }
}

abstract class CoreRxConstraintLayoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
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

    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable =
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext, Consumer(onError))
                    .also { viewDisposable.add(it) }
}
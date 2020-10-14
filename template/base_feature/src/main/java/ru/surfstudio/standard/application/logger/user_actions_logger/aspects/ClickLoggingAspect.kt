@file:Suppress("unused")

package ru.tricolor.android.application.logger.user_actions_logger.aspects

import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import androidx.recyclerview.widget.SnapHelper
import io.reactivex.Observable
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.*
import ru.tricolor.android.application.logger.user_actions_logger.ViewClick

/**
 * Аспект, отвечающий за логгирование кликов
 */
@Aspect
class ClickLoggingAspect : BaseAspect() {

    /**
     *  Обработчик вызова конструктора класса SnapOnScrollListener
     *
     *  @param snapHelper - используемый в классе SnapHelper
     */
    @Before("set(TextView *.*) && args(view)")
    fun afterSnapPositionFieldAccess(view: View) {
        view.setOnFocusChangeListener { _, _ ->  }
    }

    /**
     *  Стандартный обработчик кликов
     *
     *  @param view - кликабельная View
     */
    @Before("execution(void *.onClick(..)) && args(view)")
    fun onDefaultClickAdvice(view: View) {
        log(ViewClick(view).toString())
    }

    @Before("execution(void *.onFocusChange(..)) && args(view, isFocused)")
    fun onEditTextClickAdvice(view: View, isFocused: Boolean) {
        if (isFocused) {
            log(ViewClick(view).toString())
        }
    }

    /**
     * Обработчик вызова метода clicks() RxBinding
     *
     * @param context - информация о точке вызова метода
     * @param view - кликабельная View
     */
    @Around("call(io.reactivex.Observable com.jakewharton.rxbinding2.view.RxView.clicks(..)) && args(view)")
    fun onRxBindingClickAdvice(context: ProceedingJoinPoint, view: View): Any {
        return (context.proceed() as Observable<*>).doOnNext {
            log(ViewClick(view).toString())
        }
    }

    /**
     * Обработчик метода onCheckedChanged(..)
     *
     * @param view - чекбокс/свитчер
     */
    @Before("execution(void *.onCheckedChanged(..)) && args(view, ..)")
    fun onCheckedAdvice(view: CompoundButton) {
        log(ViewClick(view).toString())
    }
}
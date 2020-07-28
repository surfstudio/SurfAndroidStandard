@file:Suppress("unused")

package ru.surfstudio.standard.application.logger.user_actions_logger

import android.view.View
import io.reactivex.Observable
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import timber.log.Timber

/**
 * The aspect that implements the logic of logging user actions
 */
@Aspect
class UserActionsLoggingAspect {

    /**
     *  Default click handler
     */
    @Before("execution(void *.onClick(..)) && args(view)")
    fun onClickAdvice(view: View) = log(ViewClick(view).toString())

    /**
     * RxBinding click handler
     */
    @Around("call(io.reactivex.Observable com.jakewharton.rxbinding2.view.RxView.clicks(..)) && args(view)")
    fun onClickAdvice(joinPoint: ProceedingJoinPoint, view: View): Any {
        return (joinPoint.proceed() as Observable<*>).doOnNext {
            log(ViewClick(view).toString())
        }
    }

    private fun log(message: String) {
        Timber.tag(TAG)
        Timber.d(message)
    }

    companion object {
        private const val TAG = "ViewEvent"
    }
}
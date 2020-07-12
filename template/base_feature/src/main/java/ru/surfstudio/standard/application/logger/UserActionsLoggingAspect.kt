@file:Suppress("unused")

package ru.surfstudio.standard.application.logger

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
    fun onClickAdvice(view: View) {
        logClick(view)
    }

    /**
     * RxBinding click handler
     */
    @Around("call(* *.clicks(..))")
    fun onClickAdvice(joinPoint: ProceedingJoinPoint): Any {
        val view = joinPoint.args[0] as View
        return (joinPoint.proceed() as Observable<*>).doOnNext {
            logClick(view)
        }
    }

    private fun logClick(view: View) {
        val location = IntArray(2).apply {
            view.getLocationOnScreen(this)
        }

        log(
                "id: ${view.resources.getResourceEntryName(view.id)}\n" +
                        "x: ${location[0]}\n" +
                        "y: ${location[1]}\n"
        )
    }

    private fun log(message: String) {
        Timber.tag(TAG)
        Timber.d(message)
    }

    companion object {
        private const val TAG = "ViewEvent"
    }
}
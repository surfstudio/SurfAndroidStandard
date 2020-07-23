@file:Suppress("unused")

package ru.surfstudio.standard.application.logger.user_actions_logger

import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import io.reactivex.Observable
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import timber.log.Timber
import java.lang.reflect.Method

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

    private fun log(message: String) {
        Timber.tag(TAG).d(message)
    }

    companion object {
        private const val TAG = "ViewEvent"
    }
}
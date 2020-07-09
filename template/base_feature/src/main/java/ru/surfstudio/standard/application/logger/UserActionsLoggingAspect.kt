@file:Suppress("unused")

package ru.surfstudio.standard.application.logger

import android.view.View
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import timber.log.Timber

/**
 * The aspect that implements the logic of logging user actions
 */
@Aspect
class UserActionsLoggingAspect {

    /**
     * View click method template
     */
    @Pointcut("execution(void *.onClick(..))")
    fun onViewClick() {
        //do nothing
    }

    /**
     * View click handler
     */
    @Before("onViewClick() && args(view)")
    fun onClickAdvice(view: View) {
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
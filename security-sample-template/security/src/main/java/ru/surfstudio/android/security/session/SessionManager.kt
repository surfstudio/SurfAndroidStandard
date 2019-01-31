package ru.surfstudio.android.security.session

import android.app.Activity
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

//TODO лицензия
class SessionManager(private val sessionController: SessionController) {

    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: ScheduledFuture<*>? = null
    private val activeActivityCount = AtomicInteger(0)
    private val onSessionExpiredAction = Runnable {
        //todo add action
    }

    fun onActivityStarted(activity: Activity) {
        activeActivityCount.incrementAndGet()
    }

    fun onActivityStopped() {
        activeActivityCount.decrementAndGet()

    }

    private fun scheduleSessionExpiredTask() {
        cancelScheduledTask()
        scheduledFuture = executorService.schedule(
                onSessionExpiredAction,
                sessionController.getSessionDurationInMillis(),
                TimeUnit.MILLISECONDS
        )
    }

    private fun cancelScheduledTask() {
        scheduledFuture?.cancel(true)
    }

    private fun shouldSessionConfirm(activity: Activity): Boolean =
         (activity is SessionalActivity && activity.shouldSessionConfirm())
}
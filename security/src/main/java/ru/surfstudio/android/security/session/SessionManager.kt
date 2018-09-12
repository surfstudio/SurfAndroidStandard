package ru.surfstudio.android.security.session

import android.app.Activity
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Класс, отвечающий за необходимость сброса сессии.
 * Смотрит на количество активных экранов и по ним определяет, в фоне приложение или нет.
 * Если приложение уходит в фон (нет ни одной активити после вызова resumed), то запускает счетчик,
 * по прошествии которого сбрасывается сессия.
 */
class SessionManager(private val sessionChangedInteractor: SessionChangedInteractor,
                     private val blockingTimeSec: Long) {

    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: ScheduledFuture<*>? = null

    private var invalidateSession = false

    private val executorRunnable = {
        invalidateSession = activeActivityCount.get() == 0
    }

    private val activeActivityCount = AtomicInteger(0)

    fun onActivityStarted(activity: Activity) {
        activeActivityCount.incrementAndGet()
        cancelSchedule()

        if (!isSessionFree(activity) && invalidateSession) {
            sessionChangedInteractor.onSessionInvalid()
        }

        invalidateSession = false
    }

    fun onActivityStopped() {
        activeActivityCount.decrementAndGet()
        schedule()
    }

    private fun schedule() {
        cancelSchedule()
        scheduledFuture = executorService.schedule(executorRunnable, blockingTimeSec, TimeUnit.SECONDS)
    }

    private fun cancelSchedule() {
        scheduledFuture?.cancel(true)
    }

    private fun isSessionFree(obj: Any) = obj is SessionFree
}
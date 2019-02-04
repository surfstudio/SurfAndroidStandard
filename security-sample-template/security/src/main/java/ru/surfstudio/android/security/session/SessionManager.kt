/*
  Copyright (c) 2018-present, SurfStudio LLC, Margarita Volodina, Oleg Zhilo.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.security.session

import android.app.Activity
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * Менеджер сессии Activity
 * */
class SessionManager(private val sessionController: SessionController) {

    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: ScheduledFuture<*>? = null
    private val activeActivityCount = AtomicInteger(0)
    private var hasExpiredSession = false
    private val onSessionExpiredAction = Runnable {
        hasExpiredSession = true
    }

    fun onActivityStarted(activity: Activity) {
        activeActivityCount.incrementAndGet()

        if(shouldSessionConfirm(activity) && hasExpiredSession) {
            sessionController.onSessionExpired()
        }

        hasExpiredSession = false
        cancelScheduledTask()
    }

    fun onActivityStopped() {
        val hasGoneLastActivity = activeActivityCount.decrementAndGet() == 0

        if(hasGoneLastActivity && hasExpiredSession.not()) {
            scheduleSessionExpiredTask()
        }
    }

    private fun scheduleSessionExpiredTask() {
        cancelScheduledTask()
        scheduledFuture = executorService.schedule(
                onSessionExpiredAction,
                sessionController.getDurationMillis(),
                TimeUnit.MILLISECONDS
        )
    }

    private fun cancelScheduledTask() {
        scheduledFuture?.cancel(true)
        scheduledFuture = null
    }

    private fun shouldSessionConfirm(activity: Activity): Boolean =
         (activity is SessionalActivity && activity.shouldSessionConfirm())
}
/*
  Copyright (c) 2018-present, SurfStudio LLC.

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
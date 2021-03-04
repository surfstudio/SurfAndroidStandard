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
package ru.surfstudio.android.rx.extension.scheduler

import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * [Scheduler] that executes work immediately when in main thread,
 * or switches thread to main and pushes work to the end of the MessageQueue when in other thread.
 */
object MainThreadImmediateScheduler : Scheduler() {

    private val immediateScheduler = Schedulers.trampoline()
    private val mainThreadScheduler = AndroidSchedulers.mainThread()

    override fun createWorker(): Worker {
        return MainThreadImmediateWorker(
                immediateScheduler.createWorker(),
                mainThreadScheduler.createWorker()
        )
    }

    private class MainThreadImmediateWorker(
            private val immediateWorker: Worker,
            private val mainWorker: Worker
    ) : Worker() {

        override fun isDisposed(): Boolean {
            return immediateWorker.isDisposed && mainWorker.isDisposed
        }

        override fun schedule(run: Runnable, delay: Long, unit: TimeUnit): Disposable {
            return if (isMainThread()) {
                immediateWorker.schedule(run, delay, unit)
            } else {
                mainWorker.schedule(run, delay, unit)
            }
        }

        override fun dispose() {
            mainWorker.dispose()
            immediateWorker.dispose()
        }

        private fun isMainThread() = Thread.currentThread() == Looper.getMainLooper().thread
    }
}
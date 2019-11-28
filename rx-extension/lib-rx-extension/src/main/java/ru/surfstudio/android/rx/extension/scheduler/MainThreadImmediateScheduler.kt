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

    class MainThreadImmediateWorker(
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
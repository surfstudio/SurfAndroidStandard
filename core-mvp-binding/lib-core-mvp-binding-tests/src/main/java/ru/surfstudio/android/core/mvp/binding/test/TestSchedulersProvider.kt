package ru.surfstudio.android.core.mvp.binding.test

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Тестовая реализация [SchedulersProvider]
 */
class TestSchedulersProvider : SchedulersProvider {

    override fun main(): Scheduler = Schedulers.trampoline()

    override fun worker(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()
}

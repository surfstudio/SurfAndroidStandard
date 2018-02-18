package ru.surfstudio.android.rx.extension.scheduler;


import io.reactivex.Scheduler;

public interface SchedulersProvider {
    Scheduler main();

    Scheduler worker();
}

package ru.surfstudio.android.core.app.scheduler;


import io.reactivex.Scheduler;

public interface SchedulersProvider {
    Scheduler main();

    Scheduler worker();
}

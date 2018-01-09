package ru.surfstudio.android.core.app.scheduler;

import rx.Scheduler;

public interface SchedulersProvider {
    Scheduler main();

    Scheduler worker();
}

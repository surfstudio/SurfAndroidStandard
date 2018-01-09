package ru.surfstudio.android.core.app.scheduler;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * провайдер Schedulers для Rx
 */
public class SchedulersProviderImpl implements SchedulersProvider {

    @Override
    public Scheduler main(){
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler worker(){
        return Schedulers.io();
    }
}

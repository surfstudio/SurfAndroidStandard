package ru.surfstudio.android.core.app.scheduler;


import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

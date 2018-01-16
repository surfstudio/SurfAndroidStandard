package ru.surfstudio.android.core.app.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Receiver событий появления/исчезновния соединения
 */

public class ConnectionReceiver extends BroadcastReceiver {

    private PublishSubject<Boolean> connectionStateSubject = PublishSubject.create();
    private boolean isConnected;

    public ConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();// возвращает null при отсутствии соединения
        isConnected = netInfo != null && netInfo.isConnected();
        connectionStateSubject.onNext(isConnected);
    }

    public Observable<Boolean> observeConnectionChanges() {
        return connectionStateSubject;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
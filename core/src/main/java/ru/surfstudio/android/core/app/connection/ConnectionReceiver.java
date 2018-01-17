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

    private Context context;
    private PublishSubject<Boolean> connectionStateSubject = PublishSubject.create();
    private boolean isConnected;

    public ConnectionReceiver(Context context) {
        this.context = context;
        this.isConnected = checkActiveConnection();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        isConnected = checkActiveConnection();
        connectionStateSubject.onNext(isConnected);
    }

    public Observable<Boolean> observeConnectionChanges() {
        return connectionStateSubject;
    }

    public boolean isConnected() {
        return isConnected;
    }

    private boolean checkActiveConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();// возвращает null при отсутствии соединения
        return netInfo != null && netInfo.isConnected();
    }
}
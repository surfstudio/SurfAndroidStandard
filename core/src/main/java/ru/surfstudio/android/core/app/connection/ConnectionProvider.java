package ru.surfstudio.android.core.app.connection;

import android.content.Context;
import android.content.IntentFilter;

import rx.Observable;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Provider, позволяющий подписаться на событие изменения состояния соединения
 */
public class ConnectionProvider {

    private ConnectionReceiver receiver = new ConnectionReceiver();

    public ConnectionProvider(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, intentFilter);
    }

    public Observable<Boolean> observeConnectionChanges() {
        return receiver.observeConnectionChanges();
    }

    public boolean isConnected() {
        return receiver.isConnected();
    }

    public boolean isDisconnected() {
        return !receiver.isConnected();
    }
}


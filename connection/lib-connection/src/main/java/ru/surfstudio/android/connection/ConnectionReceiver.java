/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


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
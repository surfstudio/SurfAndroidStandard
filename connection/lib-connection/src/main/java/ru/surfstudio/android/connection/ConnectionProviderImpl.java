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

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import io.reactivex.Observable;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class ConnectionProviderImpl implements ConnectionProvider {
    private static final long LAST_CONNECTION_QUALITY_RESULT_CACHE_TIME = 60L * 1000; //1 мин

    private ConnectionReceiver receiver;
    private Context context;
    private boolean lastConnectionResultFast = false;
    private long lastConnectionResultTime = 0;

    public ConnectionProviderImpl(Context context) {
        this.context = context;
        this.receiver = new ConnectionReceiver(context);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, intentFilter);
    }

    @Override
    public Observable<Boolean> observeConnectionChanges() {
        return receiver.observeConnectionChanges();
    }

    @Override
    public boolean isConnected() {
        return receiver.isConnected();
    }

    @Override
    public boolean isDisconnected() {
        return !receiver.isConnected();
    }

    @Override
    public boolean isConnectedFast() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastConnectionResultTime > LAST_CONNECTION_QUALITY_RESULT_CACHE_TIME) {
            lastConnectionResultTime = currentTime;
            lastConnectionResultFast = isConnectedFastInternal(context);
        }
        return lastConnectionResultFast;
    }

    @Override
    public boolean isConnectedToWifi() {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    @Override
    public NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is fast connectivity
     *
     * @param context
     * @return
     */
    private boolean isConnectedFastInternal(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFastInternal(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     *
     * @param type
     * @param subType
     * @return
     */
    @SuppressWarnings({"squid:S1871", "squid:MethodCyclomaticComplexity"})
    private boolean isConnectionFastInternal(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_EVDO_0: // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A: // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA: // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA: // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA: // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS: // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // ~ 1-2 Mbps // API level 11
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9 // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13 // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11 // ~ 10+ Mbps
                    return true;
                case TelephonyManager.NETWORK_TYPE_1xRTT: // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA: // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE: // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS: // ~ 100 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8 // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_UNKNOWN: // Unknown
                default:
                    return false;
            }
        } else {
            return false;
        }
    }
}


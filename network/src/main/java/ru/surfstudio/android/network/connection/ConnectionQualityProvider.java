package ru.surfstudio.android.network.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import javax.inject.Inject;

import ru.surfstudio.android.dagger.scope.PerApplication;

/**
 * предоставляет состояние интернет соединения
 */
@PerApplication
public class ConnectionQualityProvider {
    private static final long LAST_CONNECTION_RESULT_CACHE_TIME = 60L * 1000; //1 мин
    private Context context;
    private boolean lastConnectionResultFast = false;
    private long lastConnectionResultTime = 0;

    @Inject
    public ConnectionQualityProvider(Context context) {
        this.context = context;
    }

    public boolean isConnectedFast() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastConnectionResultTime > LAST_CONNECTION_RESULT_CACHE_TIME) {
            lastConnectionResultTime = currentTime;
            lastConnectionResultFast = isConnectedFastInternal(context);
        }
        return lastConnectionResultFast;
    }

    /**
     * Get the network info
     *
     * @param context
     * @return
     */
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

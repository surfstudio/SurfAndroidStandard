package com.surf.myapplication.interactor.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.WorkerThread;
import android.support.v4.content.ContextCompat;

import com.agna.ferro.mvp.component.scope.PerApplication;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import ru.labirint.android.app.log.Logger;
import ru.labirint.android.interactor.location.error.LocationSecurityException;
import ru.labirint.android.interactor.location.error.LocationServiceUnavailableException;

/**
 * обертка над библиотекой, предоставляющей доступ к локации пользователя
 * позволяет производить только 1 текущий запрос
 */
@PerApplication
class LocationProviderImpl implements LocationProvider {
    public static final int TIMEOUT_FOR_CACHED_LOCATION =30 * 60 * 1000; //ms
    public static final String NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER;
    public static final String GPS_PROVIDER = LocationManager.GPS_PROVIDER;

    private boolean initialized = false;

    private GoogleApiClient googleApiClient;
    private final Context appContext;
    private LocationManager locationManager;
    private GoogleLocationListener googleLocationListener;

    @Inject
    public LocationProviderImpl(Context appContext) {
        this.appContext = appContext;
    }

    /**
     * следует вызвать при завершении запроса локации или для завершения запроса локации
     * освобождает ресурсы, связанные с запросом локации
     */
    @Override
    public void reset() {
        try {
            if (googleApiClient!= null && googleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, googleLocationListener);
            }
        } catch (Exception e) {
            //Перехватываем все исключения, тк в доке не описаны исключения
            Logger.w(e);
        }
        googleApiClient.disconnect();
    }

    /**
     * запускает получение локации
     */
    @Override
    @WorkerThread
    public void getLocation(LocationListener locationListener) {
        prepare();

        this.googleLocationListener = new GoogleLocationListener(locationListener);

        if (!checkLocationPermissions()) {
            locationListener.onError(new LocationSecurityException());
            return;
        }
        LocationData lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            Logger.d("Return cached location: " + lastKnownLocation.toString());
            locationListener.onSuccess(lastKnownLocation);
            return;
        }
        if (!isAvailable()) {
            Logger.d("location service unavailable");
            locationListener.onError(new LocationServiceUnavailableException());
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                createLocationRequest(),
                this.googleLocationListener,
                Looper.getMainLooper());
    }

    private void prepare() {
        if (!initialized) {
            locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
            googleApiClient = new GoogleApiClient.Builder(appContext)
                    .addApi(LocationServices.API)
                    .build();
            initialized = true;
        }
        googleApiClient.blockingConnect();
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        return locationRequest;
    }

    private LocationData getLastKnownLocation() {
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastKnownLocation == null) {
            return null;
        }
        LocationData locationData = new LocationData(
                lastKnownLocation.getProvider(),
                lastKnownLocation.getLongitude(),
                lastKnownLocation.getLatitude(),
                lastKnownLocation.getTime());
        Calendar calendar = GregorianCalendar.getInstance();
        Long lastKnownLocationTime = lastKnownLocation.getTime();
        long currentTime = calendar.getTimeInMillis();
        return currentTime - lastKnownLocationTime < TIMEOUT_FOR_CACHED_LOCATION
                ? locationData
                : null;

    }

    private boolean isAvailable() {
        return isGpsProviderEnabled() || isNetworkProviderEnabled();
    }

    private boolean isGpsProviderEnabled() {
        return locationManager.isProviderEnabled(GPS_PROVIDER);
    }

    private boolean isNetworkProviderEnabled() {
        return locationManager.isProviderEnabled(NETWORK_PROVIDER);
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(appContext, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkLocationPermissions() {
        return checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private class GoogleLocationListener implements com.google.android.gms.location.LocationListener {

        private LocationListener locationListener;

        public GoogleLocationListener(LocationListener locationListener) {
            this.locationListener = locationListener;
        }

        @Override
        public void onLocationChanged(Location location) {
            locationListener.onSuccess(new LocationData(
                    location.getProvider(),
                    location.getLongitude(),
                    location.getLatitude(),
                    location.getTime()));
        }
    }
}

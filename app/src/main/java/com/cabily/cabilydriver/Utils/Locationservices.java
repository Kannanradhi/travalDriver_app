package com.cabily.cabilydriver.Utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yayandroid.locationmanager.base.LocationBaseService;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.constants.FailType;
import com.yayandroid.locationmanager.constants.ProcessType;

public class Locationservices extends LocationBaseService {

    public static final String ACTION_LOCATION_CHANGED = "com.yayandroid.locationmanager.sample.service.LOCATION_CHANGED";
    public static final String ACTION_LOCATION_FAILED = "com.yayandroid.locationmanager.sample.service.LOCATION_FAILED";
    public static final String ACTION_PROCESS_CHANGED = "com.yayandroid.locationmanager.sample.service.PROCESS_CHANGED";

    public static final String EXTRA_LOCATION = "ExtraLocationField";
    public static final String EXTRA_FAIL_TYPE = "ExtraFailTypeField";
    public static final String EXTRA_PROCESS_TYPE = "ExtraProcessTypeField";

    private boolean isLocationRequested = false;
    private SessionManager sessionManager;
    public static Context mcontext;
    public Locationservices() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public LocationConfiguration getLocationConfiguration() {
        return Configurations.silentConfiguration(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // calling super is required when extending from LocationBaseService
        super.onStartCommand(intent, flags, startId);

        sessionManager = new SessionManager(Locationservices.this);

        if(!isLocationRequested) {
            isLocationRequested = true;
            getLocation();
        }

        // Return type is depends on your requirements
        return START_NOT_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
//        Intent intent = new Intent(ACTION_LOCATION_CHANGED);
//        intent.putExtra(EXTRA_LOCATION, location);
//        sendBroadcast(intent);
        sessionManager.setLatitude(String.valueOf(location.getLatitude()));
        sessionManager.setLongitude(String.valueOf(location.getLongitude()));
//        stopSelf();
    }

    @Override
    public void onLocationFailed(@FailType int type) {
//        Intent intent = new Intent(ACTION_LOCATION_FAILED);
//        intent.putExtra(EXTRA_FAIL_TYPE, type);
//        sendBroadcast(intent);

//        stopSelf();
    }

    @Override
    public void onProcessTypeChanged(@ProcessType int processType) {
//        Intent intent = new Intent(ACTION_PROCESS_CHANGED);
//        intent.putExtra(EXTRA_PROCESS_TYPE, processType);
//        sendBroadcast(intent);
    }

    /**
     * Function to get latitude
     */


}

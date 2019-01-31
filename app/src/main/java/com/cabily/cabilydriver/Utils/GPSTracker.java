package com.cabily.cabilydriver.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;


import static android.content.Context.LOCATION_SERVICE;

public class GPSTracker {
    public Context mContext;
    // Flag for GPS status
    public boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    public boolean canGetLocation = false;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        mContext = context;
        mContext.startService(new Intent(mContext, Locationservices.class));
        getLocation();
    }

    public void getLocation() {
        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
        // Getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // Getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            // No network provider is enabled
        } else {
            this.canGetLocation = true;
            this.isGPSEnabled = true;

        }
    }


    public double getLatitude() {
        SessionManager sessionManager = new SessionManager(mContext);
        // return latitude
        return sessionManager.getLatitude();
    }

    /**
     * Function to get longitude
     */

    public Location getLocation1() {
        Location loc = null;
        SessionManager sessionManager = new SessionManager(mContext);

        loc = new Location(LocationManager.GPS_PROVIDER);
        loc.setLatitude(sessionManager.getLatitude());
        loc.setLongitude(sessionManager.getLongitude());

        return loc;
    }


    public double getLongitude() {
        // return longitude
        SessionManager sessionManager = new SessionManager(mContext);
        return sessionManager.getLongitude();
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
//		alertDialog.setCancelable(false);
        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to goto settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
/*						Intent intent = new Intent(mContext, Fragment_Home.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("EXIT", true);
						mContext.startActivity(intent);*/
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public boolean isgpsenabled() {
        return this.isGPSEnabled;
    }
}

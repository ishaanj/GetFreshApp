package getfresh.com.getfreshapplication.data;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Somshubra on 6/19/2015.
 * @author Somshubra
 */
public class UserLocationManager implements LocationListener{

    private Context context;
    private LocationManager manager;

    private Location lastKnownLocation;
    private List<Address> addressList;

    private ProgressDialog pd;

    public UserLocationManager(Context context) {
        this.context = context;
        this.manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if(context instanceof UserLocationManagerListener)
            listener = (UserLocationManagerListener) context;
    }

    public boolean checkIfGPSIsEnabled() {
        return (manager.isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    /**
     * Note : The AlertDialog that it returns should be stored in a global variable.
     *        In onDestroy(), call it like this :
     *
     *        if(alert != null) { alert.dismiss();
     *
     *        to prevent a memory leak.
     * @return
     */
    public AlertDialog alertUserToEnableGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    public void getLastKnownLocation() {
        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if(locationPermission != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if(manager != null && checkIfGPSIsEnabled()) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            if(listener != null)
                listener.searchingForLocation();
        }
        else {
            if(listener != null)
                listener.gpsDisabledWhenRequestingLastKnownLocation();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(listener != null)
            listener.searchForLocationEnded();

        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if(locationPermission != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        manager.removeUpdates(this);
        this.lastKnownLocation = location;

        Geocoder geo = new Geocoder(context, Locale.getDefault());
        try {
            addressList = geo.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 1);

            if(listener != null)
                listener.addressObtained(addressList, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private UserLocationManagerListener listener;
    public interface UserLocationManagerListener {
        void gpsDisabledWhenRequestingLastKnownLocation();
        void addressObtained(List<Address> list, Location location);
        void searchingForLocation();
        void searchForLocationEnded();
    }

    public void setListener(UserLocationManagerListener listener) {
        this.listener = listener;
    }
}

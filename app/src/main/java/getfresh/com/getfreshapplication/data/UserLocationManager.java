package getfresh.com.getfreshapplication.data;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yue on 6/19/2015.
 */
public class UserLocationManager {

    private Context context;
    private LocationManager manager;

    private Location lastKnownLocation;
    private List<Address> addressList;

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

    public Location getLastKnownLocation() {
        if(manager != null && checkIfGPSIsEnabled()) {
            lastKnownLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return lastKnownLocation;
        }
        else {
            if(listener != null)
                listener.gpsDisabledWhenRequestingLastKnownLocation();
            return lastKnownLocation;
        }
    }

    public List<Address> getAddressFromLocation() {
        getLastKnownLocation();

        if(lastKnownLocation != null) {
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            try {
                addressList = geo.getFromLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 5);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addressList;
        }
        else {
            if(listener != null)
                listener.noLastKnownLocationFound();
            return null;
        }
    }

    private UserLocationManagerListener listener;
    public interface UserLocationManagerListener {
        void gpsDisabledWhenRequestingLastKnownLocation();
        void noLastKnownLocationFound();
    }

    public void setListener(UserLocationManagerListener listener) {
        this.listener = listener;
    }
}

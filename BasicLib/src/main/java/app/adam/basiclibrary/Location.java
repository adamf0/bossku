package app.adam.basiclibrary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

class Location{
    private FusedLocationProviderClient mFusedLocationClient;
    protected Activity act;

    public Location(Activity act) {
        this.act = act;
    }

    public void getLocationNow(final LocationListener locationListener) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(act);
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<android.location.Location>() {
                    @Override
                    public void onComplete(@NonNull Task<android.location.Location> task) {
                        final android.location.Location location = task.getResult();
                        if (location == null) {
                            LocationRequest mLocationRequest = new LocationRequest();
                            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            mLocationRequest.setInterval(0);
                            mLocationRequest.setFastestInterval(0);
                            mLocationRequest.setNumUpdates(3);

                            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(act);
                            if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                                        public void onLocationResult(LocationResult locationResult) {
                                            final android.location.Location mLastLocation = locationResult.getLastLocation();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    locationListener.onLocation(String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()));
                                                }
                                            }, 1500);
                                        }
                                    }, Looper.myLooper()
                            );
                        }
                        else {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    locationListener.onLocation(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
                                }
                            }, 1500);
                        }
                    }
                }
        );
    }
}

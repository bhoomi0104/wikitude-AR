package com.example.bhoomi.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wikitude.architect.ArchitectStartupConfiguration;
import com.wikitude.architect.ArchitectView;


public class MainActivity extends AppCompatActivity implements LocationListener,ActivityCompat.OnRequestPermissionsResultCallback {

    ArchitectView architectView;
    private static final int REQUEST_CAMERA=1;
    private static final int REQUEST_GPS=1;
    private static final int REQUEST_NETWORK=1;

    private  LocationManager locationManager;
    private LocationListener locationListener;
    private static final int LOCATION_UPDATE_MIN_TIME_GPS = 1000;
    private static final int LOCATION_UPDATE_DISTANCE_GPS = 0;
    private static final int LOCATION_UPDATE_MIN_TIME_NW = 1000;
    private static final int LOCATION_UPDATE_DISTANCE_NW = 0;
    private static final int LOCATION_OUTDATED_WHEN_OLDER_MS = 1000 * 60 * 10;
    /*private double latitude;
    private double longitude;
    private double altitude = 0;
    private float accuracy;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);

        }

        this.architectView = (ArchitectView)this.findViewById( R.id.architectView );
        final ArchitectStartupConfiguration config = new ArchitectStartupConfiguration();
        config.setFeatures(ArchitectStartupConfiguration.Features.Geo);
        config.setLicenseKey("ryd1axl2RAvy6Udct6UTj1OrdC1/xGxkG74zb0Hx2oU428ZJULXCG9L5AyEbV3vHoBJsoC8yt2jzpQjbnpf8XNfkrO0YjbFiTzYmdhKch2X642FwezehYzYaMRt+tT4N0xGQtcx9mdUGv3ChkeBWYkX5VvSgyddw/ZxKBjkgwdxTYWx0ZWRfX6RIFdjD5aMfXL+3zxH2U2KuGEoP4dugBpypvVulGQ9Gs8FmS5rpGNaN2BhXXqZe+HcIHfMUVgo2Ti6fUkIABpfRlAmoE2Y6BUcuun6PXaHKuCd434HglS2/9gpfh8HzdNuuHXACKOYQg638EZy5El+c7t1M01/pmro89oqbO62Zz1pDCPpKzxIAcE/jvucwlR6+kXbckl3lWZ3TNaTdn/zju9Ou2GST1Kkc9VtfaK4ZYNgDhExFU7huZxx+xxBZEqmgpF2D/OgJyZoO5y9Ie8R7dnJY3p8CtBv1CCGFJOkvtgSXQKMO/40ZY1cN1cRY0Y1xzcr0+cShBpAhOZC13UjMnhsDkbNVUoqTpVgR1q44IkrtnFUB1Od67MYKgIPUpVDzjuX7V6V1Ch3PEUpYSoE2GIZyLVmPTfpQuoHICZ/NhaKYbwp1NnPhSs1liGepVrcE+u8N0b5Aou2ybf0CRLRFNI8ZaezOc0FaXS0tGQhn4GAekRKlWaM=" );
        this.architectView.onCreate( config );
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        architectView.onPostCreate();

        try
        {
            Log.i("ONPOST","onpost is executing try");
            this.architectView.load("file:///android_asset/demo1/index.html");

            //architectView.setLocation(latitude, longitude, altitude, accuracy);
        }
        catch (Exception e)
        {
            System.out.println("error1 on post create");
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_GPS);

            }

            final Location lastKnownGPSLocation = this.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownGPSLocation != null && lastKnownGPSLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS)
            {
                this.onLocationChanged(lastKnownGPSLocation);
            }

            if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null)
            {
                this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
            }
        }


        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        REQUEST_NETWORK);

            }
            final Location lastKnownNWLocation = this.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (lastKnownNWLocation != null && lastKnownNWLocation.getTime() > System.currentTimeMillis() - LOCATION_OUTDATED_WHEN_OLDER_MS) {
                locationListener.onLocationChanged(lastKnownNWLocation);
            }
            if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
                this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
            }

        }
        architectView.onResume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        architectView.onDestroy();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_GPS);

        }

        if (this.locationListener != null && this.locationManager != null && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                this.locationManager.removeUpdates(this);
            }

        architectView.onPause();
    }


    @Override
    public void onLocationChanged(Location location)
    {
        if(location!=null && MainActivity.this.architectView != null )
        {
            if ( location.hasAltitude() && location.hasAccuracy() && location.getAccuracy()<7)
            {
                MainActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getAccuracy() );
            }
            else
            {
                MainActivity.this.architectView.setLocation( location.getLatitude(), location.getLongitude(), location.hasAccuracy() ? location.getAccuracy() : 1000 );
            }
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

}


package net.john.mapsandbox;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by John on 7/17/2016.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationManager mLocationManager;
    private String mLatitudeText, mLongitudeText;
    private Marker mLastLocMarker;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_main);

        //get the system location manager
        mLocationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Log.d("mMap", "network provider is not enabled");
        }
        //LinearLayout mainLinear = (LinearLayout) findViewById(R.id.main_linear);
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //Fragment mapFrag = getSupportFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment mapFrag = MapFragment.newInstance();
        Log.d("layout", mapFrag.toString());
        ft.add(R.id.activity_main, mapFrag);
        ft.commit();

        /*
        //connect to the db
        DbController db = DbController.getInstance(getApplicationContext());
        */

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Log.d("google", "built GoogleApiClient= " + mGoogleApiClient.toString());
        }

        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1000);

        mapFrag.getMapAsync(this);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d("google", "google api onConnected called");
        if (!checkPlayServices()){
            Log.d("mMap", "play services not connected");
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        try {
            /*
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                return;
            }
            */
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("mMap", "location changed");
                    mLastLocation = location;
                    updateLocationUI();
                }
            });
        } catch (SecurityException ex) {
            Log.d("mMap", ex.toString());
        }

        refreshLoc();
    }

    private void refreshLoc(){
        if (mGoogleApiClient == null) {
            Log.d("mMap", "google api client is null");
        }
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.d("mMap", "tried to access LastLocation");
        } catch (SecurityException ex) {
            Log.d("mMap", ex.toString());
        }

        if (mLastLocation != null) {
            Log.d("mMap", "onConnected received: " + mLastLocation.toString());
        } else {
            Log.d("mMap", "mLastLocation is null");
        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        Log.d("mMap", "checking play services");
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        1000).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult res) {
        Log.d("google", "onConnectionFailed code: " + res.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int code) {
        Log.d("google", "connectionSuspended code= " + code);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mMap", "map ready");

        //make the map a satellite type
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //set marker onClick callbacks
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("mMap", "clicked marker " + marker.getPosition().toString());

                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                SubmissionDialogFragment subFrag = SubmissionDialogFragment.newInstance(marker.getPosition());
                subFrag.show(ft, "dialog");
                return false;
            }
        });

        //set a click listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mLastLocation == null) {
                    refreshLoc();
                }
                //recenter the map if user location is available
                if (mLastLocation == null) {
                    Log.d("mMap", "recenterMap can't run when mLastLocation is null");
                    return;
                }
                if (mMap == null) {
                    Log.d("mMap", "recenterMap can't run when mMap is null");
                    return;
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                mMap.addMarker(markerOptions);
            }
        });
    }

    //update the current location icon and recenter the map
    private void updateLocationUI() {
        //remove the last location marker if one exists
        if (mLastLocMarker != null) {
            mLastLocMarker.remove();
        }
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.walking));
        options.position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
        mLastLocMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 18f));
        Log.d("mMap", "updatedLocationUI");
    }
}

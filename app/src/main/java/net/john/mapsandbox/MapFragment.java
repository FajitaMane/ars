package net.john.mapsandbox;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends SupportMapFragment  {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance) {
        Log.d("mMap", "onCreateView called");
        View rootView = inflater.inflate(R.layout.fragment_map, parent, true);
        return rootView;
    }


}

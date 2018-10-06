package com.btl.hcj.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btl.hcj.myapplication.TestActivity.SceneTransitionsActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends FragmentActivity implements LocationListener {

    public static final int MY_LOCATION_REQUEST_CODE = 1111;
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Button mPress;
    private Location mLocation;
    private View mContextView;
    private LatLng mOrigin;
    private LatLng mDest;
    private MyDirectionsData mMyDirectionsData;

    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                autocompleteFragment.setText(place.getName());
                mDest = place.getLatLng();
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Place Id: " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // To hide Navigator bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                @SuppressLint("MissingPermission") Location a = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                LatLng l = new LatLng(a.getLatitude(), a.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
            }
        });
        mPress = (Button) findViewById(R.id.press);
        getMyLocation();
        mPress.setOnClickListener((v) -> {
            mapFragment.getMapAsync(route);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void route(GoogleMap googleMap) {
        mMyDirectionsData = new MyDirectionsData(getApplicationContext());

        Object[] datas = new Object[3];

        datas[0] = mOrigin;
        datas[1] = mDest;
        datas[2] = googleMap;

        mMyDirectionsData.execute(datas);
    }

    // 과기대 37.631633, 127.077566
    // 연촌초 37.634691, 127.072858
    // 하계중 37.641074, 127.071517
    OnMapReadyCallback route = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
//            LatLng disasterPosition = new LatLng(37.635493, 127.081343);

            googleMap.addMarker(new MarkerOptions().position(mOrigin));
            googleMap.addMarker(new MarkerOptions().position(mDest));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(mOrigin);
            builder.include(mDest);
            CameraUpdate camera = CameraUpdateFactory.newLatLngBounds(builder.build(), 150);
            googleMap.animateCamera(camera);

            route(googleMap);
        }
    };


    private void requestPermission(String... permission) {
        PermissionRequester.Builder
                requester = new PermissionRequester.Builder(this);

        requester.create().requests(permission, MY_LOCATION_REQUEST_CODE,
                (activity) -> {
            Toast.makeText(activity, "위치 권한이 필요해요...", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_REQUEST_CODE:
                for (int req : grantResults) {
                    if (req == -1) {
                        // TODO 권한이 거절 됬을 때
                        finish();
                    }
                }
//                getMyLocation();
                break;
        }
    }

    private void showLocation(Location location) {
        this.mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(l));
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation(){
        Location a = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        Log.i(TAG, "last known location: " + a);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mOrigin == null)
            Snackbar.make(getCurrentFocus(), "Location Detected!", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        mOrigin = new LatLng(location.getLatitude(), location.getLongitude());
        Log.i(TAG, "changed " + location.getAccuracy() + " " + location.getProvider());
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, "status changed: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "Disable " + provider);
    }

    public boolean blinkMarker(Marker marker) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            float[] blink = {12/24f, 13/24f, 14/24f, 15/24f, 16/24f, 17/24f, 18/24f, 19/24f, 20/24f, 21/24f, 22/24f, 23/24f, 24/24f};
            int i = 0;
            int timer;
            @Override
            public void run() {
                marker.setAlpha(blink[i]);
                i++;
                if (i == blink.length) i = 0;
                timer = timer + 100;
//                if (timer < 30000)
                handler.postDelayed(this, 1000/24);
//                else
//                    marker.remove();
            }
        });
        return true;
    }

    private LatLng stringToLatLng(String str) {
        String[] strs = str.split(", ");
        return new LatLng(Double.valueOf(strs[0]), Double.valueOf(strs[1]));
    }
}

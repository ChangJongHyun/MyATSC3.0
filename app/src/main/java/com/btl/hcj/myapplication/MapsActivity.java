package com.btl.hcj.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements LocationListener {

    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<MyItem> mItemArray;
    private LocationManager mLocationManager;
    private Button mExtract;
    private Context mContext;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        mContext = getApplicationContext();
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mItemArray = new ArrayList<>();

        // To hide Navigator bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
        mapFragment.getMapAsync(start);

        mExtract = findViewById(R.id.extract);
        mExtract.setOnClickListener((v) -> {
            mapFragment.getMapAsync(mapping);
        });
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
    OnMapReadyCallback mapping = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.clear();

            mClusterManager = new ClusterManager<>(mContext, mMap);

            mMap.setOnCameraIdleListener(mClusterManager);
            mMap.setOnMarkerClickListener(mClusterManager);

            getMyLocation();
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("사용자"));

            // Add a marker in Sydney and move the camera
            LatLng university1 = new LatLng(37.631633, 127.077566);
            LatLng elementary1 = new LatLng(37.634691, 127.072858);
            LatLng middle1 = new LatLng(37.641074, 127.071517);

            mItemArray.add(new MyItem(university1));
            mItemArray.add(new MyItem(elementary1));
            mItemArray.add(new MyItem(middle1));

            mClusterManager.addItems(mItemArray);

            mMap.addMarker(new MarkerOptions().position(university1).title("과기대"));
            mMap.addMarker(new MarkerOptions().position(elementary1).title("연촌초"));
            mMap.addMarker(new MarkerOptions().position(middle1).title("하계중"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(elementary1, 16));
        }
    };

    // 과기대 37.631633, 127.077566
    // 연촌초 37.634691, 127.072858
    // 하계중 37.641074, 127.071517
    OnMapReadyCallback start = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            LatLng sydney = new LatLng(-33.91, 151.18);
            mMap.addMarker(new MarkerOptions()
                    .position(sydney)
                    .title("Hello world"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

    private void requestPermission(String permission) {
        PermissionRequester.Builder
                requester = new PermissionRequester.Builder(this);

        requester.create().requests(permission, 10000,
                (activity) -> {
            Toast.makeText(activity, "위치 권한이 필요해요...", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        mLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

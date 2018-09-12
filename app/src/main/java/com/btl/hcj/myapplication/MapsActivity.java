package com.btl.hcj.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.btl.hcj.myapplication.data.DbContract;
import com.btl.hcj.myapplication.data.DbHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements LocationListener {

    public static final int MY_LOCATION_REQUEST_CODE = 1111;
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<MyItem> mItemArray;
    private LocationManager mLocationManager;
    private Button mPress;
    private EditText mOrigin;
    private EditText mDest;
    private Context mContext;
    private Location mLocation;
    private MyDirectionsData mMyDirectionsData;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mContext = getApplicationContext();
        dbHelper = new DbHelper(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mItemArray = new ArrayList<>();

        // To hide Navigator bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);
//        mapFragment.getMapAsync(start);
        mPress = (Button) findViewById(R.id.press);
        mOrigin = (EditText) findViewById(R.id.origin);
        mDest = (EditText) findViewById(R.id.dest);
        mPress.setOnClickListener((v) -> {
            route();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private LatLng convertStringToLatLng(String s) {
        s = s.replaceAll(" ", "");
        String[] strings = s.split(",");
        return new LatLng(Double.parseDouble(strings[0]), Double.parseDouble(strings[1]));
    }

    private void geocoding(int coder) {
        LatLng target = convertStringToLatLng(mDest.getText().toString());

        Object[] datas = new Object[2];
        datas[1] = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());  // 현재 위치
        datas[2] = target;  // target 위치
        datas[3] = mMap;    // 구글 맵

        MyGeocoder myGeocoder = new MyGeocoder(getApplicationContext());
        myGeocoder.execute(datas);
    }

    private void route() {
        LatLng origin = convertStringToLatLng(mOrigin.getText().toString());
        LatLng dest = convertStringToLatLng(mDest.getText().toString());
        Log.i(TAG, dest.toString());
        StringBuilder sb = new StringBuilder();
        Object[] datas = new Object[4];

        mMyDirectionsData = new MyDirectionsData(getApplicationContext());
        datas[0] = origin;
        datas[1] = dest;
        datas[2] = mMap;

        mMyDirectionsData.execute(datas);
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
                getMyLocation();
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        boolean isGPSEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNETEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i(TAG, "GPS: " + isGPSEnable);
        Log.i(TAG, "NET: " + isNETEnable);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        String s = location.getLatitude() + ", " + location.getLongitude();
        mOrigin.setText(s);
        Log.i(TAG, "changed ");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(TAG, status+"");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(TAG, "enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(TAG, "Disable " + provider);
    }
}

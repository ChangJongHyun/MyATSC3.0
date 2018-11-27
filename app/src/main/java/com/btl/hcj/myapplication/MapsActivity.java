package com.btl.hcj.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.btl.hcj.myapplication.BottomSheet.RouteAdapter;
import com.btl.hcj.myapplication.BottomSheet.RouteHolder;
import com.btl.hcj.myapplication.BottomSheet.RouteVO;
import com.btl.hcj.myapplication.data.Direction.Leg;
import com.btl.hcj.myapplication.data.Direction.Route;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements LocationListener, BackGroundMapAPI.ItemListener, RouteAdapter.ItemListener {

    public static final int MY_LOCATION_REQUEST_CODE = 1111;
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Button mPress;
    private View mContextView;
    private LatLng mOrigin;
    private LatLng mDest;
    private LatLng mDisaster;
    private Marker mOriginMarker;
    private Marker mDestMarker;
    private LatLng mDisasterMarker;
    private BackGroundMapAPI mBackGroundMapAPI;

    private CoordinatorLayout mCoordinatorLayout;
    private BottomSheetBehavior mBehavior;
    private RecyclerView mRecyclerView;
    private RouteAdapter mRouteAdapter;
    private TextView mBottomText;

    private Polyline p;

    public static ArrayList<Route> items;

    public static final int REQUEST_DIRECTION = 1;
    public static final int REQUEST_PLACE = 2;
    public static final int REQUEST_NEARBY = 3;

    private static BackGroundMapAPI.ItemListener mItemCallback;

    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Autocomplete 실험용

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


        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mRecyclerView = findViewById(R.id.list_item);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setItemCallback(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mapFragment.getMapAsync(route);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        builder.setMessage("재난이 발생 했습니다. 재난 대피소 정보를 받아오시겠습니까? ");
        builder.setTitle("재난 발생 알림");

        AlertDialog dialog = builder.create();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gmap);

        mPress = findViewById(R.id.press);
        mPress.setOnClickListener((v) -> {
            dialog.show();
        });
    }

    OnMapReadyCallback route = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

//            mDisaster = new LatLng(37.639630, 127.080094); // 공릉 부근
            mDisaster = new LatLng(36.381342, 127.373179); // ETRI 부근

            mMap.addMarker(new MarkerOptions().position(mDisaster).title("재난 발생"));
            mMap.addCircle(new CircleOptions().center(mDisaster).radius(450).strokeColor(Color.RED));
            if (mOrigin == null) {
                Snackbar.make(getCurrentFocus(), "Location isn't detected, try again", Snackbar.LENGTH_LONG);
            } else {
                mBackGroundMapAPI = new BackGroundMapAPI(getApplicationContext());

                Object[] datas = new Object[2];

                datas[0] = mOrigin;
                datas[1] = REQUEST_DIRECTION;

                mBackGroundMapAPI.execute(datas);
            }
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
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        @SuppressLint("MissingPermission") Location a = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//                        LatLng l = new LatLng(a.getLatitude(), a.getLongitude()); // 공릉 부근
                        LatLng l = new LatLng(36.381394, 127.371068); // ETRI부근

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
                    }
                });
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation() {
        Location a = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        Log.i(TAG, "last known location: " + a);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mOrigin == null)
            Snackbar.make(getCurrentFocus(), "Location Detected!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//        mOrigin = new LatLng(location.getLatitude(), location.getLongitude()); // 원래코드
        mOrigin = new LatLng(36.383672, 127.366935); // 대전 실험 전용
        this.mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                if (mOriginMarker != null)
                    mOriginMarker.remove();
                mOriginMarker = mMap.addMarker(new MarkerOptions().position(mOrigin).title("Current Position"));
            }
        });
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

    public static void receiveItem(List<RouteVO> item) {
        mItemCallback.getItem(item);
    }

    public static void setItemCallback(BackGroundMapAPI.ItemListener mItemCallback) {
        MapsActivity.mItemCallback = mItemCallback;
    }

    @Override
    public void getItem(List<RouteVO> item) {

        if (mDestMarker != null)
            mDestMarker.remove();
        if (p != null)
            p.remove();

        if (item != null) {
            p = mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(item.get(0).getOverviewPolyline())));

            mDest = item.get(0).getDestination();
            mDestMarker = mMap.addMarker(new MarkerOptions().position(mDest));

            mRouteAdapter = new RouteAdapter(item, this);
            mRecyclerView.setAdapter(mRouteAdapter);
            mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    @Override
    public void onItemClick(RouteVO route, RouteHolder holder) {

        if (p != null)
            p.remove();

        if (!mDest.equals(route.getDestination())) {
            mDest = route.getDestination();
            mDestMarker.remove();
            mDestMarker = mMap.addMarker(new MarkerOptions().position(mDest).title(route.getStringDestiation()));
        }
        for(RouteHolder routeHolder : RouteAdapter.mRouteHolder) {
            if(routeHolder.equals(holder))
                holder.infoView.setTypeface(null, Typeface.BOLD);
//                holder.infoView.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
            else
                routeHolder.infoView.setTypeface(null, Typeface.NORMAL);
        }

        p = mMap.addPolyline(new PolylineOptions().color(Color.BLACK).clickable(true)
                .addAll(PolyUtil.decode(route.getOverviewPolyline())));

        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}

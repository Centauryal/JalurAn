package com.centaury.jalurangkot;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.centaury.jalurangkot.adapter.PagerAdapter;
import com.centaury.jalurangkot.adapter.PlaceAutocompleteAdapter;
import com.centaury.jalurangkot.app.AppConfig;
import com.centaury.jalurangkot.app.AppController;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import biz.laenger.android.vpbs.BottomSheetUtils;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener {

    // LogCat tag
    private static final String TAG = MapsActivity.class.getSimpleName();

    // Code used in requesting runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // Represents a geographical location.
    private Location mLastLocation;

    // Keys for storing activity state in the Bundle.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final String LOG_TAG = "SearchPlace";

    // Location updates intervals in sec
    private static final long UPDATE_INTERVAL = 10000; // 10 sec
    private static final long FASTEST_INTERVAL = UPDATE_INTERVAL / 2;

    private GoogleMap mMap;
    private AutoCompleteTextView destination;
    private PlaceAutocompleteAdapter mAdapter;

    private CameraPosition mCameraPosition;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private LatLng latLng, latLngAwal, latLngAkhir, newLatLngTemp;

    private Marker mCurrLocationMarker, marker;
    private float v;
    private int index, next;
    private Handler handler;
    private String mLatitude, mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        destination = (AutoCompleteTextView) findViewById(R.id.place_autocomplete_fragment);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        if (savedInstanceState != null) {
            mLastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setupBottomSheet();

        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, null, null);

        destination.setAdapter(mAdapter);

        /*
        * Sets the start and destination points based on the values selected
        * from the autocomplete text views.
        * */
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (!places.getStatus().isSuccess()) {
                            // Request did not complete successfully
                            Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                            places.release();
                            return;
                        }
                        // Get the Place object from the buffer.
                        final Place place = places.get(0);
                        hideKeyboard();
                        mLatitude=String.valueOf(place.getLatLng().latitude);
                        mLongitude=String.valueOf(place.getLatLng().longitude);
                        newLatLngTemp = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLngTemp, 15f));
                    }
                });

            }
        });

        /*
        These text watchers set the start and end points to null because once there's
        * a change after a value has been selected from the dropdown
        * then the value has to reselected from dropdown to get
        * the correct location.
        * */
        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (newLatLngTemp != null) {
                    newLatLngTemp = null;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setupBottomSheet() {
        final PagerAdapter sectionsPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this, PagerAdapter.TabItem.ARMADA_TERDEKAT, PagerAdapter.TabItem.JALUR_MIKROLET, PagerAdapter.TabItem.TEMPAT_PENTING);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        BottomSheetUtils.setupViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText(R.string.title_armada_terdekat);
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.marker_multiple, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText(R.string.title_jalur_mikrolet);
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.mikrolet_marker, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText(R.string.title_tempat_penting);
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.home_marker, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();
        myTimer.schedule(myTask,1000, 5000);

    }

    class MyTimerTask extends TimerTask {
        public void run() {
            getLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human_male_black_36dp));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        // ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        // public void onRequestPermissionsResult(int requestCode, String[] permissions,
        // int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        //this code stops location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        if (mMap != null) {
            savedInstanceState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            savedInstanceState.putParcelable(KEY_LOCATION, mLastLocation);
            super.onSaveInstanceState(savedInstanceState);
        }

    }

    /**
     * Function to store user in MySQL database will post params(id,
     * lat, lon) to set url
     * */
    private void getLocation() {

        // Tag used to cancel the request
        String tag_string_request = "req_location";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_GETLOCATION, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {
                Log.d(TAG, "Location Response: " + response.toString());

                if (marker != null){
                    marker.remove();
                }

                try {
                    final JSONArray jObj = new JSONArray(response);

                    // Check for error node in json
                    for (int i = 0; i < jObj.length(); i++) {

                        JSONObject jTreeObj = jObj.getJSONObject(i);
                        String nopol = jTreeObj.getString("nopol");
                        Double lat = jTreeObj.getDouble("lastlat");
                        Double lon = jTreeObj.getDouble("lastlon");
                        String polyline = jTreeObj.getString("point");

                        latLng = new LatLng(lat, lon);

                        List<LatLng> polyLineList;
                        polyLineList = PolyUtil.decode(polyline);

                        marker = mMap.addMarker(new MarkerOptions().position(latLng)
                                .flat(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.lyn_marker)));

                        AnimateMarker(marker, polyLineList);

                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_request);
    }

    private void AnimateMarker(final Marker markeranimasi, final List<LatLng> polyLineList) {
        handler = new Handler();
        index = -1;
        next = 1;
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (index < polyLineList.size() - 1) {
                    index++;
                    next = index + 1;
                }
                if (index < polyLineList.size() - 1) {
                    latLngAwal = polyLineList.get(index);
                    latLngAkhir = polyLineList.get(next);
                }
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(1000); //Duration
                valueAnimator.setInterpolator(new LinearInterpolator());

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        if (polyLineList.isEmpty() || polyLineList.size() == 1) {
                            v = valueAnimator.getAnimatedFraction();
                            double lon = v * latLng.longitude + (1 - v)
                                    * latLng.longitude;
                            double lat = v * latLng.latitude + (1 - v)
                                    * latLng.latitude;
                            LatLng newPos = new LatLng(lat, lon);
                            markeranimasi.setPosition(newPos);
                            markeranimasi.setAnchor(0.5f, 0.5f);
                            markeranimasi.setRotation(getBearing(latLng, newPos));
                        } else {
                            v = valueAnimator.getAnimatedFraction();
                            double lon = v * latLngAkhir.longitude + (1 - v)
                                    * latLngAwal.longitude;
                            double lat = v * latLngAkhir.latitude + (1 - v)
                                    * latLngAwal.latitude;
                            LatLng newPos = new LatLng(lat, lon);
                            markeranimasi.setPosition(newPos);
                            markeranimasi.setAnchor(0.5f, 0.5f);
                            markeranimasi.setRotation(getBearing(latLngAwal, newPos));
                        }
                    }
                });
                valueAnimator.start();
                handler.postDelayed(this, 1000);
            }
        });
    }

    public boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSIONS_REQUEST_CODE);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lon = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lon / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lon / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lon / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lon / lat))) + 270);
        return -1;
    }
}

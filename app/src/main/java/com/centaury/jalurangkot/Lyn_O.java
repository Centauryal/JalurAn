package com.centaury.jalurangkot;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.centaury.jalurangkot.app.AppConfig;
import com.centaury.jalurangkot.app.AppController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.maps.model.JointType.ROUND;

public class Lyn_O extends AppCompatActivity implements OnMapReadyCallback {

    // LogCat tag
    private static final String TAG = Lyn_O.class.getSimpleName();

    private GoogleMap mMap;

    private Marker marker;
    private List<LatLng> polyLineList;
    private PolylineOptions polylineOptions, greypolylineOptions;
    private Polyline polyline, greypolyline;

    private Handler handler;
    private LatLng startPosition, endPosition, latlng;
    private int index, next;
    private float v;
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyn_o);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        polyLineList = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ruteLynO();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void ruteLynO() {
        // Tag used to cancel the request
        String tag_string_request = "req_location_lyn_O";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_RUTELYN_O, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Location Response: " + response.toString());

                try {
                    JSONArray jObj = new JSONArray(response);

                    // Check for error node in json
                    for (int i = 0; i < jObj.length(); i++) {

                        JSONObject jTreeObj = jObj.getJSONObject(i);
                        String urutan = jTreeObj.getString("urutan");
                        Double lat = jTreeObj.getDouble("lat");
                        Double lon = jTreeObj.getDouble("lon");

                        latlng = new LatLng(lat, lon);
                        polyLineList = PolyUtil.decode(String.valueOf(latlng));
                        Log.d(TAG, polyLineList + "");

                        marker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_halte))
                                .title(urutan).position(latlng));


                        /*// Instantiating the class PolylineOptions to plot polyline in the map
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.YELLOW);
                        polylineOptions.width(10);
                        polyLineList.add(latlng);
                        polylineOptions.addAll(polyLineList);
                        mMap.addPolyline(polylineOptions);

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng latLng : polyLineList) {
                            builder.include(latLng);
                        }
                        LatLngBounds bounds = builder.build();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 11));*/

                    }

                    //Adjusting bounds
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latlng : polyLineList) {
                        builder.include(latlng);
                    }
                    LatLngBounds bounds = builder.build();
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 12);
                    mMap.animateCamera(mCameraUpdate);

                    polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.RED);
                    polylineOptions.width(10);
                    polylineOptions.startCap(new SquareCap());
                    polylineOptions.endCap(new SquareCap());
                    polylineOptions.jointType(ROUND);
                    polylineOptions.addAll(polyLineList);
                    polyline = mMap.addPolyline(polylineOptions);

                    greypolylineOptions = new PolylineOptions();
                    greypolylineOptions.color(Color.GRAY);
                    greypolylineOptions.width(10);
                    greypolylineOptions.startCap(new SquareCap());
                    greypolylineOptions.endCap(new SquareCap());
                    greypolylineOptions.jointType(ROUND);
                    polylineOptions.addAll(polyLineList);
                    greypolyline = mMap.addPolyline(greypolylineOptions);

                    mMap.addMarker(new MarkerOptions()
                            .position(polyLineList.get(polyLineList.size() - 1)));

                    ValueAnimator polylineAnimator = ValueAnimator.ofInt(0, 100);
                    polylineAnimator.setDuration(2000);
                    polylineAnimator.setInterpolator(new LinearInterpolator());
                    polylineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            List<LatLng> points = polyline.getPoints();
                            int percentValue = (int) valueAnimator.getAnimatedValue();
                            int size = points.size();
                            int newPoints = (int) (size * (percentValue / 100.0f));
                            List<LatLng> p = points.subList(0, newPoints);
                            greypolyline.setPoints(p);
                        }
                    });

                    polylineAnimator.start();
                    marker = mMap.addMarker(new MarkerOptions().position(latlng)
                            .flat(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.lyn_marker)));
                    handler = new Handler();
                    index = -1;
                    next = 1;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (index < polyLineList.size() - 1) {
                                index++;
                                next = index + 1;
                            }
                            if (index < polyLineList.size() - 1) {
                                startPosition = polyLineList.get(index);
                                endPosition = polyLineList.get(next);
                            }
                            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                            valueAnimator.setDuration(3000);
                            valueAnimator.setInterpolator(new LinearInterpolator());
                            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    v = valueAnimator.getAnimatedFraction();
                                    lng = v * endPosition.longitude + (1 - v)
                                            * startPosition.longitude;
                                    lat = v * endPosition.latitude + (1 - v)
                                            * startPosition.latitude;
                                    LatLng newPos = new LatLng(lat, lng);
                                    marker.setPosition(newPos);
                                    marker.setAnchor(0.5f, 0.5f);
                                    marker.setRotation(getBearing(startPosition, newPos));
                                    mMap.moveCamera(CameraUpdateFactory
                                            .newCameraPosition
                                                    (new CameraPosition.Builder()
                                                            .target(newPos)
                                                            .zoom(15.5f)
                                                            .build()));
                                }
                            });
                            valueAnimator.start();
                            handler.postDelayed(this, 3000);
                        }
                    }, 3000);



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

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}

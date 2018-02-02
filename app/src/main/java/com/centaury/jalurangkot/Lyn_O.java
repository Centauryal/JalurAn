package com.centaury.jalurangkot;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.centaury.jalurangkot.app.AppConfig;
import com.centaury.jalurangkot.app.AppController;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
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

    private List<LatLng> polyLineList;

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

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(googleMap.getCameraPosition().target)
                .zoom(17)
                .bearing(30)
                .tilt(45)
                .build()));
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
                        String nama = jTreeObj.getString("nama");
                        Double lat = jTreeObj.getDouble("lat");
                        Double lon = jTreeObj.getDouble("lon");

                        LatLng latlng = new LatLng(lat, lon);

                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_halte))
                                .title(urutan).snippet(nama).position(latlng));

                        // Instantiating the class PolylineOptions to plot polyline in the map
                        PolylineOptions polylineOptions = new PolylineOptions();
                        // Setting the color of the polyline
                        polylineOptions.color(Color.RED);
                        // Setting the width of the polyline
                        polylineOptions.width(10);
                        // Adding the taped point to the ArrayList
                        polyLineList.add(latlng);
                        // Setting points of polyline
                        polylineOptions.addAll(polyLineList);
                        // Adding the polyline to the map
                        mMap.addPolyline(polylineOptions);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10));

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

    /*private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length()-1;
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }*/
}

package com.lambton.projects.note_wethree_android.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.lambton.projects.note_wethree_android.R;
import com.lambton.projects.note_wethree_android.Utils;
import com.lambton.projects.note_wethree_android.volley.GetByVolley;
import com.lambton.projects.note_wethree_android.volley.VolleySingleton;

import org.json.JSONObject;

import java.io.IOException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener
{
    private static final int REQUEST_CODE = 1;
    private static final float DEFAULT_ZOOM_LEVEL = 10.0f;

    private GoogleMap mMap;
    private double mLat, mLng;
    private LocationManager mLocationManager;
    private Location mUserLocation;
    private ToggleButton mToggleButton;
    private String mMode = "Driving";
    private int mStrokeColor = Color.RED;
    private String [] mInfo = null;
    private Marker mInfoMarker = null;
    private String mTitle, mSnippet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setMemberVariables();
        checkPermissions();
    }

    private void setMemberVariables()
    {
        mToggleButton = findViewById(R.id.toggleButton);
        Intent intent = getIntent();
        mLat = intent.getDoubleExtra("lat", 0);
        mLng = intent.getDoubleExtra("lng", 0);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Method that checks User Location access and request permission
     */
    private void checkPermissions()
    {
        if (!hasLocationPermission())
        {
            requestLocationPermission();
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
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mMap.setOnPolylineClickListener(this);
        setDestination();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestLocationPermission();
            return;
        }
        enableUserLocationAndZoom();
    }

    private void setDestination()
    {
        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(mLat, mLng));
        Marker marker = mMap.addMarker(markerOptions);
        geocodeAsync(marker);
    }

    /**
     * Method to request Location Permission
     */
    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    /**
     * Method to check if the app has User Location Permission
     * @return - True if the app has User Location Permission
     */
    private boolean hasLocationPermission()
    {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (mMap != null)
                {
                    enableUserLocationAndZoom();
                }
            }
        }
    }

    /**
     * Method to show User Location on Map and Zoom onto it
     */
    @SuppressLint("MissingPermission")
    private void enableUserLocationAndZoom()
    {
        mMap.setMyLocationEnabled(true);
        mUserLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (mUserLocation != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mUserLocation.getLatitude(), mUserLocation.getLongitude()), DEFAULT_ZOOM_LEVEL));
        }
    }

    private void geocodeAsync(Marker marker)
    {
        new Thread(() ->
        {
            try
            {
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                Address address = geocoder.getFromLocation(mLat, mLng, 1).get(0);
                String[] info = Utils.getFormattedAddress(address);
                MapsActivity.this.runOnUiThread(() ->
                {
                    marker.setTitle(info[0]);
                    marker.setSnippet(info[1]);
                    mTitle = info[0];
                    mSnippet = info[1];
                });
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public void navigateClicked(View view)
    {
        LatLng latLng = new LatLng(mLat,mLng);
        Location location = new Location("");
        location.setLongitude(mLng);
        location.setLatitude(mLat);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, getDirectionURL(latLng), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mInfo = GetByVolley.getDirection(response,mMap,location,mStrokeColor, mTitle, mSnippet);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d(TAG," onErrorResponse: "+error);
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private String getDirectionURL(LatLng latLng) {
        StringBuilder URL = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        URL.append("origin="+mUserLocation.getLatitude() + ","+ mUserLocation.getLongitude());
        URL.append("&destination="+latLng.latitude+","+latLng.longitude);
        URL.append("&key="+getString(R.string.google_maps_key));
        URL.append("&mode="+mMode);
        System.out.println(URL);
        return URL.toString();
    }

    public void modeClicked(View view)
    {
        if(mToggleButton.isChecked())
        {
            mStrokeColor = Color.GREEN;
            mMode = "Walking";
        }
        else
        {
            mStrokeColor = Color.RED;
            mMode = "Driving";
        }
    }

    @Override
    public void onPolylineClick(Polyline polyline)
    {
        if(mInfo != null)
        {
            if(mInfoMarker != null)
            {
                mInfoMarker.remove();
                mInfoMarker = null;
            }
            LatLng place1 = polyline.getPoints().get(0);
            LatLng place2 = polyline.getPoints().get(1);
            double lat = (place1.latitude + place2.latitude)/2;
            double lng = (place1.longitude + place2.longitude)/2;
            BitmapDescriptor transparent = BitmapDescriptorFactory.fromResource(R.mipmap.transparent);
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat,lng)).title("Distance: "+mInfo[0]).snippet("Duration: "+mInfo[1]).icon(transparent);
            mInfoMarker = mMap.addMarker(markerOptions);
            mInfoMarker.showInfoWindow();
        }
    }
}
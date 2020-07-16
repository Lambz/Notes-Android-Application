package com.lambton.projects.note_wethree_android.volley;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class GetByVolley {

    public static String[] getDirection(JSONObject jsonObject, GoogleMap googleMap, Location location, int strokeColor, String title, String snippet)
    {
        HashMap<String, String> distances = null;
        VolleyParser directionParser = new VolleyParser();
        distances = directionParser.parseDistance(jsonObject);
        String distance = distances.get("distance");
        String duration = distances.get("duration");
        String [] directionsList = directionParser.parseDirections(jsonObject);
        displayDirections(directionsList, distance, duration, googleMap, location, strokeColor, title, snippet);
        return new String[] {distance,duration};
    }

    private static void displayDirections(String[] directionsList, String distance, String duration, GoogleMap googleMap, Location location, int strokeColor, String title, String snippet) {
        googleMap.clear();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions options = new MarkerOptions().position(latLng)
                .title(title)
                .snippet(snippet);
        googleMap.addMarker(options);
        for(String direction: directionsList)
        {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(strokeColor)
                    .width(10)
                    .addAll(PolyUtil.decode(direction)).clickable(true);
            googleMap.addPolyline(polylineOptions);
        }
    }
}

package com.lambton.projects.note_wethree_android.volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VolleyParser {

    public HashMap<String, String> parseDistance(JSONObject jsonObject)
    {
        JSONArray jsonArray = null;
        try
        {
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getDuration(jsonArray);
    }

    private HashMap<String, String> getDuration(JSONArray jsonArray)
    {
        HashMap<String,String> distanceDurationDictinory = new HashMap<>();
        String distance = "";
        String duration = "";

        try
        {
            duration = jsonArray.getJSONObject(0).getJSONObject("duration").getString("text");
            distance = jsonArray.getJSONObject(0).getJSONObject("distance").getString("text");
            distanceDurationDictinory.put("duration",duration);
            distanceDurationDictinory.put("distance",distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return distanceDurationDictinory;
    }

    public String[] parseDirections(JSONObject jsonObject)
    {
        JSONArray jsonArray = null;
        try
        {
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPaths(jsonArray);
    }

    private String[] getPaths(JSONArray jsonArray) {
        int count = jsonArray.length();
        String [] polylinePoints = new String [count];
        for(int i=0; i < count; i++)
        {
            try {
                polylinePoints[i] = getPath(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return polylinePoints;
    }

    private String getPath(JSONObject jsonObject) {
        String polylinePoint = "";
        try {
            polylinePoint = jsonObject.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return polylinePoint;
    }

}

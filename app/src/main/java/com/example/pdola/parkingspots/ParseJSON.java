package com.example.pdola.parkingspots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pdola on 27.03.2017.
 */

public class ParseJSON {
    public static String[] id;
    public static String[] address;
    public static String[] space;
    public static String[] freeSpace;
    public static String[] latitude;
    public static String[] longitude;


    public static final String JSON_ARRAY = "result";
    public static final String KEY_ID = "id";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_SPACE = "space";
    public static final String KEY_FREE_SPACE = "freeSpace";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    private JSONArray parkingspots = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            parkingspots = jsonObject.getJSONArray(JSON_ARRAY);

            id = new String[parkingspots.length()];
            address = new String[parkingspots.length()];
            space = new String[parkingspots.length()];
            freeSpace = new String[parkingspots.length()];
            latitude = new String[parkingspots.length()];
            longitude = new String[parkingspots.length()];

            for(int i=0;i<parkingspots.length();i++){
                JSONObject jo = parkingspots.getJSONObject(i);
                id[i] = jo.getString(KEY_ID);
                address[i] = jo.getString(KEY_ADDRESS);
                space[i] = jo.getString(KEY_SPACE);
                freeSpace[i] = jo.getString(KEY_FREE_SPACE);
                latitude[i] = jo.getString(KEY_LATITUDE);
                longitude[i] = jo.getString(KEY_LONGITUDE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.example.pdola.parkingspots;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {


    // Might be null if Google Play services APK is not available.
    GoogleMap mMap;
    MapFragment mf;
    TextView textView;
    TextView textViewNearest;
    MyLocationListener locationListener;
    LocationManager locationManager;
    private Context context;
    Marker marker;
    DatabaseHelper myDatabase;

    Button viewAllButton;
    Button showAllButton;


    // URL to get contacts JSON
    private static String url = "https://parkingspots.000webhostapp.com/select.php";

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getActionBar().setHomeButtonEnabled(true);
        // getActionBar().setDisplayHomeAsUpEnabled(true);

        mf = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mf.getMapAsync(this);
        textView = (TextView) findViewById(R.id.tvText);
        textViewNearest = (TextView) findViewById(R.id.tvNearest);

        context = this.getApplicationContext();
//Setting listeners and managers
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(mMap, marker, context);
        viewAllButton = (Button) findViewById(R.id.buttonShow);
        showAllButton = (Button) findViewById(R.id.buttonShowAll);
        myDatabase = new DatabaseHelper(this);


        contactList = new ArrayList<>();


        viewAll();
        goToParkingsList();
    }


    public void goToParkingsList() {
        showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ParkingsList.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }


    public void viewAll() {
        viewAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = myDatabase.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Nothing Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("ID: " + res.getString(0) + "\n");
                    buffer.append("Address: " + res.getString(1) + "\n");
                    buffer.append("Space: " + res.getString(2) + "\n");
                    buffer.append("Free Space: " + res.getString(3) + "\n");
                    buffer.append("Latitude: " + res.getString(4) + "\n");
                    buffer.append("Longitude: " + res.getString(5) + "\n\n");
                }
                showMessage("Data", buffer.toString());
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    @Override
    public void onMapLoaded() {
        Location loc = null;
        try {
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 13));

        if (loc != null && mMap != null) {
            try {
                // Setup the location updates and gps status listener
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
                // provider, min time/distance

            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        locationListener.setMap(map);
        //responsible for showing chathead with data of exact point
        map.setInfoWindowAdapter(new ToDotAdapter(getLayoutInflater()));

        mMap = map;
        //setting map type on terrain
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        // calls onMapLoaded when layout done
        map.setOnMapLoadedCallback(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null) {
            try {
                // If the map is already loaded - setup the location updates and gps status listener
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);
                // provider, min time/distance

            } catch (SecurityException e) {
                Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause(); // Stop all ongoing updates
        try {
            locationManager.removeUpdates(locationListener);
        } catch (SecurityException e) {
            Log.e("PERMISSION_EXCEPTION", "PERMISSION_NOT_GRANTED");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}



/*
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        // haversine great circle distance approximation, returns meters
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60; // 60 nautical miles per degree of seperation
        dist = dist * 1852; // 1852 meters per nautical mile
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }*/

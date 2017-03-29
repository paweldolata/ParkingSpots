package com.example.pdola.parkingspots;

/**
 * Created by pdola on 20.03.2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

public class MyLocationListener implements android.location.LocationListener {
        public GoogleMap mMap;
        public String latitude;
        public String longitude;
        private Context context;
        public Marker marker;



        public MyLocationListener(GoogleMap mMap2, Marker marker2, Context con) {
            this.mMap=mMap2;
            this.marker=marker2;
            this.context=con;

        }



        public void setMap(GoogleMap mMap){
            this.mMap = mMap;
        }


        @Override
        public void onLocationChanged(final Location location) {
            if (marker != null) {
                marker.remove();
            }
            int px = this.context.getResources().getDimensionPixelSize(R.dimen.map_dot_marker_size);
            Bitmap mDotMarkerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(mDotMarkerBitmap);
            Drawable shape = this.context.getResources().getDrawable(R.drawable.current_position);
            shape.setBounds(0, 0, mDotMarkerBitmap.getWidth(), mDotMarkerBitmap.getHeight());
            shape.draw(canvas);

            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(mDotMarkerBitmap)).title(String.format("Latitude/Longitude: " + location.getLatitude() + "/" + location.getLongitude() )));


            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
            Log.d(TAG, "Received GPS request for " + String.valueOf(latitude) + "," + String.valueOf(longitude)       + " , ready to rumble!");
        }




        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    }



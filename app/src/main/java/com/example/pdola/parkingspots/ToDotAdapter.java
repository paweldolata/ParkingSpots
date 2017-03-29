package com.example.pdola.parkingspots;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by pdola on 20.03.2017.
 */

public class ToDotAdapter implements GoogleMap.InfoWindowAdapter {
        private View popup=null;
        private LayoutInflater inflater=null;

        ToDotAdapter(LayoutInflater inflater) {
            this.inflater=inflater;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return(null);
        }

        //Indicates that Lint should ignore the specified warnings for the annotated element.
        @SuppressLint("InflateParams")
        @Override
        public View getInfoContents(Marker marker) {
            if (popup == null) {
                popup=inflater.inflate(R.layout.popup, null);
            }

            TextView tv=(TextView)popup.findViewById(R.id.title);

            tv.setText(marker.getTitle());

            return(popup);
        }
}

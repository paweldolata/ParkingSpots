package com.example.pdola.parkingspots;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListItemDetailActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_detail);
        textView = (TextView) findViewById(R.id.tvNearest);
        String data = getIntent().getExtras().getString("address");
        textView.setText(data);
    }
}

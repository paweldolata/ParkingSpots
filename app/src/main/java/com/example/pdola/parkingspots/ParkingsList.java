package com.example.pdola.parkingspots;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParkingsList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private TextView textView;
    private String TAG = MainActivity.class.getSimpleName();
    // URL to get contacts JSON
    private static String url = "https://parkingspots.000webhostapp.com/select.php";

    ArrayList<HashMap<String, String>> contactList;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkings_list);
        listView = (ListView) findViewById(R.id.list);
        textView = (TextView) findViewById(R.id.tvText);
        contactList = new ArrayList<>();
        listView.setOnItemClickListener(this);

        new GetContacts().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i("HelloListView", "You clicked Item: " + id + " at position:" + position);
        // Then you start a new Activity via Intent
        Intent intent = new Intent(this, ListItemDetailActivity.class);
        // Or / And
        String address = contactList.get(position).get(id);
        String space = contactList.get(position).get(id);
        String freeSpace = contactList.get(position).get(id);
        intent.putExtra("id", id);
        intent.putExtra("address", address);
        intent.putExtra("space",space);
        intent.putExtra("freeSpace",freeSpace);

        startActivity(intent);
    }

    private class GetContacts extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ParkingsList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPHandler sh = new HTTPHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String address = c.getString("address");
                        String space = c.getString("space");
                        String freeSpace = c.getString("freeSpace");
                        String latitude = c.getString("latitude");
                        String longitude = c.getString("longitude");



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("address", address);
                        contact.put("space", space);
                        contact.put("freeSpace",freeSpace);
                        contact.put("latitude",latitude);
                        contact.put("longitude",longitude);

                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ParkingsList.this, contactList,
                    R.layout.list_item, new String[]{"address", "freeSpace",
                    "space"}, new int[]{R.id.address,
                    R.id.freeSpace, R.id.space});

            listView.setAdapter(adapter);
        }

    }
}

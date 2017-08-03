package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String CurrentSelectDatabase;
    String CurrentSelectURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        CallToDatabase newDatabase = new CallToDatabase();
        newDatabase.execute();
    }

    private class CallToDatabase extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
            final String CurrentSelectDatabase = prefs.getString("SelectedDatabase", "No Database Selected");
            final String CurrentDataCall = prefs.getString("CurrentDataCall", "No Call Selected");

            String location = CurrentSelectDatabase + "brapi/v1/"+CurrentDataCall +"?pageSize=9000";


            try {
                //JSON connection
                URL url = new URL(location);
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURL.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();

                } finally {
                    httpURL.disconnect();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, location);
            return  location;
        }

        @Override
        protected void onPostExecute(final String response) {

            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONObject metadata = jsonResponse.getJSONObject("metadata");
                JSONObject result = jsonResponse.getJSONObject("result");
                JSONArray data = result.getJSONArray("data");
                for(int i = 0; i < data.length(); i++)
                {
                    JSONObject dataObject = data.getJSONObject(i);
                    double longitude = 0.000;
                    double latitude = 0.000;
                    String name = dataObject.getString("name");

                    try
                    {
                        longitude = dataObject.getDouble("longitude");
                        latitude = dataObject.getDouble("latitude");
                    }

                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    LatLng sydney = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(sydney).title(name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

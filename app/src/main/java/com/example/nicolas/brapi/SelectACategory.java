package com.example.nicolas.brapi;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class SelectACategory extends AppCompatActivity {
    String callKey;
    String callValue;
    String CurrentSelectDatabase;


    public static final String CurrentDataCall = "com.example.nicolas.brapi";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_acategory);

        SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
        CurrentSelectDatabase = prefs.getString("SelectedDatabase", "https://cassavabase.org/");
        callValue = prefs.getString("callValue", "");

        TextView txt = (TextView) findViewById(R.id.CurrentDatabase);
        txt.setText("Database Picked: " + CurrentSelectDatabase);

        CallToDatabase newCall = new CallToDatabase();
        newCall.execute();
    }

    private class CallToDatabase extends AsyncTask<Void, Void, ArrayList> {
        @Override
        protected ArrayList doInBackground(Void... voids) {
            try {
                URL url = new URL(CurrentSelectDatabase + "brapi/v1/calls?pageSize=100");
                Log.d(TAG, CurrentSelectDatabase);
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURL.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    String completeCallResponse = stringBuilder.toString();
                    ArrayList filteredCallResponse = new ArrayList();
                    JSONObject jObj;
                    try {
                        jObj = new JSONObject(completeCallResponse);

                        JSONObject metadata = jObj.getJSONObject("metadata");
                        JSONObject results = jObj.getJSONObject("result");
                        JSONArray data = results.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject temp = data.getJSONObject(i);
                            Log.d(TAG, temp.toString());
                            final String call = temp.getString("call");
                            if(call.equals("token") || call.equals("observationlevels") || call.contains("/") || call.contains("maps"))
                            {
                                Log.d(TAG,call);
                            }
                            else if (call.contains("germplasm/id/") || call.contains("studies/id/"))
                            {
                                filteredCallResponse.add(call);
                            }
                            else
                            {
                                filteredCallResponse.add(call);
                            }

                        }
                        return filteredCallResponse;

                    } catch (JSONException e) {

                    }

                } finally {
                    httpURL.disconnect();
                }

            } catch (Exception e) {
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList response) {

            for(int i = 0; i < response.size(); i++)
            {

                final String replacemment = response.get(i).toString();
                //Create Button here
                final Button CategoryButton = new Button(getApplicationContext());
                CategoryButton.setText(replacemment);

                LinearLayout eleven = (LinearLayout) findViewById(R.id.LLPickACaegory);
                ConstraintLayout.LayoutParams twelve = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                eleven.addView(CategoryButton, twelve);

                CategoryButton.setBackgroundResource(R.drawable.createdbutton_style);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) CategoryButton.getLayoutParams();
                params.setMargins(25, 60, 25, 5); //left, top, right, bottom
                CategoryButton.setPadding(0, 40, 0, 40);
                CategoryButton.setTextColor(Color.BLACK);

                CategoryButton.setLayoutParams(params);

                //attach onClickListener locations
                if(replacemment.equals("locations"))
                {
                    CategoryButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                            editor.putString("callKey", callKey);
                            editor.putString("callValue", callValue);
                            editor.putString("CurrentDataCall", replacemment);
                            editor.apply();

                            Intent GetCrop = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(GetCrop);
                        }
                    });
                }
                else {

                    //attach onClickListener general
                    CategoryButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                            editor.putString("callKey", callKey);
                            editor.putString("callValue", callValue);
                            editor.putString("CurrentDataCall", replacemment);
                            editor.apply();

                            Intent GetCrop = new Intent(getApplicationContext(), CallToURL.class);
                            startActivity(GetCrop);
                        }
                    });
                }
            }
        }
    }


}

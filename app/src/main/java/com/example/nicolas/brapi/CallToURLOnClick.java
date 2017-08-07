package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class CallToURLOnClick extends AppCompatActivity {

    String CurrentSelectDatabase;
    String currentDetailCall;
    String StringObjValue;
    String StringKeyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_urlon_click);

        SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
        CurrentSelectDatabase = prefs.getString("SelectedDatabase", "");

        Intent intent = getIntent();
        currentDetailCall = intent.getStringExtra("currentDetailCall");

        CallToDatabase someData = new CallToDatabase();
        someData.execute();
    }

    private class CallToDatabase extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids) {


            try {
                SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
                StringObjValue = prefs.getString("StringObjValue", "");
                StringKeyValue = prefs.getString("StringKeyValue", "");
                String builtUrl = CurrentSelectDatabase +currentDetailCall;

                    Log.d(TAG, builtUrl);
                    URL url = new URL(builtUrl);
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
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response)
        {

            LinearLayout myLayout = (LinearLayout) findViewById(R.id.OnClickContent);
            String StringBufferData = "";
            if(response == null)
            {
                response = "THERE WAS AN ERROR";
            }

            JSONObject jObj = null;
            try {
                jObj = new JSONObject(response);

                JSONObject metadata = jObj.getJSONObject("metadata");
                JSONArray status = metadata.getJSONArray("status");
                JSONArray datafiles = metadata.getJSONArray("datafiles");
                try {
                    JSONObject pagination = metadata.getJSONObject("pagination");
                }catch (JSONException e) {

                }

                JSONObject result = jObj.getJSONObject("result");
                JSONArray data = null;
                try {
                    data = result.getJSONArray("data");
                }catch (JSONException e){

                }

                String Something = "";
                String StringPageSuff = "";

                    final LinearLayout aNewLinLayout = new LinearLayout(getApplicationContext());
                    aNewLinLayout.setOrientation(LinearLayout.VERTICAL);
                    aNewLinLayout.setBackgroundResource(R.drawable.boarder_style);


                    for(Iterator<String> iter = result.keys();iter.hasNext();)
                    {
                        final LinearLayout aNewLinLayoutHoriz = new LinearLayout(getApplicationContext());
                        aNewLinLayoutHoriz.setOrientation(LinearLayout.HORIZONTAL);

                        final TextView theOtherText = new TextView(getApplicationContext());
                        final TextView rowTextView = new TextView(getApplicationContext());

                        String key = iter.next();
                        String objValue = "";


                        if(currentDetailCall.contains("markerprofile"))
                        {
                            try {
                                Object value = result.get(key);
                                objValue = result.get(key).toString();
                                Something += key + ": " + value + "\n";

                                Log.d(TAG, Something);

                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                            try {

                                for (int j = 0; j < data.length(); j++)
                                {
                                    JSONObject dataObj = data.getJSONObject(j);

                                }
                            } catch (JSONException e) {

                            }

                            rowTextView.setText(key + ": ");
                            theOtherText.setText(objValue);

                            aNewLinLayoutHoriz.addView(rowTextView);
                            aNewLinLayoutHoriz.addView(theOtherText);


                            aNewLinLayout.addView(aNewLinLayoutHoriz);
                        }
                        else
                        {
                            try {
                                Object value = result.get(key);
                                objValue = result.get(key).toString();
                                Something += key + ": " + value + "\n";

                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                            rowTextView.setText(key + ": ");
                            theOtherText.setText(objValue);

                            aNewLinLayoutHoriz.addView(rowTextView);
                            aNewLinLayoutHoriz.addView(theOtherText);

                            aNewLinLayout.addView(aNewLinLayoutHoriz);
                        }





                        }


                    myLayout.addView(aNewLinLayout);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) aNewLinLayout.getLayoutParams();
                    params.setMargins(0, 0, 0, 20);
                    aNewLinLayout.setPadding(10,0,0,10);
                    aNewLinLayout.setLayoutParams(params);


            } catch (JSONException e) {
                e.printStackTrace();
                TextView worstCaseScenarioText = new TextView(getApplicationContext());
                worstCaseScenarioText.setText(response);
                myLayout.addView(worstCaseScenarioText);
            }

        }
    }

}

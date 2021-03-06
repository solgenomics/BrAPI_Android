package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class CallToURL extends AppCompatActivity {

    EditText et1;
    EditText et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_url);

        SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
        final String CurrentSelectDatabase = prefs.getString("SelectedDatabase", "No Database Selected");
        final String CurrentDataCall = prefs.getString("CurrentDataCall", "No Call Selected");


        MyTaskParams params = new MyTaskParams(CurrentSelectDatabase, CurrentDataCall);
        CallToDatabase myTask = new CallToDatabase();
        myTask.execute(params);

        // Connectivity Verification -----------------------------
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
        } else {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
        //---------------------------------------------------------

    }

    public void onButtonSpecify(View view) {
        Intent intent = new Intent(this, SpecifyRec.class);
        startActivity(intent);
    }

    public void onPageSizeChange(View view) {
        Intent intent = new Intent(getApplicationContext(), changePageSize.class);
        startActivity(intent);
    }


    //===============================================================================================================================================

    public static class MyTaskParams {
        String Select;
        String Call;

        MyTaskParams(String Select, String Call) {
            this.Select = Select;
            this.Call = Call;
        }
    }

    //===============================================================================================================================================
    private class CallToDatabase extends AsyncTask<MyTaskParams, Void, String> {
        @Override
        protected String doInBackground(MyTaskParams... params) {
            String CurrentDatabase = params[0].Select;
            String CurrentDataCall = params[0].Call;


            try {
                String builtUrl = CurrentDatabase + "brapi/v1/" + CurrentDataCall + "?";
                SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
                String ArrayParameters = prefs.getString("ListArrayParameters", "No Parameters");

                String PageSizeInfo = prefs.getString("pageSize", "10");
                String CurrentPageInfo = prefs.getString("currentPage", "0");

                try {
                    JSONArray parametersArray = new JSONArray(ArrayParameters);
                    for (int i = 0; i < parametersArray.length(); i++) {
                        String paramKey = parametersArray.getString(i);
                        //ListOfArraysParams[i] = someValue;

                        String textValue = prefs.getString(paramKey, "");

                        builtUrl += "&" + paramKey + "=" + textValue;

                    }

                    if (!PageSizeInfo.equals("")) {
                        builtUrl += "&" + "pageSize=" + PageSizeInfo;
                    }
                    if (!CurrentPageInfo.equals("")) {
                        builtUrl += "&" + "page=" + CurrentPageInfo;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        protected void onPostExecute(String response) {

            String StringBufferData = "";
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            TextView txt = (TextView) findViewById(R.id.germplasmSearchResult);
            txt.setText(null);

            TextView pageSize = (TextView) findViewById(R.id.pageSize);
            TextView currentPage = (TextView) findViewById(R.id.currentPage);
            TextView totalPages = (TextView) findViewById(R.id.totalPages);
            TextView totalCount = (TextView) findViewById(R.id.totalCount);

            LinearLayout myLayout = (LinearLayout) findViewById(R.id.ListTextViews);

            JSONObject jObj = null;
            try {
                jObj = new JSONObject(response);

                JSONObject metadata = jObj.getJSONObject("metadata");
                JSONArray status = metadata.getJSONArray("status");
                JSONArray datafiles = metadata.getJSONArray("datafiles");
                JSONObject pagination = metadata.getJSONObject("pagination");

                JSONObject results = jObj.getJSONObject("result");
                JSONArray data = results.getJSONArray("data");

                String Something = "";
                String StringPageSuff = "";

                pageSize.setText("pageSize" + ": " + pagination.get("pageSize"));
                currentPage.setText("currentPage" + ": " + pagination.get("currentPage"));
                totalPages.setText("totalPages" + ": " + pagination.get("totalPages"));
                totalCount.setText("totalCount" + ": " + pagination.get("totalCount"));

                LinearLayout linearLayoutwithStuff = (LinearLayout) findViewById(R.id.linearLayoutwithStuff);
                linearLayoutwithStuff.setBackgroundResource(R.drawable.rounded_corner_style);
                linearLayoutwithStuff.setPadding(10, 0, 0, 0);

                for (int i = 0; i < data.length(); i++) {
                    final LinearLayout aNewLinLayout = new LinearLayout(getApplicationContext());
                    aNewLinLayout.setOrientation(LinearLayout.VERTICAL);
                    aNewLinLayout.setBackgroundResource(R.drawable.boarder_style);


                    try {

                        JSONObject temp = data.getJSONObject(i);
                        SharedPreferences.Editor editor= getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                        String currentDetailCall = "";

                        for (Iterator<String> iter = temp.keys(); iter.hasNext(); ) {
                            final LinearLayout aNewLinLayoutHoriz = new LinearLayout(getApplicationContext());
                            aNewLinLayoutHoriz.setOrientation(LinearLayout.HORIZONTAL);

                            final TextView theOtherText = new TextView(getApplicationContext());
                            final TextView rowTextView = new TextView(getApplicationContext());

                            String key = iter.next();
                            String objValue = "";
                            if (temp.get(key).toString().equals("null") || temp.get(key).toString().equals("NA/NA") || temp.get(key).toString().equals("[]")
                                    || temp.get(key).toString().equals("") || temp.get(key).toString().equals("{}")) {

                            } else {
                                try {
                                    Object value = temp.get(key);
                                    objValue = temp.get(key).toString();
                                    Something += key + ": " + value + "\n";
                                    if (key.equals("markerProfileDbId")) {
                                        currentDetailCall = "brapi/v1/markerprofiles/" + objValue+"?"+"pageSize=30000";
                                        editor.putString("StringObjValue", objValue);
                                        editor.apply();
                                    }

                                    if (key.equals("germplasmDbId")) {
                                        currentDetailCall = "brapi/v1/germplasm/" + objValue;
                                        editor.putString("StringObjValue", objValue);
                                        editor.apply();
                                    }

                                    if (key.equals("traitDbId")) {
                                        currentDetailCall = "brapi/v1/traits/" + objValue;
                                        editor.putString("StringObjValue", objValue);
                                        editor.apply();
                                    }




                                } catch (JSONException e) {
                                    // Something went wrong!
                                }
                                rowTextView.setText("  " + key + ": ");
                                rowTextView.setTextColor(Color.WHITE);
                                theOtherText.setText(objValue);
                                theOtherText.setTextColor(Color.WHITE);

                                aNewLinLayoutHoriz.addView(rowTextView);
                                aNewLinLayoutHoriz.addView(theOtherText);

                                aNewLinLayout.addView(aNewLinLayoutHoriz);

                            }
                            aNewLinLayout.setId(i);
                        }

                        final String finalCurrentDetailCall = currentDetailCall;

                        if (currentDetailCall.equals("")) {

                        } else {
                            aNewLinLayout.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getApplicationContext(), CallToURLOnClick.class);
                                    intent.putExtra("currentDetailCall", finalCurrentDetailCall);
                                    startActivity(intent);
                                }
                            });
                        }


                    } catch (JSONException e) {
                        JSONArray temper = data.getJSONArray(i);


                        final LinearLayout aNewLinLayoutHoriz = new LinearLayout(getApplicationContext());
                        aNewLinLayoutHoriz.setOrientation(LinearLayout.HORIZONTAL);


                        for (int k = 0; k < temper.length(); k++) {
                            final TextView theOtherText = new TextView(getApplicationContext());
                            theOtherText.setText(temper.toString(k));
                            aNewLinLayoutHoriz.addView(theOtherText);
                        }
                        aNewLinLayout.addView(aNewLinLayoutHoriz);


                    }

                    myLayout.addView(aNewLinLayout);


                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) aNewLinLayout.getLayoutParams();
                    params.setMargins(0, 0, 0, 20);
                    aNewLinLayout.setPadding(10, 10, 10, 20);
                    aNewLinLayout.setLayoutParams(params);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                TextView worstCaseScenarioText = new TextView(getApplicationContext());
                worstCaseScenarioText.setText(response);
                myLayout.addView(worstCaseScenarioText);
            }

        }


    }

}

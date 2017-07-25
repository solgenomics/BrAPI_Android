package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class CallToURL extends AppCompatActivity
{

    EditText et1;
    EditText et2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        if(nInfo!=null && nInfo.isConnected())
        {}
        else
        {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
        //---------------------------------------------------------

    }
    public void onButtonSpecify(View view)
    {
        Intent intent = new Intent(this, SpecifyRec.class);
        startActivity(intent);
    }

    public void onPageSizeChange(View view)
    {
        Intent intent = new Intent(getApplicationContext(), changePageSize.class);
        startActivity(intent);
    }


    public static class MyTaskParams
    {
        String Select;
        String Call;

        MyTaskParams(String Select, String Call)
        {
            this.Select = Select;
            this.Call = Call;
        }
    }

    private class CallToDatabase extends AsyncTask<MyTaskParams, Void, String>
    {
        @Override
        protected String doInBackground(MyTaskParams...params)
        {
            String CurrentDatabase = params[0].Select;
            String CurrentDataCall = params[0].Call;



            try {
                String builtUrl = CurrentDatabase + CurrentDataCall + "?";
                SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
                String ArrayParameters = prefs.getString("ListArrayParameters", "No Parameters");

                SharedPreferences pageSizeChange = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
                String PageSizeInfo = pageSizeChange.getString("pageSizePrefs", "");

                try {
                    JSONArray parametersArray = new JSONArray(ArrayParameters);
                    for (int i = 0; i < parametersArray.length(); i++) {
                        String paramKey = parametersArray.getString(i);
                        //ListOfArraysParams[i] = someValue;

                        String textValue = prefs.getString(paramKey, "");

                        builtUrl += "&" + paramKey + "=" + textValue;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*
                try {

                JSONArray parametersArray = new JSONArray(PageSizeInfo);
                for (int i = 0; i < parametersArray.length(); i ++)
                {
                    String paramKey = parametersArray.getString(i);
                    //ListOfArraysParams[i] = someValue;

                    String textValue = pageSizeChange.getString(paramKey, "");
                    if(!textValue.equals("null"))
                        {
                            builtUrl += "&" + paramKey + "=" + textValue;
                        }
                    }
                } catch (JSONException e) {
                e.printStackTrace();
                 }
                 */


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

            String StringBufferData = "";
            if(response == null)
            {
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
                linearLayoutwithStuff.setBackgroundResource(R.drawable.boarder_style);
                linearLayoutwithStuff.setPadding(10,0,0,0);

                for (int i = 0; i < data.length(); i ++)
                {
                    final LinearLayout aNewLinLayout = new LinearLayout(getApplicationContext());
                    aNewLinLayout.setOrientation(LinearLayout.VERTICAL);
                    aNewLinLayout.setBackgroundResource(R.drawable.boarder_style);


                    JSONObject temp = data.getJSONObject(i);


                    String currentDetailCall = "";

                    for(Iterator<String> iter = temp.keys();iter.hasNext();)
                    {
                        final LinearLayout aNewLinLayoutHoriz = new LinearLayout(getApplicationContext());
                        aNewLinLayoutHoriz.setOrientation(LinearLayout.HORIZONTAL);

                        final TextView theOtherText = new TextView(getApplicationContext());
                        final TextView rowTextView = new TextView(getApplicationContext());

                        String key = iter.next();
                        String objValue = "";
                        if(temp.get(key).toString().equals("null") || temp.get(key).toString().equals("NA/NA") || temp.get(key).toString().equals("[]")
                                || temp.get(key).toString().equals("") || temp.get(key).toString().equals("{}"))
                        {

                        }
                        else
                        {
                            try {
                                Object value = temp.get(key);
                                objValue = temp.get(key).toString();
                                Something += key + ": " + value + "\n";

                                if (key.equals("germplasmDbId")){

                                    //SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                                    //editor.putString("currentDetailCall", "brapi/v1/germplasm/"+objValue);
                                    //editor.apply();

                                    currentDetailCall = "brapi/v1/germplasm/"+objValue;
                                }

                                else if (key.equals("studyDbId")){

                                    currentDetailCall =  "brapi/v1/studies/" + objValue;
                                }

                                else if (key.equals("trialDbId")){

                                    currentDetailCall = "brapi/v1/trials/" + objValue;
                                }

                                else if (key.equals("traitDbId")){

                                    currentDetailCall = "brapi/v1/traits/" + objValue;
                                }

                            } catch (JSONException e) {
                                // Something went wrong!
                            }
                            rowTextView.setText(key + ": ");
                            theOtherText.setText(objValue);

                            aNewLinLayoutHoriz.addView(rowTextView);
                            aNewLinLayoutHoriz.addView(theOtherText);

                            aNewLinLayout.addView(aNewLinLayoutHoriz);

                        }
                        aNewLinLayout.setId(i);
                    }

                    final String finalCurrentDetailCall = currentDetailCall;
                    aNewLinLayout.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(getApplicationContext(), CallToURLOnClick.class);
                            intent.putExtra("currentDetailCall", finalCurrentDetailCall);
                            startActivity(intent);
                        }
                    });




                    myLayout.addView(aNewLinLayout);


                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) aNewLinLayout.getLayoutParams();
                    params.setMargins(0, 0, 0, 20);
                    aNewLinLayout.setPadding(10,0,0,10);
                    aNewLinLayout.setLayoutParams(params);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        public void LayoutClick(View view)
        {
            Intent intent = new Intent(getApplicationContext(), changePageSize.class);
            startActivity(intent);
        }


    }

}

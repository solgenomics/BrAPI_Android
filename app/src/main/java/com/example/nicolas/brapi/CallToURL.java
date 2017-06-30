package com.example.nicolas.brapi;

import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class CallToURL extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_url);

        SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
        String CurrentSelectDatabase = prefs.getString("SelectedDatabase", "No Database Selected");

        new CallToDatabase().execute(CurrentSelectDatabase);
    }

    private class CallToDatabase extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String...params)
        {
            String CurrentDatabase = params[0];


            Log.d(TAG, CurrentDatabase);
            try
            {
                URL url = new URL(CurrentDatabase+"/brapi/v1/germplasm-search");
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();

                try
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURL.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    Log.d(TAG, stringBuilder.toString());
                    return stringBuilder.toString();

                }finally {
                    httpURL.disconnect();
                }

            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response)
        {

            if(response == null)
            {
                response = "THERE WAS AN ERROR";
            }
            TextView txt = (TextView) findViewById(R.id.germplasmSearchResult);
            txt.setText(response);

            Log.d(TAG, response);
        }
    }


}

package com.example.nicolas.brapi;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class PostToBrapi extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_brapi);

        Intent intent = getIntent();

        String CurrentCreateData = intent.getStringExtra(AddADatabase.CurrentCreatedData);
        String CurrentCreateURL = intent.getStringExtra(AddADatabase.CurrentCreatedURL);

        CallToURL.MyTaskParams params = new CallToURL.MyTaskParams(CurrentCreateData, CurrentCreateURL);
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

    private class CallToDatabase extends AsyncTask<CallToURL.MyTaskParams, Void, String>
    {

        @Override
        protected String doInBackground(CallToURL.MyTaskParams...params)
        {
            String CurrentDatabase = params[0].Select;
            String CurrentDataCall = params[0].Call;

            Log.d(TAG, CurrentDatabase);
            try
            {
                URL url = new URL("http://brapi.org/brapi/newdatabase");
                Log.d(TAG, "http://brapi.org/brapi/newdatabase");
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

            Log.d(TAG, response);
        }
    }



}

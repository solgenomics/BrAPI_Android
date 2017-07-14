package com.example.nicolas.brapi;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class POSTToLogin extends AppCompatActivity
{

    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_url);

        Intent intent = getIntent();
        String UserName = intent.getStringExtra(LoginInPage.Username);
        String PassWord = intent.getStringExtra(LoginInPage.Password);

        MyTaskParams params = new MyTaskParams(UserName, PassWord);

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

        progress = (ProgressBar) findViewById(R.id.progressBar);

        //Intent intent1 = new Intent(this, MainActivity.class);
        //startActivity(intent1);
    }



    public static class MyTaskParams
    {
        String UserName;
        String PassWord;

        MyTaskParams(String UserName, String PassWord)
        {
            this.UserName = UserName;
            this.PassWord = PassWord;
        }

    }

    private class CallToDatabase extends AsyncTask<MyTaskParams, Void, String>
    {

        @Override
        protected String doInBackground(MyTaskParams...params)
        {
            String CurrentUserName = params[0].UserName;
            String CurrentPassWord = params[0].PassWord;

            Log.d(TAG, CurrentUserName);
            try
            {
                URL url = new URL("https://test.brapi.org/brapi/v1/token");
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();

                httpURL.setRequestMethod("POST");
                httpURL.setRequestProperty("grant_type", "password");
                httpURL.setRequestProperty("username", CurrentUserName);
                httpURL.setRequestProperty("password", CurrentPassWord);
                httpURL.setDoOutput(true);

                //OutputStream outputPost = new BufferedOutputStream(httpURL.getOutputStream());
                //writeStream(outputPost);
                //outputPost.flush();
                //outputPost.close();
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

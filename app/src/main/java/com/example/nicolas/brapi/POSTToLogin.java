package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class POSTToLogin extends AppCompatActivity
{

    ProgressBar progress;
    String accessToken;
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
                //httpURL.setRequestProperty("grant_type", "password");
                //httpURL.setRequestProperty("username", CurrentUserName);
                //httpURL.setRequestProperty("password", CurrentPassWord);
                String data = URLEncoder.encode("grant_type", "UTF-8") + "=" + URLEncoder.encode("password", "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(CurrentUserName, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(CurrentPassWord, "UTF-8");

                httpURL.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(httpURL.getOutputStream());
                wr.write(data);
                wr.flush();

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

            JSONObject Resp;
            JSONObject metadata;
            String userDisplayName;
            accessToken = null;
            String expiresIn;
            try
            {
                Resp = new JSONObject(response);
                metadata = Resp.getJSONObject("metadata");
                userDisplayName = Resp.getString("userDisplayName");
                accessToken = Resp.getString("access_token");
                expiresIn = Resp.getString("expires_in");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            if(accessToken.equals("null"))
            {
                Intent LoginPage = new Intent(getApplicationContext(), LoginInPage.class);
                Toast.makeText(POSTToLogin.this, "Try Again!", Toast.LENGTH_SHORT).show();

                startActivity(LoginPage);
            }
            else
            {
                SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                editor.putString("accessToken", accessToken);
                editor.apply();

                Intent Entered = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(POSTToLogin.this, "Welcome!", Toast.LENGTH_SHORT).show();
                startActivity(Entered);

            }

        }
    }


}
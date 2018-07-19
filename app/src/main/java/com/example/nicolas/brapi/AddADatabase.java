package com.example.nicolas.brapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;


public class AddADatabase extends AppCompatActivity implements View.OnClickListener
{
    public static final String CurrentCreatedData = "PostToBrapiData";
    public static final String CurrentCreatedURL = "PostToBrapiURL";
    Button bAddData;
    EditText DatabaseName, URLFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adatabase);

        bAddData = (Button) findViewById(R.id.bCreateDatabase);
    }

    @Override
    public void onClick(View view)
    {
        DatabaseName = (EditText) findViewById(R.id.DatabaseName);
        URLFormat = (EditText) findViewById(R.id.URLFormat);

        String NewDatabaseName = DatabaseName.getText().toString();
        String NewURLFormat = URLFormat.getText().toString();


        CreateDatabase retreiveDatabaselist = new CreateDatabase();
        retreiveDatabaselist.execute();
    }

    //------------------------------------------------------------------------------------------------------------------------
    public class CreateDatabase extends AsyncTask<String, Void, String>
    {

        String NewDatabaseName = DatabaseName.getText().toString();
        String NewURLFormat = URLFormat.getText().toString();
        @Override
        protected String doInBackground(String... strings)
        {

            try
            {
                SharedPreferences editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
                String variable = editor.getString("accessToken", "0");

                URL url = new URL("https://cassavabase.org/brapiapp/new_database?databaseName=" + NewDatabaseName + "&databaseURL=" + NewURLFormat + "&accessToken=" + variable);
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
            Log.d(TAG, response);
            Intent intent = new Intent(getApplicationContext(), PickADatabase.class);
            Intent stayOnPage = new Intent(getApplicationContext(), AddADatabase.class);
            try
            {
                JSONObject JSONresponse = new JSONObject(response);
                try
                {
                    String errorMessage = JSONresponse.getString("error");
                    Toast.makeText(AddADatabase.this, errorMessage, Toast.LENGTH_LONG).show();
                    startActivity(stayOnPage);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                try
                {
                    String successMessage = JSONresponse.getString("success");
                    Toast.makeText(AddADatabase.this, "Database ADDED!", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------------
    //Async class here
    public class retreiveDatabaselist extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids)
        {

            try
            {
                URL url = new URL("https://cassavabase.org/brapiapp/list_databases");
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
                    return stringBuilder.toString();

                }finally {
                    httpURL.disconnect();
                }

            } catch (Exception e) {
                return null;
            }

        }


        @Override
        protected void onPostExecute(String response){


        }
    }
}

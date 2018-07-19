package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import static android.support.constraint.R.id.parent;

public class RemoveADatabase extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_adatabase);

        retreiveDatabaselist retreiveDatabase = new retreiveDatabaselist();
        retreiveDatabase.execute();
    }

    //------------------------------------------------------------------------------------------------------------------------
    public class deleteDatabase extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                URL url = new URL("https://cassavabase.org/brapiapp/remove_database");
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();


                SharedPreferences editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
                String variable = editor.getString("accessToken", "0");

                httpURL.setRequestMethod("POST");
                String data = URLEncoder.encode("databaseName", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8");
                data += "&" + URLEncoder.encode("accessToken", "UTF-8") + "=" + URLEncoder.encode(variable, "UTF-8");

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
            Intent intent = new Intent(getApplicationContext(), PickADatabase.class);
            Toast.makeText(RemoveADatabase.this, "Database REMOVED", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------
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

            JSONObject jObj = null;

            try {
                jObj = new JSONObject(response);
                JSONArray jArr = jObj.getJSONArray("database_list");


                for (int i = 0; i < jArr.length(); i++) {
                    JSONArray databaseArray = jArr.getJSONArray(i);
                    final String DatabaseName = databaseArray.getString(0);
                    final String DatabaseURL = databaseArray.getString(1);

                    if(DatabaseURL.contains("cassavabase") || DatabaseURL.contains("sweetpotatobase") || DatabaseURL.contains("musabase")
                            || DatabaseURL.contains("yambase"))
                    {

                    }
                    else
                    {
                        //button
                        Button myButton = new Button(getApplicationContext());
                        myButton.setText(DatabaseName);
                        myButton.setId(R.id.DatabaseName);

                        LinearLayout eleven = (LinearLayout) findViewById(R.id.LayoutOfButtonDelete);
                        ConstraintLayout.LayoutParams twelve = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                        eleven.addView(myButton, twelve);

                        myButton.setBackgroundResource(R.drawable.createdbutton_style);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) myButton.getLayoutParams();
                        params.setMargins(25, 60, 25, 5); //left, top, right, bottom
                        myButton.setPadding(0,40,0,40);
                        myButton.setTextColor(Color.BLACK);
                        myButton.setLayoutParams(params);

                        //attach onClickListener
                        myButton.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View view) {
                                deleteDatabase deleteDatabase = new deleteDatabase();
                                deleteDatabase.execute(DatabaseName);

                            }
                        });
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

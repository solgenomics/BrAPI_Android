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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.support.constraint.R.id.parent;

public class PickADatabase extends AppCompatActivity
{
    LinearLayout myLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_adatabase);

        retreiveDatabaselist retreiveDatabase = new retreiveDatabaselist();
        retreiveDatabase.execute();
    }

    public void selectedAddADatabase(View view)
    {
        Intent intent = new Intent(this, AddADatabase.class);
        startActivity(intent);
    }

    public void selectedRemoveADatabase(View view)
    {
        Intent intent2 = new Intent(this, RemoveADatabase.class);
        startActivity(intent2);
    }

    public class retreiveDatabaselist extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids)
        {

            try
            {
                URL url = new URL("https://test.brapi.org/brapiapp/list_databases");
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



                    LinearLayout eleven = (LinearLayout) findViewById(R.id.LayoutOfButtonCreate);

                    //Layout
                    myLayout = new LinearLayout(getApplicationContext());
                    myLayout.setId(R.id.DatabaseName);
                    myLayout.setOrientation(LinearLayout.VERTICAL);



                    //adding style && TextView
                    myLayout.setBackgroundResource(R.drawable.createdbutton_style);
                    TextView DBname = new TextView(getApplicationContext());
                    DBname.setText(DatabaseName);
                    DBname.setAllCaps(true);
                    DBname.setTextColor(Color.BLACK);
                    DBname.setGravity(1);

                    TextView URLName = new TextView(getApplicationContext());
                    URLName.setText("("+DatabaseURL+")");
                    URLName.setTextColor(Color.BLACK);
                    URLName.setGravity(1);

                    myLayout.addView(DBname);
                    myLayout.addView(URLName);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(25, 60, 25, 5); //left, top, right, bottom
                    myLayout.setPadding(0, 40, 0, 40);
                    myLayout.setLayoutParams(params);
                    eleven.addView(myLayout);

                    //attach onClickListener
                    myLayout.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                            editor.putString("SelectedDatabase", DatabaseURL);
                            editor.putString("SelectedDatabaseName", DatabaseName);
                            editor.apply();
                            Intent GetCrop = new Intent(getApplicationContext(), SelectACategory.class);
                            startActivity(GetCrop);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

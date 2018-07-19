package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.crypto.SealedObject;
import javax.net.ssl.HttpsURLConnection;
import static android.content.ContentValues.TAG;

public class SpecifyRec extends AppCompatActivity{

    LinearLayout FirstLin;


    String[] ArrayParameters;
    String JsonArrayParams;
    JSONArray jArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specify_rec);

        FirstLin = (LinearLayout) findViewById(R.id.SpecifyLinearLayout);
        FirstLin.setOrientation(LinearLayout.VERTICAL);

        retreiveDatabaselist retreiveDatabaselist = new retreiveDatabaselist();
        retreiveDatabaselist.execute();
    }

    public void CallToURL(View view) {

        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();

        editor.putString("ListArrayParameters", JsonArrayParams);

        for (int i = 0; i < jArr.length(); i++)
        {
            //editor.putString("ListArrayParameter",ArrayParameters[i]);
            EditText t = (EditText) findViewById(i);
            if(t.getText().equals(""))
            {

            }
            else {
                //editor.putString("ListArrayEditors", t.getText().toString());
                try {
                    editor.putString(jArr.getString(i), t.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        editor.apply();
        Intent intent = new Intent(this, CallToURL.class);
        startActivity(intent);

    }
    public class retreiveDatabaselist extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids)
        {
            try
            {
                String propExtention = "";
                SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
                String CurrentDataCall = prefs.getString("CurrentDataCall", "");

                String Something = CurrentDataCall;

                if(!CurrentDataCall.equals(null))
                {
                   if(Something.equals("brapi/v1/traits"))
                   {
                       Something="traits";
                   }
                   if(Something.equals("brapi/v1/germplasm-search"))
                   {
                        Something="germplasm-search";
                   }
                   if(Something.equals("brapi/v1/phenotypes-search"))
                   {
                        Something="phenotypes-search";
                   }
                   if(Something.equals("brapi/v1/locations"))
                   {
                        Something="locations";
                   }
                   if(Something.equals("brapi/v1/trials"))
                   {
                        Something="trials";
                   }
                   if(Something.equals("brapi/v1/studies-search"))
                   {
                        Something="studies-search";
                   }
                }



                //Change URL to User preferences
                URL url = new URL("https://cassavabase.org/brapiapp/searchparameters?call="+Something);
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
                    Log.d(TAG,stringBuilder.toString());
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
            JSONObject jObj = null;


            try
            {
                LinearLayout FirstLin = (LinearLayout) findViewById(R.id.SpecifyLinearLayout);
                FirstLin.setOrientation(LinearLayout.VERTICAL);
                jObj = new JSONObject(response);
                jArr = jObj.getJSONArray("parameters");
                JsonArrayParams = jArr.toString();

                for (int i = 0; i < jArr.length(); i++) {
                    LinearLayout SecondLin = new LinearLayout(getApplicationContext());
                    SecondLin.setOrientation(LinearLayout.HORIZONTAL);
                    String StringParameters = jArr.getString(i);

                    final TextView TextViewParameters = new TextView(getApplicationContext());
                    final EditText EditParameters = new EditText(getApplicationContext());

                    EditParameters.setId(i);
                    TextViewParameters.setText(StringParameters+ ": ");
                    SecondLin.addView(TextViewParameters);
                    SecondLin.addView(EditParameters);

                    RelativeLayout.LayoutParams twelve = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    SecondLin.setLayoutParams(twelve);

                    FirstLin.addView(SecondLin);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

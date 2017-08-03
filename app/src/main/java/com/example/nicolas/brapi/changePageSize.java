package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

public class changePageSize extends AppCompatActivity {

    Button ok;
    EditText etPageSize;
    EditText etCurrentPage;
    String pageSizeParams;
    JSONArray jArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_page_size);

        ok = (Button) findViewById(R.id.ok);
        etPageSize = (EditText) findViewById(R.id.ChangePageSize);
        etCurrentPage = (EditText) findViewById(R.id.ChangeCurrentPage);
}

    public void ChangePageSize(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();

            try {
                editor.putString("pageSize", etPageSize.getText().toString());
                editor.putString("currentPage", etCurrentPage.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        editor.apply();
        Intent intent = new Intent(getApplicationContext(), CallToURL.class);
        startActivity(intent);
    }
}

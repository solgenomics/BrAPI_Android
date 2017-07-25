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
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_page_size);

        ok = (Button) findViewById(R.id.ok);
        et = (EditText) findViewById(R.id.ChangePageSize);
    }

    public void ChangePageSize(View view)
    {
        SharedPreferences.Editor editor = (SharedPreferences.Editor) getSharedPreferences("pageSize", MODE_PRIVATE);
        editor.putString("pagesize", et.getText().toString());
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), CallToURL.class);
        startActivity(intent);
    }
}

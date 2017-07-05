package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;


public class PickACrop extends AppCompatActivity
{

    public static final String CurrentDataCall = "com.example.nicolas.brapi";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_acrop);

        Intent GetCrop = new Intent(this, CallToURL.class);

        GetCrop.putExtra(CurrentDataCall, "brapi/v1/crops");

        startActivity(GetCrop);
    }

}

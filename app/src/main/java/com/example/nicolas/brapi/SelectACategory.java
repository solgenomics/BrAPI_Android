package com.example.nicolas.brapi;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectACategory extends AppCompatActivity
{

    public static final String CurrentDataCall = "com.example.nicolas.brapi";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_acategory);

        SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
        String CurrentSelectDatabase = prefs.getString("SelectedDatabase", "No Database Selected");

        TextView txt = (TextView) findViewById(R.id.CurrentDatabase);
        txt.setText("Database Picked: " +CurrentSelectDatabase);
    }

    public void SelectTraits(View view)
    {


        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("CurrentDataCall","brapi/v1/traits");
        editor.apply();

        Intent GetCrop = new Intent(this, CallToURL.class);
        startActivity(GetCrop);
    }

    public void SelectGermplasm(View view)
    {

        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("CurrentDataCall","brapi/v1/germplasm-search");
        editor.apply();
        Intent GetCrop = new Intent(this, CallToURL.class);
        startActivity(GetCrop);
    }

    public void SelectTrials(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("CurrentDataCall","brapi/v1/trials");
        editor.apply();
        Intent GetCrop = new Intent(this, CallToURL.class);
        startActivity(GetCrop);
    }

    public void SelectPhenotypes(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("CurrentDataCall","brapi/v1/phenotypes-search");
        editor.apply();
        Intent GetCrop = new Intent(this, CallToURL.class);
        startActivity(GetCrop);
    }

    public void SelectLocations(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("CurrentDataCall","brapi/v1/locations");
        editor.apply();
        Intent GetCrop = new Intent(this, CallToURL.class);
        startActivity(GetCrop);
    }

    public void SelectStudies(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("CurrentDataCall","brapi/v1/studies-search");
        editor.apply();
        Intent GetCrop = new Intent(this, CallToURL.class);
        startActivity(GetCrop);
    }
}

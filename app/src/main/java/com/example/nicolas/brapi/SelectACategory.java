package com.example.nicolas.brapi;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectACategory extends MainActivity
{

    public static final String CurrentDataCall = "com.example.nicolas.brapi";
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_acategory);

    }

    public void SelectTraits(View view)
    {
        Intent GetCrop = new Intent(this, CallToURL.class);
        GetCrop.putExtra(CurrentDataCall, "brapi/v1/traits");
        startActivity(GetCrop);
    }

    public void SelectGermplasm(View view)
    {
        Intent GetCrop = new Intent(this, CallToURL.class);
        GetCrop.putExtra(CurrentDataCall, "brapi/v1/germplasm-search");
        startActivity(GetCrop);
    }

    public void SelectTrials(View view)
    {
        Intent GetCrop = new Intent(this, CallToURL.class);
        GetCrop.putExtra(CurrentDataCall, "brapi/v1/trials");
        startActivity(GetCrop);
    }

    public void SelectPhenotypes(View view)
    {
       Intent GetCrop = new Intent(this, CallToURL.class);
        GetCrop.putExtra(CurrentDataCall, "brapi/v1/phenotypes-search");
        startActivity(GetCrop);
    }

    public void SelectLocations(View view)
    {
        Intent GetCrop = new Intent(this, CallToURL.class);
        GetCrop.putExtra(CurrentDataCall, "brapi/v1/locations");
        startActivity(GetCrop);
    }
}
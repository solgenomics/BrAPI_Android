package com.example.nicolas.brapi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PickADatabase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_adatabase);
    }

    public void selectCassavaDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "CassavaDatebase");

        editor.apply();
    }

    public void selectSweetPotatoDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "SweetPotatoDatabase");

        editor.apply();
    }

    public void selectMusaDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "MusaDatabase");

        editor.apply();
    }

    public void selectYamDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "YamDatabase");

        editor.apply();
    }
}

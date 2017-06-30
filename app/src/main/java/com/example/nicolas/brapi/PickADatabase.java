package com.example.nicolas.brapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;

import java.net.URL;


public class PickADatabase extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_adatabase);
    }

    
    public void selectCassavaDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "https://cassavabase.org/");

        editor.apply();

        Intent GetCrop = new Intent(this, PickACrop.class);
        startActivity(GetCrop);
    }

    public void selectSweetPotatoDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "https://sweetpotatobase.org/");

        editor.apply();

        Intent GetCrop = new Intent(this, PickACrop.class);
        startActivity(GetCrop);
    }

    public void selectMusaDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "https://musabase.org/");

        editor.apply();

        Intent GetCrop = new Intent(this, PickACrop.class);
        startActivity(GetCrop);
    }

    public void selectYamDatabase(View view)
    {
        SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
        editor.putString("SelectedDatabase", "https://yambase.org/");

        editor.apply();

        Intent GetCrop = new Intent(this, PickACrop.class);
        startActivity(GetCrop);
    }


}

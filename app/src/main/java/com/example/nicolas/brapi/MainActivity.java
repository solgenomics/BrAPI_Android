package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //on current database
        SharedPreferences prefs = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
        String CurrentSelectDatabase = prefs.getString("SelectedDatabase", "No Database Selected");


        TextView txt = (TextView) findViewById(R.id.CurrentDatabase);
        txt.setText("Current Database: " +CurrentSelectDatabase);

    }

    public void SelectedDatabase(View view)
    {
        Intent intent = new Intent(this, PickADatabase.class);
        startActivity(intent);
    }

    public void SelectedSearch(View view)
    {
        Intent intent = new Intent(this, MainSearchDatabase.class);
        startActivity(intent);
    }

}




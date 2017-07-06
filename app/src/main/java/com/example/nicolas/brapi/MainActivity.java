package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        txt.setText("Picked: " +CurrentSelectDatabase);


        //on current search
        SharedPreferences prefs2 = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE);
        String CurrentSelectSearch = prefs.getString("SelectedSearch", "No Database Searched");

        TextView txt2 = (TextView) findViewById(R.id.CurrentSearched);
        txt2.setText("Searched: " +CurrentSelectSearch);


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




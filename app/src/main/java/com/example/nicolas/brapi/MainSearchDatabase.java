package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import static android.R.attr.button;
import static android.R.attr.id;
import static android.content.ContentValues.TAG;

public class MainSearchDatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    public static final String CurrentDataCall = "com.example.nicolas.brapi";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_database);

        //Database Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner6);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Database_List_URL, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Category Spinner
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Category_List_URL, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        final String StringSpinner = spinner.getSelectedItem().toString();
        final String StringSpinner2 = spinner2.getSelectedItem().toString();

        //Identifying button
        Button button = (Button) findViewById(R.id.SearchId);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                editor.putString("SelectedSearch", StringSpinner + StringSpinner2);
                editor.apply();

            }
        });
        Intent GetCrop = new Intent(this, CallToURL.class);
        GetCrop.putExtra(CurrentDataCall, StringSpinner2);
        startActivity(GetCrop);
    }

    //Action when something is selected
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
    }

    //Action when nothing is selected
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
}

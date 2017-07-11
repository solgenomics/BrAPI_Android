package com.example.nicolas.brapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import static android.content.ContentValues.TAG;

public class SearchADatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    Spinner spinner, spinner2;

    Button bSearch;
    public static final String CurrentDataCall = "com.example.nicolas.brapi";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_adatabase);

        //Database Spinner
        spinner = (Spinner) findViewById(R.id.spinner6);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Database_List_URL, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Category Spinner
        spinner2 = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Category_List_URL, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        bSearch = (Button) findViewById(R.id.SearchId);
        bSearch.setOnClickListener(this);


    }

    //Action when something is selected
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
    }

    //Action when nothing is selected
    public void onNothingSelected(AdapterView<?> parent)
    {
    }
    @Override
    public void onClick(View view) {
        String StringData = spinner.getSelectedItem().toString();
        String StringRestofData = spinner2.getSelectedItem().toString();

        Intent GetCrop = new Intent(this, WebViewActivity.class);
        GetCrop.putExtra(StringData, StringRestofData);


        Log.d(TAG, StringData + StringRestofData);
        startActivity(GetCrop);
    }
}

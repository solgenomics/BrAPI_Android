package com.example.nicolas.brapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainSearchDatabase extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search_database);

        //Database Spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner6);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Database_List, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Category Spinner

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.Category_List, android.R.layout.simple_dropdown_item_1line);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

    }


    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String sSelected = parent.getItemAtPosition(pos).toString();
        Toast.makeText(this,sSelected,Toast.LENGTH_SHORT).show();

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }


}

package com.example.nicolas.brapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static android.content.ContentValues.TAG;


public class AddADatabase extends AppCompatActivity implements View.OnClickListener
{
    public static final String CurrentCreatedData = "PostToBrapiStrings";
    public static final String CurrentCreatedURL = "PostToBrapiStrings";
    Button bAddData;
    EditText DatabaseName, URLFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adatabase);

        bAddData = (Button) findViewById(R.id.bCreateDatabase);
    }

    @Override
    public void onClick(View view)
    {
        DatabaseName = (EditText) findViewById(R.id.DatabaseName);
        URLFormat = (EditText) findViewById(R.id.URLFormat);

        String NewDatabaseName = DatabaseName.getText().toString();
        String NewURLFormat = URLFormat.getText().toString();

        Log.d(TAG, NewDatabaseName);
        Log.d(TAG, NewURLFormat);

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(CurrentCreatedData, NewDatabaseName);
        intent.putExtra(CurrentCreatedURL, NewURLFormat);
        startActivity(intent);
    }

}

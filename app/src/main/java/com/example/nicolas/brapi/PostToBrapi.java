package com.example.nicolas.brapi;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

public class PostToBrapi extends AppCompatActivity {

    ProgressBar circle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_brapi);

        Intent intent = getIntent();

        String CurrentCreateData = intent.getStringExtra(AddADatabase.CurrentCreatedData);
        String CurrentCreateURL = intent.getStringExtra(AddADatabase.CurrentCreatedURL);

        CallToURL.MyTaskParams params = new CallToURL.MyTaskParams(CurrentCreateData, CurrentCreateURL);


        // Connectivity Verification -----------------------------
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo!=null && nInfo.isConnected())
        {}
        else
        {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
        //---------------------------------------------------------

        circle = (ProgressBar) findViewById(R.id.progressBar);









    }


}

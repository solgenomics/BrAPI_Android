package com.example.nicolas.brapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity implements View.OnClickListener {

    public static final String FirstName = "PostToBrapiFirstName";
    public static final String LastName = "PostTOBrapiLastName";
    public static final String UserName = "PostToBrapiUser";
    public static final String Password = "PostToBrapiPass";
    public static final String Organization = "PostToBrapiOrganization";
    public static final String Email = "PostToBrapiEmail";

    Button bRegister;

    EditText etFirstName, etLastName, etOrginization, etCreateUsername, etCreatePassword, etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etOrginization= (EditText) findViewById(R.id.etOrginization);
        etCreateUsername = (EditText) findViewById(R.id.etCreateUsername);
        etCreatePassword = (EditText) findViewById(R.id.etCreatePassword);
        etEmail = (EditText) findViewById(R.id.etEmail);

        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String StringFirstName = etFirstName.toString();
        String StringLastName = etLastName.toString();
        String StringOrganization = etOrginization.toString();
        String StringUsername = etCreateUsername.toString();
        String StringPassword = etCreatePassword.toString();
        String StringEmail = etEmail.toString();

        switch (view.getId())
        {
            case R.id.bRegister:
                Intent intent = new Intent(this, WebViewRegister.class);

                intent.putExtra(FirstName, StringFirstName);
                intent.putExtra(LastName, StringLastName);
                intent.putExtra(Organization, StringOrganization);
                intent.putExtra(UserName, StringUsername);
                intent.putExtra(Password, StringPassword);
                intent.putExtra(Email, StringEmail);


                startActivity(intent);
                break;
        }
    }
}

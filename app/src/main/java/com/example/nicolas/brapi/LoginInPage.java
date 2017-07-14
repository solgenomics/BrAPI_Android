package com.example.nicolas.brapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginInPage extends AppCompatActivity implements View.OnClickListener{

    Button bLogin;
    EditText etUserName, etPassword;
    TextView tvRegister;

    public static final String Username = "PostToBrapiUserInput";
    public static final String Password = "PostToBrapiPassInput";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in_page);

        bLogin = (Button) findViewById(R.id.bLogin);
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);

        bLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        String getUserName = etUserName.toString();
        String getPassword = etPassword.toString();

        switch (view.getId())
        {
            case R.id.bLogin:
                Intent intent = new Intent(this, POSTToLogin.class);

                intent.putExtra(Username, getUserName);
                intent.putExtra(Password, getPassword);

                startActivity(intent);
                break;
            case R.id.tvRegister:
                Intent intent1 = new Intent(this, Register.class);
                startActivity(intent1);
                break;
        }
    }
}

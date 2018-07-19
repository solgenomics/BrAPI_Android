package com.example.nicolas.brapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import static android.content.ContentValues.TAG;

import javax.net.ssl.HttpsURLConnection;


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

        CreateLogin login = new CreateLogin();
        login.execute();
    }

    public class CreateLogin extends AsyncTask<String, Void, String>
    {

        String StringFirstName = etFirstName.getText().toString();
        String StringLastName = etLastName.getText().toString();
        String StringOrganization = etOrginization.getText().toString();
        String StringUsername = etCreateUsername.getText().toString();
        String StringPassword = etCreatePassword.getText().toString();
        String StringEmail = etEmail.getText().toString();

        @Override
        protected String doInBackground(String... strings)
        {

            try
            {
                URL url = new URL("https://cassavabase.org/brapiapp/register");
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();
                httpURL.setRequestMethod("POST");
                String data = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(StringFirstName, "UTF-8");
                data += "&" + URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(StringLastName, "UTF-8");
                data += "&" + URLEncoder.encode("organization", "UTF-8") + "=" + URLEncoder.encode(StringOrganization, "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(StringUsername, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(StringPassword, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(StringEmail, "UTF-8");


                httpURL.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(httpURL.getOutputStream());
                wr.write(data);
                wr.flush();

                try
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURL.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();

                }finally {
                    httpURL.disconnect();
                }

            } catch (Exception e) {
                return null;
            }

        }
        @Override
        protected void onPostExecute(String response)
        {



            if (response.equals("{\"error\":\"User Not Logged In\"}"))
            {
                Toast.makeText(Register.this, "Username TAKEN!", Toast.LENGTH_LONG).show();

            }
            else {

                Intent intent = new Intent(getApplicationContext(), LoginInPage.class);
                Toast.makeText(Register.this, "You are REGISTERED!", Toast.LENGTH_LONG).show();

                startActivity(intent);
            }
        }
    }

    //Async class here
    public class retreiveDatabaselist extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... voids)
        {

            try
            {
                URL url = new URL("https://cassavabase.org/brapiapp/list_databases");
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();

                try
                {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURL.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while((line = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();

                }finally {
                    httpURL.disconnect();
                }

            } catch (Exception e) {
                return null;
            }

        }


        @Override
        protected void onPostExecute(String response){


        }
    }

}

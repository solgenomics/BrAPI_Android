package com.example.nicolas.brapi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import static android.content.ContentValues.TAG;


public class LoginInPage extends AppCompatActivity implements View.OnClickListener{

    Button bLogin;
    EditText etUserName, etPassword;
    TextView tvRegister;
    String accessToken;

    private String username,password;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

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

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            etUserName.setText(loginPreferences.getString("username", ""));
            etPassword.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        // Connectivity Verification -----------------------------
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo!=null && nInfo.isConnected())
        {}
        else
        {
            Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);

        username = etUserName.getText().toString();
        password = etPassword.getText().toString();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        String getUserName = etUserName.getText().toString();
        String getPassword = etPassword.getText().toString();

        switch (view.getId())
        {
            case R.id.bLogin:
                MyTaskParams params = new MyTaskParams(getUserName, getPassword);
                CallToDatabase myTask = new CallToDatabase();
                myTask.execute(params);
                break;

            case R.id.tvRegister:
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
                break;
        }

    }

    public static class MyTaskParams
    {
        String UserName;
        String PassWord;

        MyTaskParams(String UserName, String PassWord)
        {
            this.UserName = UserName;
            this.PassWord = PassWord;
        }

    }

    private class CallToDatabase extends AsyncTask<MyTaskParams, Void, String> {

        @Override
        protected String doInBackground(MyTaskParams... params) {
            String CurrentUserName = params[0].UserName;
            String CurrentPassWord = params[0].PassWord;

            try {
                URL url = new URL("https://test.brapi.org/brapi/v1/token");
                HttpsURLConnection httpURL = (HttpsURLConnection) url.openConnection();

                httpURL.setRequestMethod("POST");
                //httpURL.setRequestProperty("grant_type", "password");
                //httpURL.setRequestProperty("username", CurrentUserName);
                //httpURL.setRequestProperty("password", CurrentPassWord);
                String data = URLEncoder.encode("grant_type", "UTF-8") + "=" + URLEncoder.encode("password", "UTF-8");
                data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(CurrentUserName, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(CurrentPassWord, "UTF-8");

                httpURL.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(httpURL.getOutputStream());
                wr.write(data);
                wr.flush();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURL.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();

                } finally {
                    httpURL.disconnect();
                }

            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            JSONObject Resp;
            JSONObject metadata;
            String userDisplayName;
            accessToken = null;
            String expiresIn;
            try {
                Resp = new JSONObject(response);
                metadata = Resp.getJSONObject("metadata");
                userDisplayName = Resp.getString("userDisplayName");
                accessToken = Resp.getString("access_token");
                expiresIn = Resp.getString("expires_in");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (accessToken.equals("null")) {
                Intent LoginPage = new Intent(getApplicationContext(), LoginInPage.class);
                Toast.makeText(LoginInPage.this, "Try Again!", Toast.LENGTH_SHORT).show();

                startActivity(LoginPage);
            } else {
                SharedPreferences.Editor editor = getSharedPreferences("Variables.BrAPI", MODE_PRIVATE).edit();
                editor.putString("accessToken", accessToken);
                editor.apply();

                Intent Entered = new Intent(getApplicationContext(), PickADatabase.class);
                Toast.makeText(LoginInPage.this, "Welcome!", Toast.LENGTH_SHORT).show();
                startActivity(Entered);

            }

        }
    }
}

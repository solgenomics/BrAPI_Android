package com.example.nicolas.brapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class WebViewActivity extends AppCompatActivity {

    ProgressBar progressBar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        String CurrentCreatedURL = AddADatabase.CurrentCreatedURL.toString();
        String CurrentCreatedDatabase = AddADatabase.CurrentCreatedData.toString();

        setContentView(webView);

        webView.loadUrl(CurrentCreatedURL);



    }
}

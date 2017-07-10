package com.example.nicolas.brapi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class WebViewActivity extends AppCompatActivity {

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


        shouldOverrideUrlLoading(webView,CurrentCreatedURL);


    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String CurrentCreatedURL = AddADatabase.CurrentCreatedURL.toString();
        String CurrentCreatedDatabase = AddADatabase.CurrentCreatedData.toString();

        if (Uri.parse(url).getHost().equals(CurrentCreatedURL)) {
            // This is my web site, so do not override; let my WebView load the page
            return false;
        }
        // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
        return true;
    }
}
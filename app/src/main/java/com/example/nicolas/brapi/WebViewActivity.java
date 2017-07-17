package com.example.nicolas.brapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import static android.content.ContentValues.TAG;


public class WebViewActivity extends AppCompatActivity {

    ProgressBar progressBar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        Intent intent = getIntent();
        String CurrentCreateURL = intent.getStringExtra(AddADatabase.CurrentCreatedURL);
        String CurrentCreateData = intent.getStringExtra(AddADatabase.CurrentCreatedData);

        webView = (WebView) findViewById(R.id.webView);
        WebViewClient webViewClient= new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        };

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("https://test.brapi.org/brapiapp/new_database?databaseName=" + CurrentCreateData + "&databaseURL=" + CurrentCreateURL);
        webView.addJavascriptInterface(new  MyJavaScriptInterface(this), "HtmlViewer");

        Intent crop = new Intent(this, PickADatabase.class);
        startActivity(crop);
    }
}

class MyJavaScriptInterface{

    private Context ctx;
    MyJavaScriptInterface(Context ctx) {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void showHTML(String html) {
        System.out.println(html);

        Log.d(TAG, html);
        String end1 = "Database CREATED";
        String end2 = "Database EXISTS";


        int duration = Toast.LENGTH_LONG;

        if(html.equals("<html><head></head><body>{\"error\":\"The given database name already exists and cannot be added!\"}</body></html>"))
        {
            Toast toast = Toast.makeText(ctx, end2, duration);
            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText(ctx, end1, duration);
            toast.show();
        }

    }
}
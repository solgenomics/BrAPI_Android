package com.example.nicolas.brapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class WebViewLogin extends AppCompatActivity
{

    String UserName, Password;
    WebView webView;
    private Object SocketTimeoutException;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_login);

        Intent intent = getIntent();
        UserName = intent.getStringExtra(Register.UserName);
        Password = intent.getStringExtra(Register.Password);

        webView = (WebView) findViewById(R.id.webViewLogin);
        WebViewClient webViewClient= new WebViewClient()
        {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                webView.loadUrl("javascript:HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        };

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(webViewClient);

        webView.addJavascriptInterface(new  MyJavaScriptInterface3(this), "HtmlViewer");
        Intent crop = new Intent(this, PickADatabase.class);
        startActivity(crop);
    }
}

class MyJavaScriptInterface3
{
    Intent intent = new Intent();
    String UserName = intent.getStringExtra(Register.UserName);

    private Context ctx;

    MyJavaScriptInterface3(Context ctx)
    {
        this.ctx = ctx;
    }

    @JavascriptInterface
    public void showHTML(String html)
    {
        System.out.println(html);

        Log.d(TAG, html);
        String end1 = "Welcome " + UserName + "!";
        String end2 = "Try again";

        int duration = Toast.LENGTH_LONG;

        if (html.equals("<html><head></head><body>{\"error\":\"The given database name already exists and cannot be added!\"}</body></html>"))
        {
            Toast toast = Toast.makeText(ctx, end2, duration);
            toast.show();
        } else {
            Toast toast = Toast.makeText(ctx, end1, duration);
            toast.show();

        }
    }
}


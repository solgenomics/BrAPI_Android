package com.example.nicolas.brapi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class WebViewRegister extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_register);

        Intent intent = getIntent();
        String Firstname = intent.getStringExtra(Register.FirstName);
        String LastName = intent.getStringExtra(Register.LastName);
        String Organization = intent.getStringExtra(Register.Organization);
        String UserName = intent.getStringExtra(Register.UserName);
        String Password = intent.getStringExtra(Register.Password);
        String Email = intent.getStringExtra(Register.Email);

        webView = (WebView) findViewById(R.id.webViewRegister);
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
        webView.loadUrl("https://test.brapi.org/brapiapp/register?"
                + "firstName=" + Firstname
                + "&lastName=" + LastName
                + "&organization" + Organization
                + "&userName" + UserName
                + "&password" + Password
                + "&email" + Email);

        webView.addJavascriptInterface(new  MyJavaScriptInterface(this), "HtmlViewer");

        Intent crop = new Intent(this, PickADatabase.class);
        startActivity(crop);
    }
}
class MyJavaScriptInterface2{

    private Context ctx;
    MyJavaScriptInterface2(Context ctx) {
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
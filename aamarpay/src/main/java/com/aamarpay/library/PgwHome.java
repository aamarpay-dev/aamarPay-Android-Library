package com.aamarpay.library;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PgwHome extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgw_home);

        String url = getIntent().getStringExtra("URL");
        WebView webView = findViewById(R.id.pgw_web);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new myWebClient());
        webView.loadUrl(url);
    }

    public static class myWebClient extends WebChromeClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            // TODO Auto-generated method stub
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            // TODO Auto-generated method stub
//            Log.d("TEST_URL", url);
//            view.loadUrl(url);
//            return true;
//
//        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    private static class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("TEST_URL", url);
            view.loadUrl(url);
            return true;
        }
    }
}
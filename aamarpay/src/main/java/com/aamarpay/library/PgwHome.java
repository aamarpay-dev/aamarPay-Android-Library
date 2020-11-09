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

import java.lang.reflect.Method;

import im.delight.android.webview.AdvancedWebView;

public class PgwHome extends AppCompatActivity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;
    private AamarPay aamarPay;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgw_home);

        aamarPay = new AamarPay(PgwHome.this, "", "");

        String payment_url = getIntent().getStringExtra("URL");

        mWebView = (AdvancedWebView) findViewById(R.id.pgwHome);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);
        mWebView.loadUrl(payment_url);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if (url.contains("payment-success")) {

        } else if (url.contains("payment-fail")) {

        } else if (url.contains("payment-cancel")) {
            try {
                Method m = AamarPay.class.getDeclaredMethod("onCancelListener");
                m.setAccessible(true);
                m.invoke(aamarPay);
            } catch (Exception e) {
                // Do Nothing
            }
            finish();
        }
    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}
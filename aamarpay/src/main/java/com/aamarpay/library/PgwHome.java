package com.aamarpay.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class PgwHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgw_home);

        String url = getIntent().getStringExtra("URL");
        WebView webView = findViewById(R.id.pgw_web);
        webView.loadUrl(url);
    }
}
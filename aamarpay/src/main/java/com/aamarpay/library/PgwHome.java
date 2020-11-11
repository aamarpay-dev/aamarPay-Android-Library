package com.aamarpay.library;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import im.delight.android.webview.AdvancedWebView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aamarpay.library.AamarPay.listener;

public class PgwHome extends AppCompatActivity implements AdvancedWebView.Listener {

    private View pgw_loading;
    private String store_id, signature_key, trxID;
    private boolean isTestMode, paymentSuccess = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgw_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.pgwStatus));
        }

        pgw_loading = findViewById(R.id.pgw_loading);

        String payment_url = getIntent().getStringExtra("URL");
        trxID = getIntent().getStringExtra("TRX_ID");
        store_id = getIntent().getStringExtra("STORE_ID");
        signature_key = getIntent().getStringExtra("SIGNATURE_KEY");
        isTestMode = getIntent().getBooleanExtra("TEST_MODE", true);

        AdvancedWebView mWebView = (AdvancedWebView) findViewById(R.id.pgwHome);
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);
        mWebView.loadUrl(payment_url);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        pgw_loading.setVisibility(View.VISIBLE);
        if (url.contains("payment-success")) {
            paymentSuccess = true;
            AsyncTrxVerification trxVerification = new AsyncTrxVerification();
            trxVerification.execute();
        } else if (url.contains("payment-fail")) {
            paymentSuccess = false;
            AsyncTrxVerification trxVerification = new AsyncTrxVerification();
            trxVerification.execute();
        } else if (url.contains("payment-cancel")) {
            listener.onPaymentCancel(false, "Payment cancelled by user.");
            finish();
        }
    }

    @Override
    public void onPageFinished(String url) {
        pgw_loading.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        // Disabled
    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncTrxVerification extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgw_loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            getTrxData();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pgw_loading.setVisibility(View.GONE);
            finish();
        }
    }

    public void getTrxData() {
        OkHttpClient client = new OkHttpClient();
        String url = "";
        if (isTestMode) {
            url = "https://sandbox.aamarpay.com/api/v1/trxcheck/request.php?request_id=" + trxID + "&store_id=" + store_id + "&signature_key=" + signature_key + "&type=json";
        } else {
            url = "https://secure.aamarpay.com/api/v1/trxcheck/request.php?request_id=" + trxID + "&store_id=" + store_id + "&signature_key=" + signature_key + "&type=json";
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        final String myResponse = response.body().string();
                        PgwHome.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = new JSONObject(myResponse);
                                    if (paymentSuccess) {
                                        listener.onPaymentSuccess(jsonObject);
                                    } else {
                                        listener.onPaymentFailure(jsonObject);
                                    }
                                } catch (JSONException e) {
                                    listener.onPaymentProcessingFailed(true, "Payment processing failed due to transaction verification failure.");
                                }
                            }
                        });
                    } catch (Exception e) {
                        listener.onPaymentProcessingFailed(true, "Payment processing failed due to transaction verification failure.");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                listener.onPaymentProcessingFailed(true, "Payment processing failed due to transaction verification failure.");
            }
        });
    }
}
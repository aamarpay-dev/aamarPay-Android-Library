package com.aamarpay.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aamarpay.library.AamarPay;
import com.aamarpay.library.PgwHome;
import com.aamarpay.library.Retrofit.LiveClient;
import com.aamarpay.payment.Retrofit.SandboxClient;
import com.airbnb.lottie.L;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import im.delight.android.webview.AdvancedWebView;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;

    OkHttpClient client = new OkHttpClient();

    private AamarPay aamarPay;

    private AlertDialog alertDialog;
    private String trxID, trxAmount, trxCurrency, customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry, paymentDescription;
    EditText trx_id, trx_amount, trx_currency, customer_name, customer_email, customer_phone, customer_address, customer_city, customer_country, payment_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Reference to fields
        trx_id = findViewById(R.id.trx_id);
        trx_amount = findViewById(R.id.trx_amount);
        trx_currency = findViewById(R.id.trx_currency);
        customer_name = findViewById(R.id.customer_name);
        customer_email = findViewById(R.id.customer_email);
        customer_phone = findViewById(R.id.customer_phone);
        customer_address = findViewById(R.id.customer_address);
        customer_city = findViewById(R.id.customer_city);
        customer_country = findViewById(R.id.customer_country);
        payment_description = findViewById(R.id.payment_description);

        // Initiate payment
        aamarPay = new AamarPay(MainActivity.this, "aamarpay", "28c78bb1f45112f5d40b956fe104645a");
        aamarPay.testMode(true);
        aamarPay.autoGenerateTransactionID(true);

        // Generate unique transaction id
        trxID = aamarPay.generate_trx_id();
        // Setting the values to fields
        trx_id.setText(trxID);

        // Get the data
        trxAmount = trx_amount.getText().toString();
        trxCurrency = trx_currency.getText().toString();
        customerName = customer_name.getText().toString();
        customerEmail = customer_email.getText().toString();
        customerPhone = customer_phone.getText().toString();
        customerAddress = customer_address.getText().toString();
        customerCity = customer_city.getText().toString();
        customerCountry = customer_country.getText().toString();
        paymentDescription = payment_description.getText().toString();

        aamarPay.setTransactionParameter(trxAmount, trxCurrency, paymentDescription);
        aamarPay.setCustomerDetails(customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry);

        aamarPay.initPGW(new AamarPay.onInitListener() {
            @Override
            public void onInitFailure(Boolean error, String message) {
                Log.d("TEST_", message);
            }

            @Override
            public void onPaymentSuccess(JSONObject jsonObject) {
                Log.d("TEST_DER", jsonObject.toString());
            }

            @Override
            public void onPaymentFailure(JsonObject jsonObject) {
                Log.d("TEST_DER", jsonObject.toString());
            }

            @Override
            public void onPaymentProcessingFailed(Boolean error, String message) {
                Log.d("TEST_DER", message);
            }

            @Override
            public void onPaymentCancel(Boolean error, String message) {
                Log.d("TEST_DER", message);
//                okGTest();
            }
        });

//        showLoading();
        // Swipe refresh layout actions
        swipeRefresh();

//        AamarPay aamarPay = new AamarPay(MainActivity.this);
//        aamarPay.initPGW(new AamarPay.onInitListener() {
//            @Override
//            public void onSuccess(JsonObject jsonObject) {
//                Log.d("TEST_D", jsonObject.toString());
//            }
//
//            @Override
//            public void onFailure(JsonObject jsonObject) {
//
//            }
//        });

//        AamarPay.init_pgw(MainActivity.this, "SANDBOX");
//        Call<ResponseBody> call = SandboxClient
//                .getInstance()
//                .getApi()
//                .init_payment(ApiJsonMap(trxID, trxAmount, trxCurrency, customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry, paymentDescription));
//
//        call.enqueue(new retrofit2.Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
//                String resp = null;
//                try {
//                    if (response.body() != null) {
//                        resp = response.body().string();
//                        try {
//                            final JSONObject jsonObject = new JSONObject(resp);
//                            String payment_url = jsonObject.getString("payment_url");
//                            Log.d("TEST_SDK", jsonObject.getString("payment_url"));
//                            alertDialog.dismiss();
//                            mWebView = (AdvancedWebView) findViewById(R.id.advancedWebview);
//                            mWebView.setVisibility(View.VISIBLE);
//                            mWebView.setListener(MainActivity.this, MainActivity.this);
//                            mWebView.setMixedContentAllowed(false);
//                            mWebView.loadUrl(payment_url);
//                        } catch (JSONException e) {
//                            errorPopUp("Error: " + e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (IOException e) {
//                    errorPopUp("Error: " + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
//                errorPopUp("Error: " + t.getMessage());
//            }
//        });
    }

    private void okGTest() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://secure.aamarpay.com/api/v1/trxcheck/request.php?request_id=WEP-SMZZ4ZM8EC&store_id=aamarpay&signature_key=28c78bb1f45112f5d40b956fe104645e&type=json";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(okhttp3.@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("TEST_DER", myResponse);
                        }
                    });
                }
            }

            @Override
            public void onFailure(okhttp3.@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private JsonObject ApiJsonMap(String trxID, String trxAmount, String trxCurrency, String customerName, String customerEmail, String customerPhone, String customerAddress, String customerCity, String customerCountry, String paymentDescription) {

        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("store_id", "aamarpay");
            jsonObj_.put("signature_key", "28c78bb1f45112f5d40b956fe104645a");
            jsonObj_.put("cus_name", customerName);
            jsonObj_.put("cus_email", customerEmail);
            jsonObj_.put("cus_phone", customerPhone);
            jsonObj_.put("cus_add1", customerAddress);
            jsonObj_.put("cus_city", customerCity);
            jsonObj_.put("cus_country", customerCountry);
            jsonObj_.put("amount", trxAmount);
            jsonObj_.put("tran_id", trxID);
            jsonObj_.put("currency", trxCurrency);
            jsonObj_.put("success_url", "library-success");
            jsonObj_.put("fail_url", "This is fail URL");
            jsonObj_.put("cancel_url", "This is cancel URL");
            jsonObj_.put("desc", paymentDescription);
            jsonObj_.put("type", "json");


            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    // Swipe refresh layout actions
    private void swipeRefresh() {
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Regenerate unique transaction id
                trxID = aamarPay.generate_trx_id();

                // Setting the values to fields
                trx_id.setText(trxID);

                // Set refreshing false
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @SuppressLint("InflateParams")
    public void errorPopUp(String retryMsg) {
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        // Initialize a new window manager layout parameters
        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.7f);
        // Set alert dialog height equal to screen height 70%
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view = LayoutInflater.from(this).inflate(R.layout.error_popup, null);
        } else {
            view = LayoutInflater.from(this).inflate(R.layout.error_popup_old, null);
        }
        TextView retryTxt = view.findViewById(R.id.retry_btn);
        TextView retryMessage = view.findViewById(R.id.custom_message);
        retryMessage.setText(retryMsg);
        retryTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = dialogWindowWidth;
            alertDialog.getWindow().setAttributes(layoutParams);
        }
    }

    @SuppressLint("InflateParams")
    public void showLoading() {
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        // Initialize a new window manager layout parameters
        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.4f);
        // Set alert dialog height equal to screen height 70%
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view = LayoutInflater.from(this).inflate(R.layout.loading_popup, null);
        } else {
            view = LayoutInflater.from(this).inflate(R.layout.loading_popup_old, null);
        }
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            layoutParams.copyFrom(alertDialog.getWindow().getAttributes());
            layoutParams.width = dialogWindowWidth;
            alertDialog.getWindow().setAttributes(layoutParams);
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        if (url.contains("library-success")) {
            Log.d("TEST__", url);
            mWebView.setVisibility(View.GONE);
        }
//        Log.d("TEST__", url);
        showLoading();
    }

    @Override
    public void onPageFinished(String url) {
        alertDialog.dismiss();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {
        Log.d("TEST___URL", url);
    }
}
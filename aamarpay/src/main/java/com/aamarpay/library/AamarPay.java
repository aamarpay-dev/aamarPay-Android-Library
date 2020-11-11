package com.aamarpay.library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aamarpay.library.Retrofit.LiveClient;
import com.aamarpay.library.Retrofit.SandboxClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AamarPay {
    public static final String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();
    private Context context;
    private String store_id, signature_key, trxID, trxAmount, trxCurrency, customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry, paymentDescription;
    private boolean isPaymentInfoSet, isCustomerInfoSet, autoGenerateTrx, isTestMode;

    public interface onInitListener {
        void onInitFailure(Boolean error, String message);

        void onPaymentSuccess(JSONObject jsonObject);

        void onPaymentFailure(JSONObject jsonObject);

        void onPaymentProcessingFailed(Boolean error, String message);

        void onPaymentCancel(Boolean error, String message);
    }

    public static onInitListener listener;

    public AamarPay(Context context, String store_id, String signature_key) {
        // Set the context
        this.context = context;

        // Setting store id and signature key
        this.store_id = store_id;
        this.signature_key = signature_key;

        // set null or default listener or accept as argument to constructor
        listener = null;

        // Set default false
        this.isCustomerInfoSet = false;
        this.isPaymentInfoSet = false;
        this.autoGenerateTrx = false;
        this.isTestMode = true;

        // Init variables
        this.trxAmount = "";
        this.trxCurrency = "";
        this.paymentDescription = "";
        this.customerName = "";
        this.customerEmail = "";
        this.customerAddress = "";
        this.customerPhone = "";
        this.customerCity = "";
        this.customerCountry = "";
    }

    // Set if the library generate the transaction id itself
    public void autoGenerateTransactionID(Boolean generate) {
        this.autoGenerateTrx = generate;
    }

    // Set custom trx ID
    public void setTransactionID(String transaction_id) {
        trxID = transaction_id;
    }

    // Set the trx parameter
    public void setTransactionParameter(String transaction_amount, String transaction_currency, String payment_description) {
        this.trxAmount = transaction_amount;
        this.trxCurrency = transaction_currency;
        this.paymentDescription = payment_description;
        this.isPaymentInfoSet = true;
    }

    // Set customer details
    public void setCustomerDetails(String customer_name, String customer_email, String customer_phone, String customer_address, String customer_city, String customer_country) {
        this.customerName = customer_name;
        this.customerEmail = customer_email;
        this.customerAddress = customer_address;
        this.customerPhone = customer_phone;
        this.customerCity = customer_city;
        this.customerCountry = customer_country;
        this.isCustomerInfoSet = true;
    }

    // Sandbox/Live Switch
    public void testMode(Boolean mode) {
        this.isTestMode = mode;
    }

    public void initPGW(onInitListener listener) {
        AamarPay.listener = listener;
        if (isPaymentInfoSet && isCustomerInfoSet) {
            if (autoGenerateTrx) {
                trxID = generate_trx_id();
                initGateway();
            } else {
                if (trxID.equals("")) {
                    listener.onInitFailure(true, "You need to provide a transaction id. You can auto generate transaction id by setting autoGenerateTrx as true.");
                } else {
                    initGateway();
                }
            }
        } else {
            listener.onInitFailure(true, "Payment info or customer details are missing.");
        }
    }

    private void initGateway() {
        if (isTestMode) {
            // Sandbox
            Call<ResponseBody> call = SandboxClient
                    .getInstance()
                    .getApi()
                    .init_payment(createJSONMap());

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    String resp = null;
                    try {
                        if (response.body() != null) {
                            resp = response.body().string();
                            if (resp.equals("Invalid Store ID")) {
                                listener.onInitFailure(true, "Invalid Store ID");
                            } else {
                                try {
                                    final JSONObject jsonObject = new JSONObject(resp);
                                    String payment_url = jsonObject.getString("payment_url");
                                    Intent intent = new Intent(context, PgwHome.class);
                                    intent.putExtra("URL", payment_url);
                                    intent.putExtra("TEST_MODE", isTestMode);
                                    intent.putExtra("TRX_ID", trxID);
                                    intent.putExtra("STORE_ID", store_id);
                                    intent.putExtra("SIGNATURE_KEY", signature_key);
                                    ((Activity) context).startActivityForResult(intent, 1000);
                                } catch (JSONException e) {
                                    listener.onInitFailure(true, e.getMessage());
                                }
                            }
                        }
                    } catch (IOException e) {
                        listener.onInitFailure(true, e.getMessage());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    listener.onInitFailure(true, t.getMessage());
                }
            });
        } else {
            // Live
            Call<ResponseBody> call = LiveClient
                    .getInstance()
                    .getApi()
                    .init_payment(createJSONMap());

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    String resp = null;
                    try {
                        if (response.body() != null) {
                            resp = response.body().string();
                            try {
                                final JSONObject jsonObject = new JSONObject(resp);
                                String payment_url = jsonObject.getString("payment_url");
                                Intent intent = new Intent(context, PgwHome.class);
                                intent.putExtra("URL", payment_url);
                                intent.putExtra("TEST_MODE", isTestMode);
                                intent.putExtra("TRX_ID", trxID);
                                intent.putExtra("STORE_ID", store_id);
                                intent.putExtra("SIGNATURE_KEY", signature_key);
                                ((Activity) context).startActivityForResult(intent, 1000);
                            } catch (JSONException e) {
                                listener.onInitFailure(true, e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    } catch (IOException e) {
                        listener.onInitFailure(true, e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    listener.onInitFailure(true, t.getMessage());
                }
            });
        }
    }

    private JsonObject createJSONMap() {
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("store_id", this.store_id);
            jsonObj_.put("signature_key", this.signature_key);
            jsonObj_.put("cus_name", this.customerName);
            jsonObj_.put("cus_email", this.customerEmail);
            jsonObj_.put("cus_phone", this.customerPhone);
            jsonObj_.put("cus_add1", this.customerAddress);
            jsonObj_.put("cus_city", this.customerCity);
            jsonObj_.put("cus_country", this.customerCountry);
            jsonObj_.put("amount", this.trxAmount);
            jsonObj_.put("tran_id", trxID);
            jsonObj_.put("currency", this.trxCurrency);
            jsonObj_.put("success_url", "android-sdk/payment-success.html");
            jsonObj_.put("fail_url", "android-sdk/payment-fail.html");
            jsonObj_.put("cancel_url", "android-sdk/payment-cancel.html");
            jsonObj_.put("desc", this.paymentDescription);
            jsonObj_.put("type", "json");

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return gsonObject;
    }

    public String generate_trx_id() {
        return randomString() + System.currentTimeMillis();
    }

    private static String randomString() {
        StringBuilder sb = new StringBuilder(3);

        for (int i = 0; i < 3; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }

    @SuppressLint("InflateParams")
    public void showLoading(AlertDialog alertDialog) {
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        // Initialize a new window manager layout parameters
        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.4f);
        // Set alert dialog height equal to screen height 70%
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view = LayoutInflater.from(context).inflate(R.layout.loading_popup, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.loading_popup_old, null);
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

    @SuppressLint("InflateParams")
    public void errorPopUp(AlertDialog alertDialog, String retryMsg) {
        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        // Initialize a new window manager layout parameters
        // Set alert dialog width equal to screen width 70%
        int dialogWindowWidth = (int) (displayWidth * 0.7f);
        // Set alert dialog height equal to screen height 70%
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        View view;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view = LayoutInflater.from(context).inflate(R.layout.error_popup, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.error_popup_old, null);
        }
        TextView retryMessage = view.findViewById(R.id.custom_message);
        retryMessage.setText(retryMsg);
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
}

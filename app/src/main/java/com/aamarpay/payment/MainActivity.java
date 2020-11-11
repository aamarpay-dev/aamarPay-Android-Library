package com.aamarpay.payment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aamarpay.library.AamarPay;
import com.aamarpay.library.DialogBuilder;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private AamarPay aamarPay;
    private DialogBuilder dialogBuilder;
    private String trxID, trxAmount, trxCurrency, customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry, paymentDescription;
    EditText trx_id, trx_amount, trx_currency, customer_name, customer_email, customer_phone, customer_address, customer_city, customer_country, payment_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialogBuilder = new DialogBuilder(MainActivity.this, alertDialog);

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
//        aamarPay = new AamarPay(MainActivity.this, "khaidaitoday", "3cc65e1dd9fc945f99b2e117ead299f3");
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

        Button payNow = findViewById(R.id.payButton);

        trx_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                payNow.setText(String.format("Pay %s %s", trxCurrency.toUpperCase(), s.toString()));
            }
        });

        trx_currency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                payNow.setText(String.format("Pay %s %s", s.toString().toUpperCase(), trxAmount));
            }
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.showLoading();
                aamarPay.setTransactionParameter(trxAmount, trxCurrency, paymentDescription);
                aamarPay.setCustomerDetails(customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry);
                aamarPay.initPGW(new AamarPay.onInitListener() {
                    @Override
                    public void onInitFailure(Boolean error, String message) {
                        Log.d("TEST_IF", message);
                        dialogBuilder.dismissDialog();
                        dialogBuilder.errorPopUp(message);
                    }

                    @Override
                    public void onPaymentSuccess(JSONObject jsonObject) {
                        Log.d("TEST_PS", jsonObject.toString());
                        dialogBuilder.dismissDialog();
                    }

                    @Override
                    public void onPaymentFailure(JSONObject jsonObject) {
                        Log.d("TEST_PF", jsonObject.toString());
                        dialogBuilder.dismissDialog();
                    }

                    @Override
                    public void onPaymentProcessingFailed(JSONObject jsonObject) {
                        Log.d("TEST_PPF", jsonObject.toString());
                        dialogBuilder.dismissDialog();
                    }

                    @Override
                    public void onPaymentCancel(JSONObject jsonObject) {
                        Log.d("TEST_PPF", jsonObject.toString());
                        dialogBuilder.dismissDialog();
//                        aamarPay.getTransactionInfo("sdad");
                    }
                });
            }
        });

        // Swipe refresh layout actions
        swipeRefresh();
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
}
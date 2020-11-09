package com.aamarpay.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.aamarpay.library.AamarPay;

public class MainActivity extends AppCompatActivity {

    private String trxID, trxAmount, customerName, customerEmail, customerPhone, customerAddress, customerCity, customerCountry, paymentDescription;
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

        // Generate unique transaction id
        trxID = AamarPay.generate_trx_id();

        // Setting the values to fields
        trx_id.setText(trxID);

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
                trxID = AamarPay.generate_trx_id();

                // Setting the values to fields
                trx_id.setText(trxID);

                // Set refreshing false
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
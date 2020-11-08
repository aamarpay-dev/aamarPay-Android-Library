package com.aamarpay.library;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class aamarpay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aamarpay);
    }

    public void init() {
        Toast.makeText(aamarpay.this, "Hello World", Toast.LENGTH_LONG).show();
    }
}
package com.aamarpay.library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class aamarpay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aamarpay);
    }

    public static void init(Context context) {
        Toast.makeText(context, "Hello World", Toast.LENGTH_LONG).show();
    }
}
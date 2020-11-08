package com.aamarpay.library;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AamarPay {
    public static void init_pgw(Context context) {
        Intent intent = new Intent(context, PgwHome.class);
        intent.putExtra("URL", "https://google.com");
        context.startActivity(intent);
    }
}

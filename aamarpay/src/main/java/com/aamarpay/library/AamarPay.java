package com.aamarpay.library;

import android.content.Context;
import android.content.Intent;

import java.util.Random;

public class AamarPay {
    public static final String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();

    public static void init_pgw(Context context) {
        Intent intent = new Intent(context, PgwHome.class);
        intent.putExtra("URL", "https://google.com");
        context.startActivity(intent);
    }

    public static String generate_trx_id() {
        return randomString() + System.currentTimeMillis();
    }

    private static String randomString() {
        StringBuilder sb = new StringBuilder(3);

        for (int i = 0; i < 3; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }

        return sb.toString();
    }
}

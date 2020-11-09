package com.aamarpay.library;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.aamarpay.library.Retrofit.LiveClient;
import com.aamarpay.library.Retrofit.SandboxClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AamarPay {
    public static final String DATA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static Random RANDOM = new Random();
    private Context context;

    public interface onInitListener {
        public void onSuccess(JsonObject jsonObject);

        public void onFailure(JsonObject jsonObject);
    }

    private onInitListener listener;

    public AamarPay(Context ctx) {
        context = ctx;
        // set null or default listener or accept as argument to constructor
        this.listener = null;
    }

    public void initPGW(onInitListener listener){
        this.listener = listener;
        onSuccessListener();
    }

    public void onSuccessListener(){
        JsonObject gsonObject = new JsonObject();
        try {
            JSONObject jsonObj_ = new JSONObject();
            jsonObj_.put("error", false);
            jsonObj_.put("payment_url", "google.com");

            JsonParser jsonParser = new JsonParser();
            gsonObject = (JsonObject) jsonParser.parse(jsonObj_.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listener.onSuccess(gsonObject);
    }

    public void init_pgw(Context context, String type, String store_id, String signature_key) {
        if (type.toLowerCase().equals("live")) {
            Call<ResponseBody> call = LiveClient
                    .getInstance()
                    .getApi()
                    .init_payment("adsad", "json");

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("TEST_SDK", t.getMessage());
                }
            });
        } else if (type.toLowerCase().equals("sandbox")) {
            Call<ResponseBody> call = SandboxClient
                    .getInstance()
                    .getApi()
                    .init_payment("adad", "json");

            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("TEST_SDK", response.toString());
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("TEST_SDK", t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Other", Toast.LENGTH_SHORT).show();
        }
//        Intent intent = new Intent(context, PgwHome.class);
//        intent.putExtra("URL", "https://google.com");
//        context.startActivity(intent);
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

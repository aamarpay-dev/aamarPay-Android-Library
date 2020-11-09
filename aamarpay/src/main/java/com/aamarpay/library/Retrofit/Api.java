package com.aamarpay.library.Retrofit;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
    @Headers("Content-Type: application/json")
    @POST("jsonpost.php")
    Call<ResponseBody> init_payment(
            @Body JsonObject jsonBody
    );
}

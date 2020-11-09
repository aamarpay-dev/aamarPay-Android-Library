package com.aamarpay.payment.Retrofit;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {
    @Headers("Content-Type: application/json")
    @POST("jsonpost.php")
    Call<ResponseBody> init_payment(
            @Body JsonObject jsonBody
//            @Field("store_id") String store_id,
//            @Field("signature_key") String signature_key,
//            @Field("cus_name") String customer_name,
//            @Field("cus_email") String customer_email,
//            @Field("cus_phone") String customer_phone,
//            @Field("cus_add1") String customer_address,
//            @Field("cus_city") String customer_city,
//            @Field("cus_country") String customer_country,
//            @Field("amount") String amount,
//            @Field("tran_id") String transaction_id,
//            @Field("currency") String currency,
//            @Field("success_url") String success_url,
//            @Field("fail_url") String fail_url,
//            @Field("cancel_url") String cancel_url,
//            @Field("desc") String description,
//            @Field("type") String data_type
    );
}

package com.aamarpay.library.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("jsonpost.php")
    Call<ResponseBody> init_payment(
            @Field("store_id") String store_id
//            @Field("device_token") String device_token,
//            @Field("user_name") String user_name,
//            @Field("user_email") String user_email,
//            @Field("user_address") String user_address,
//            @Field("user_phone") String user_phone,
//            @Field("user_gender") String user_gender,
//            @Field("user_photo") String user_photo,
//            @Field("user_referral") String referral_code,
//            @Field("device_info") String device_info,
//            @Field("blood_group") String blood_group,
//            @Field("date_of_birth") String date_of_birth,
//            @Field("favorite_category") String favorite_category,
//            @Field("favorite_item") String favorite_item,
//            @Field("favorite_restaurant") String favorite_restaurant
    );
}

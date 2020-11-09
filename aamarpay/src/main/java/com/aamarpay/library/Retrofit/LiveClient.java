package com.aamarpay.library.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LiveClient {

    private static final String BASE_URL = "https://secure.aamarpay.com/";

    private static LiveClient mInstance;
    private Retrofit retrofit;

    private LiveClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized LiveClient getInstance(){
        if(mInstance == null){
            mInstance = new LiveClient();
        }
        return mInstance;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }
}

package com.aamarpay.library.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SandboxClient {

    private static final String BASE_URL = "https://sandbox.aamarpay.com/";

    private static SandboxClient mInstance;
    private Retrofit retrofit;

    private SandboxClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized SandboxClient getInstance(){
        if(mInstance == null){
            mInstance = new SandboxClient();
        }
        return mInstance;
    }

    public Api getApi(){
        return retrofit.create(Api.class);
    }
}

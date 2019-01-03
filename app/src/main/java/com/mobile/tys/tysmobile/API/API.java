package com.mobile.tys.tysmobile.API;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    public static final String BASE_URL =  "http://104.36.166.65/apitys/api/";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static Retrofit retrofit = null;

    public static Retrofit getApi(){
        Gson gson = new GsonBuilder()
                .setDateFormat(DATE_FORMAT)
                .create();
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }



}
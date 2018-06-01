package com.example.jinsu.nh_life.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private RetroService retroService;
    private static RetroClient instance = null;
    private final String BASE_URL = "http://172.17.22.237:8000/";

    private RetroClient()
    {
        getClient();
    }
    public static RetroClient getInstance()
    {
        if(instance == null)
        {
            instance = new RetroClient();

        }
        return instance;
    }

    public RetroService getRetroService() {
        return retroService;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void getClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroService.class);
    }





}

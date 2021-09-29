package com.bifos.kakao.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class KakaoClient {

    private final String baseUrl;

    public KakaoClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private Retrofit getRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 20, TimeUnit.SECONDS))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .create();

        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .build();
    }

    public MatchingRestful getClient() {
        return getRetrofit().create(MatchingRestful.class);
    }
}

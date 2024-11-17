package com.hk.transportProject.Retrofit_Intanse;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 날씨 정보 API 설정
public class WeatherRetrofitClient {
    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


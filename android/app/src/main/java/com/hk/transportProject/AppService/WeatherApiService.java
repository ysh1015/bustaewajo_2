package com.hk.transportProject.AppService;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("getUltraSrtNcst")
    Call<JsonObject> getWeatherInfo(
            @Query("serviceKey") String serviceKey,
            @Query("numOfRows") int numOfRows,
            @Query("pageNo") int pageNo,
            @Query("dataType") String dataType,
            @Query("base_date") String base_Date,
            @Query("base_time") String base_Time,
            @Query("nx") int nx,
            @Query("ny") int ny

    );
}


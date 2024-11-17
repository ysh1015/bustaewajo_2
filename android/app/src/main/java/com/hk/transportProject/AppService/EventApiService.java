package com.hk.transportProject.AppService;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EventApiService {
    @GET("api/15094713/v1/uddi:289f0a79-8aa8-494c-86e1-a14c01e3fe47")
    Call<JsonObject> getEventInfo(
            @Query("page") int page,
            @Query("perPage") int perPage,
            @Query("serviceKey") String servicekey
    );
}


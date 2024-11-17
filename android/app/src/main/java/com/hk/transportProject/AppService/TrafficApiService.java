package com.hk.transportProject.AppService;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hk.transportProject.response.TrafficApiResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TrafficApiService {
    @GET("1613000/BusSttnInfoInqireService/getCrdntPrxmtSttnList")
    Call<TrafficApiResponse> getTrafficInfo(
            @Query("serviceKey") String servicekey,
            @Query("pageNo") int pageNo,
            @Query("numOfRows") int numOfRows,
            @Query("_type") String _type,
            @Query("gpsLati") float latitude,
            @Query("gpsLong") float longitude
    );
}


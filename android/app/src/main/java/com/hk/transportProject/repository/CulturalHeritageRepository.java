package com.hk.transportProject.repository;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.hk.transportProject.AppService.EventApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CulturalHeritageRepository {
    private final EventApiService apiService;
    private final MutableLiveData<JsonObject> culturalHeritageData = new MutableLiveData<>();

    public CulturalHeritageRepository(EventApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<JsonObject> getCulturalHeritageData(int page, int perPage, String api_key) {
        Call<JsonObject> call = apiService.getEventInfo(page, perPage, api_key);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    culturalHeritageData.setValue(response.body());
                } else {
                    // 실패한 경우 null 또는 에러 처리 가능
                    culturalHeritageData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // 네트워크 에러 등으로 실패 시 null로 설정
                culturalHeritageData.setValue(null);
            }
        });
        return culturalHeritageData;
    }
}

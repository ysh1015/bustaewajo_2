package com.hk.transportProject.manager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hk.transportProject.AppService.TrafficApiService;
import com.hk.transportProject.R;
import com.hk.transportProject.Retrofit_Intanse.TrafficRetrofitClient;
import com.hk.transportProject.network.NetworkUtils;
import com.hk.transportProject.response.TrafficApiResponse;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusStopManager {
    private final Context context;
    private final NaverMap naverMap;
    private final List<Marker> markers;
    private final TrafficApiService apiService;
    
    private double currentLat;
    private double currentLng;
    
    public BusStopManager(Context context, NaverMap naverMap) {
        this.context = context;
        this.naverMap = naverMap;
        this.markers = new ArrayList<>();
        this.apiService = TrafficRetrofitClient.getClient()
            .create(TrafficApiService.class);
    }
    
    public void fetchNearbyBusStops(double latitude, double longitude) {
        this.currentLat = latitude;
        this.currentLng = longitude;
        
        if (!NetworkUtils.isNetworkAvailable(context)) {
            showError("네트워크 연결을 확인해주세요.");
            return;
        }
        
        clearMarkers();
        
        apiService.getTrafficInfo(
            "g3s/sU96JysexYpblDXIc4+V33peeadeoSi2BpBF5ej8XHRQtmPphiSA4dkF3s7b0CF5gDDO6/N2/weFSIDgCA==",
            1,
            10,
            "json",
            (float)latitude,
            (float)longitude
        ).enqueue(new Callback<TrafficApiResponse>() {
            @Override
            public void onResponse(Call<TrafficApiResponse> call,
                                   Response<TrafficApiResponse> response) {
                if (response.isSuccessful() && response.body() != null 
                    && response.body().isSuccessful()) {
                    handleSuccessResponse(response.body());
                } else {
                    showError("API 응답에 실패했습니다.");
                    Log.e("BusStopManager", "API response error: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<TrafficApiResponse> call, Throwable t) {
                showError("API 호출 중 오류가 발생했습니다.");
                Log.e("BusStopManager", "API call failed", t);
            }
        });
    }
    
    private void handleSuccessResponse(TrafficApiResponse response) {
        List<TrafficApiResponse.BusStation> stations = response.getValidBusStations();
        if (stations.isEmpty()) {
            showMessage("주변에 버스 정류장이 없습니다.");
            return;
        }
        
        for (TrafficApiResponse.BusStation station : stations) {
            if (station.isWithinRadius(currentLat, currentLng, 500)) {
                addBusStopMarker(station);
            }
        }
    }
    
    private void addBusStopMarker(TrafficApiResponse.BusStation station) {
        Marker marker = new Marker();
        marker.setPosition(station.toLatLng());
        marker.setIcon(OverlayImage.fromResource(R.drawable.ic_bus_stop));
        marker.setWidth(60);
        marker.setHeight(60);
        marker.setCaptionText(station.getNodeName() + 
            " (" + station.getFormattedDistance(currentLat, currentLng) + ")");
        marker.setTag(station);
        
        marker.setOnClickListener(overlay -> {
            showBusStopInfo(station);
            return true;
        });
        
        markers.add(marker);
        marker.setMap(naverMap);
    }
    
    private void showBusStopInfo(TrafficApiResponse.BusStation station) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(context);
        View bottomSheetView = LayoutInflater.from(context)
            .inflate(R.layout.bottom_sheet_bus_stop, null);
            
        TextView stationNameText = bottomSheetView.findViewById(R.id.station_name);
        TextView stationInfoText = bottomSheetView.findViewById(R.id.station_info);
        
        stationNameText.setText(station.getNodeName());
        
        String stationInfo = String.format(
            "정류소 번호: %d\n정류소 ID: %s\n도시 코드: %d",
            station.getNodeNo(),
            station.getNodeId(),
            station.getCityCode()
        );
        stationInfoText.setText(stationInfo);
        
        bottomSheet.setContentView(bottomSheetView);
        bottomSheet.show();
    }
    
    public void clearMarkers() {
        for (Marker marker : markers) {
            marker.setMap(null);
        }
        markers.clear();
    }
    
    private void showError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    private void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

} 
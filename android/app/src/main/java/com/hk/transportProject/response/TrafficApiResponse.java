package com.hk.transportProject.response;

import com.google.gson.annotations.SerializedName;
import com.naver.maps.geometry.LatLng;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TrafficApiResponse {
    @SerializedName("response")
    private Response response;

    public Response getResponse() {
        return response;
    }

    public static class Response {
        @SerializedName("header")
        private Header header;
        @SerializedName("body")
        private Body body;

        public Header getHeader() {
            return header;
        }

        public Body getBody() {
            return body;
        }

        public boolean isSuccess() {
            return header != null && "00".equals(header.getResultCode());
        }

        public List<BusStation> getBusStations() {
            if (body != null && body.getItems() != null) {
                return body.getItems().getItem();
            }
            return Collections.emptyList();
        }
    }

    public static class Header {
        @SerializedName("resultCode")
        private String resultCode;
        @SerializedName("resultMsg")
        private String resultMsg;

        public String getResultCode() {
            return resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }
    }

    public static class Body {
        @SerializedName("items")
        private Items items;
        @SerializedName("numOfRows")
        private int numOfRows;
        @SerializedName("pageNo")
        private int pageNo;
        @SerializedName("totalCount")
        private int totalCount;

        public Items getItems() {
            return items;
        }

        public int getNumOfRows() {
            return numOfRows;
        }

        public int getPageNo() {
            return pageNo;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }

    public static class Items {
        @SerializedName("item")
        private List<BusStation> item;

        public List<BusStation> getItem() {
            return item;
        }
    }

    public static class BusStation {
        @SerializedName("citycode")
        private int cityCode;

        @SerializedName("gpslati")
        private double latitude;

        @SerializedName("gpslong")
        private double longitude;

        @SerializedName("nodeid")
        private String nodeId;

        @SerializedName("nodenm")
        private String nodeName;

        @SerializedName("nodeno")
        private int nodeNo;

        public int getCityCode() {
            return cityCode;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public String getNodeId() {
            return nodeId;
        }

        public String getNodeName() {
            return nodeName;
        }

        public int getNodeNo() {
            return nodeNo;
        }

        public boolean isValid() {
            return nodeId != null 
                && nodeName != null 
                && latitude != 0 
                && longitude != 0;
        }

        public LatLng toLatLng() {
            return new LatLng(latitude, longitude);
        }

        public double calculateDistanceTo(double targetLat, double targetLng) {
            final double LATITUDE_TO_METER = 111200;   // 위도 1도당 약 111.2km
            final double LONGITUDE_TO_METER = 88800;   // 위도 37도에서 경도 1도당 약 88.8km
            
            double latDiff = Math.abs(targetLat - latitude);
            double lngDiff = Math.abs(targetLng - longitude);
            
            return Math.sqrt(
                Math.pow(latDiff * LATITUDE_TO_METER, 2) +
                Math.pow(lngDiff * LONGITUDE_TO_METER, 2)
            );
        }

        public String getFormattedDistance(double targetLat, double targetLng) {
            double distance = calculateDistanceTo(targetLat, targetLng);
            if (distance < 1000) {
                return String.format(Locale.KOREA, "%.0f m", distance);
            } else {
                return String.format(Locale.KOREA, "%.1f km", distance / 1000);
            }
        }

        public boolean isWithinRadius(double targetLat, double targetLng, double radiusInMeters) {
            return calculateDistanceTo(targetLat, targetLng) <= radiusInMeters;
        }

        @Override
        public String toString() {
            return "BusStation{" +
                    "nodeId='" + nodeId + '\'' +
                    ", nodeName='" + nodeName + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }

    public List<BusStation> getValidBusStations() {
        if (response == null) return Collections.emptyList();
        
        return response.getBusStations().stream()
                .filter(BusStation::isValid)
                .collect(Collectors.toList());
    }

    public boolean isSuccessful() {
        return response != null && response.isSuccess();
    }

    public String getErrorMessage() {
        if (response != null && response.getHeader() != null) {
            return response.getHeader().getResultMsg();
        }
        return "Unknown error";
    }
}
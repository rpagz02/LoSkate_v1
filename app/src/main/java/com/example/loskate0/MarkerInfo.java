package com.example.loskate0;

public class MarkerInfo
{
private String Latitude;
private String Longitude;

    public MarkerInfo(String latitude, String longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public MarkerInfo(){
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    @Override
    public String toString() {
        return "MarkerInfo{" +
                "Latitude='" + Latitude + '\'' +
                ", Longitude='" + Longitude + '\'' +
                '}';
    }
}

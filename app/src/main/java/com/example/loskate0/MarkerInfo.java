package com.example.loskate0;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo
{
    // readability
private String Latitude;
private String Longitude;
private String ID;

    public MarkerInfo(String latitude, String longitude, String _ID) {
        Latitude = latitude;
        Longitude = longitude;
        ID = _ID;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

package com.example.loskate0;

import android.graphics.Bitmap;


public class MarkerInfo
{
private String Latitude;
private String Longitude;
private String ID; // spot title
private String Notes; // description of the spot
private String Image; // user taken picture of the spot @ the spot


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public MarkerInfo(String latitude, String longitude, String _ID, String _Image, String _Notes) {
        Latitude = latitude;
        Longitude = longitude;
        ID = _ID;
        Image = _Image;
        Notes = _Notes;
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

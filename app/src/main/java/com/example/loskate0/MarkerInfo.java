package com.example.loskate0;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo
{
    private LatLng coords;


    public MarkerInfo(LatLng _coords)
    {
        this.coords = _coords;
    }
    public MarkerInfo() {
    }

    public LatLng getCoords() {
        return this.coords;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }
}

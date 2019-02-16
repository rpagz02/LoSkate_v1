package com.example.loskate0;

import com.google.android.gms.maps.model.LatLng;

public class MarkerInfo
{
    private LatLng coords;
    private int markerIndex;

    public MarkerInfo(LatLng _coords, int _markerIndex)
    {
        coords = _coords;
        markerIndex = _markerIndex;
    }
    public MarkerInfo() {
    }

    public LatLng getCoords() {
        return coords;
    }

    public void setCoords(LatLng coords) {
        this.coords = coords;
    }

    public int getMarkerIndex() {
        return markerIndex;
    }

    public void setMarkerIndex(int markerIndex) {
        this.markerIndex = markerIndex;
    }
}

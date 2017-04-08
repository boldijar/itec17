package com.bolnizar.code.data.model;

import com.google.android.gms.maps.model.LatLng;

import com.orm.SugarRecord;

public class PhotoRecord extends SugarRecord {
    public double latitude;
    public double longitude;
    public String path;
    public long time;

    public PhotoRecord(double latitude, double longitude, String path) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.path = path;
        time = System.currentTimeMillis();

    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    public PhotoRecord() {
    }
}


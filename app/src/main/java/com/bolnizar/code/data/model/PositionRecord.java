package com.bolnizar.code.data.model;

import com.google.android.gms.maps.model.LatLng;

import com.orm.SugarRecord;

public class PositionRecord extends SugarRecord {

    public long time;
    public double latitude;
    public double longitude;

    public static PositionRecord newPosition(double latitude, double longitude) {
        PositionRecord positionRecord = new PositionRecord();
        positionRecord.time = System.currentTimeMillis();
        positionRecord.latitude = latitude;
        positionRecord.longitude = longitude;
        return positionRecord;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}

package com.bolnizar.code.data.model;

import com.google.android.gms.maps.model.LatLng;

import com.orm.SugarRecord;

public class PositionRecord extends SugarRecord {

    public long time;
    public double latitude;
    public double longitude;
    public float speed;

    public static PositionRecord newPosition(double latitude, double longitude, float speed) {
        PositionRecord positionRecord = new PositionRecord();
        positionRecord.time = System.currentTimeMillis();
        positionRecord.latitude = latitude;
        positionRecord.speed = speed;
        positionRecord.longitude = longitude;
        return positionRecord;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }

    public static PositionRecord newPositionWithTime(double latitude, double longitude
            , float speed, long time) {
        PositionRecord positionRecord = new PositionRecord();
        positionRecord.time = time;
        positionRecord.longitude = longitude;
        positionRecord.latitude = latitude;
        positionRecord.speed = speed;
        return positionRecord;
    }



}

package com.bolnizar.code.data.model;

import com.orm.SugarRecord;

public class PositionRecord extends SugarRecord {

    public long time;
    public long latitude;
    public long longitude;

    public static PositionRecord newPosition(long latitude, long longitude) {
        PositionRecord positionRecord = new PositionRecord();
        positionRecord.time = System.currentTimeMillis();
        positionRecord.latitude = latitude;
        positionRecord.longitude = longitude;
        return positionRecord;
    }
}

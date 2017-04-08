package com.bolnizar.code.data.model;

import com.facebook.Profile;

public class LocationItem {
    public double latitude;
    public double longitude;
    public long time;
    public String userId;
    public String deviceId;

    public static LocationItem fromRecord(PositionRecord record, String deviceId) {
        LocationItem item = new LocationItem();
        item.latitude = record.latitude;
        item.longitude = record.longitude;
        item.time = record.time;
        if (Profile.getCurrentProfile() != null) {
            item.userId = Profile.getCurrentProfile().getId();
        }
        item.deviceId = deviceId;
        return item;
    }
}

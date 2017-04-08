package com.bolnizar.code.pages.map;

import com.bolnizar.code.data.model.LocationItem;

import java.util.List;

interface PathsMapView {
    void gotHotspots(List<LocationItem> items);
}

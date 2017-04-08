package com.bolnizar.code.pages.map;

import com.bolnizar.code.data.api.responses.BaseResponse;
import com.bolnizar.code.data.model.LocationItem;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface BackendService {

    @POST("send")
    Observable<BaseResponse> sync(@Body List<LocationItem> items);

    @POST("hotspots")
    Observable<List<LocationItem>> getHotspots(@Body LocationItem item);
}

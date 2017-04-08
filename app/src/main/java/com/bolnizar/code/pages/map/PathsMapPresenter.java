package com.bolnizar.code.pages.map;

import com.bolnizar.code.data.model.LocationItem;
import com.bolnizar.code.presenter.BasePresenter;
import com.bolnizar.code.utils.RxUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class PathsMapPresenter extends BasePresenter<PathsMapView> {

    @Inject
    PathsMapPresenter() {

    }

    @Inject
    BackendService mBackendService;

    void load() {
        LocationItem item = new LocationItem();
        if (MyLocationService.mLastLocation != null) {
            item.latitude = MyLocationService.mLastLocation.getLatitude();
            item.longitude = MyLocationService.mLastLocation.getLongitude();
        }
        addSubscription(mBackendService.getHotspots(item)
                .compose(RxUtils.provideDefaultTransformer())
                .subscribe(new Subscriber<List<LocationItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<LocationItem> locationItems) {
                        getView().gotHotspots(locationItems);
                    }
                }));

    }
}

package com.bolnizar.code.pages.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.bolnizar.code.R;
import com.bolnizar.code.utils.RxUtils;
import com.bolnizar.code.view.fragments.BaseFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import timber.log.Timber;

public class PathsMapFragment extends BaseFragment implements OnMapReadyCallback {

    private SupportMapFragment mMapFragment;
    private LatLng myLocation;

    private double MULTIPLYER = 10000000.0;
    private long mLatitude = 457539326;
    private long mLongitude = 211553162;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paths_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.paths_map);
        mMapFragment.getMapAsync(this);
    }

    private void initMap() {
        Observable.interval(1, TimeUnit.SECONDS)
                .compose(RxUtils.provideDefaultTransformer())
                .doOnNext(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        newPosition();
                    }
                })
                .subscribe();
    }

    private void newPosition() {

        double lat = mLatitude / MULTIPLYER;
        double longg = mLongitude / MULTIPLYER;
        Timber.e("Vals + " + lat + " " + longg);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLatitude / MULTIPLYER, mLongitude / MULTIPLYER))      // Sets the center of the map to location user
                .zoom(15.5f)
                .tilt(3)
                .build();                  // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //initMap();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(45.747656, 21.2258753))      // Sets the center of the map to location user
                .zoom(15.5f)
                .tilt(3)
                .build();                  // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}

package com.bolnizar.code.pages.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.bolnizar.code.R;
import com.bolnizar.code.data.model.PositionRecord;
import com.bolnizar.code.view.fragments.BaseFragment;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PathsMapFragment extends BaseFragment implements OnMapReadyCallback {

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private List<PositionRecord> mLocations = new ArrayList<>();

    private Marker mMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paths_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.paths_map);
        mMapFragment.getMapAsync(this);
        initLocationUpdate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_paths_map, menu);
    }

    private void initLocationUpdate() {
        getContext().startService(new Intent(getContext(), MyLocationService.class));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mLocations = SugarRecord.listAll(PositionRecord.class);
        drawAll();
    }

    private void drawLast2() {
        if (mLocations.size() <= 1) {
            return;
        }
        PolylineOptions polylineOptions = new PolylineOptions().width(10).color(Color.BLUE);
        List<PositionRecord> locations = mLocations.subList(mLocations.size() - 2, mLocations.size());
        if (distance(locations.get(0).toLatLng(), locations.get(1).toLatLng()) > 100) {
            return;
        }
        for (PositionRecord positionRecord : locations) {
            polylineOptions.add(positionRecord.toLatLng());
        }
        mMap.addPolyline(polylineOptions);
    }

    private float distance(LatLng pos1, LatLng pos2) {
        float[] result = new float[10];
        Location.distanceBetween(pos1.latitude, pos1.longitude,
                pos2.latitude, pos2.longitude, result);
        return result[0];
    }

    private void drawAll() {
        if (mLocations.size() <= 1) {
            return;
        }
        LatLng lastLatLng = null;
        for (PositionRecord positionRecord : mLocations) {
            LatLng newPosition = positionRecord.toLatLng();
            if (lastLatLng != null) {
                PolylineOptions polylineOptions = new PolylineOptions().width(10).color(Color.BLUE);
                polylineOptions.add(lastLatLng).add(newPosition);
                if (distance(lastLatLng, newPosition) < 100) {
                    mMap.addPolyline(polylineOptions);
                }
            }
            lastLatLng = newPosition;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLocations.get(mLocations.size() - 1).toLatLng())
                .zoom(18)
                .tilt(3)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
    private void goToPosition(Location location) {
        if (mMap == null) {
            return;
        }
        PositionRecord positionRecord = PositionRecord.newPosition(location.getLatitude(), location.getLongitude(), location.getSpeed());
        positionRecord.save();
        mLocations.add(positionRecord);

        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        if (!bounds.contains(positionRecord.toLatLng()) || mMap.getCameraPosition().zoom < 10) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(positionRecord.toLatLng())
                    .zoom(18)
                    .tilt(3)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        if (mMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().title("Current position").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)).position(positionRecord.toLatLng());
            mMarker = mMap.addMarker(markerOptions);
        }
        mMarker.setPosition(positionRecord.toLatLng());
        if (mLocations.size() <= 1) {
            return;
        }
        drawLast2();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Location event) {
        goToPosition(event);
    }

}

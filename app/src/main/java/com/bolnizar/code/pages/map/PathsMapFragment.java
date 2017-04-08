package com.bolnizar.code.pages.map;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.bolnizar.code.R;
import com.bolnizar.code.data.model.PhotoRecord;
import com.bolnizar.code.data.model.PositionRecord;
import com.bolnizar.code.utils.ResourceUtil;
import com.bolnizar.code.view.fragments.BaseFragment;
import com.mindorks.paracamera.Camera;
import com.orm.SugarRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class PathsMapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private List<PositionRecord> mLocations = new ArrayList<>();

    private Marker mMarker;
    private Camera mCamera;

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
        initCam();
    }

    private void initCam() {
        mCamera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(50)
                .setImageHeight(700)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_paths_map, menu);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        map.setOnMarkerClickListener(this);
        mLocations = SugarRecord.listAll(PositionRecord.class);
        drawAll();
        drawImages();

        if (mLocations.size() > 0) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(getContext(), R.drawable.ic_location));
            MarkerOptions markerOptions = new MarkerOptions().title("Current position").position(mLocations.get(mLocations.size() - 1).toLatLng()).icon(icon);
            mMarker = mMap.addMarker(markerOptions);
        }
    }

    private void drawImages() {
        List<PhotoRecord> records = SugarRecord.listAll(PhotoRecord.class);
        for (PhotoRecord record : records) {
            displayPhotoRecord(record);
        }
    }

    private int getColor(float speed) {
        if (speed < 10) {
            return ContextCompat.getColor(getContext(), R.color.yellow);
        }
        if (speed < 13) {
            return ContextCompat.getColor(getContext(), R.color.orange);
        }
        if (speed < 18) {
            return ContextCompat.getColor(getContext(), R.color.red);
        }
        return ContextCompat.getColor(getContext(), R.color.red_dark);
    }

    private void drawLast2() {
        if (mLocations.size() <= 1) {
            return;
        }
        PolylineOptions polylineOptions = new PolylineOptions().width(10).color(getColor(mLocations.get(mLocations.size() - 1).speed));
        List<PositionRecord> locations = mLocations.subList(mLocations.size() - 2, mLocations.size());
        if (distance(locations.get(0).toLatLng(), locations.get(1).toLatLng()) > 1000) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_map_photo) {
            Timber.d("Clicked photo icon");
            try {
                mCamera.takePicture();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = mCamera.getCameraBitmap();
            if (bitmap != null) {
                gotPhoto();

            } else {
                Toast.makeText(this.getContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void gotPhoto() {
        if (mLocations.size() < 1) {
            return;
        }
        PositionRecord positionRecord = mLocations.get(mLocations.size() - 1);
        PhotoRecord photoRecord = new PhotoRecord(positionRecord.latitude, positionRecord.longitude, mCamera.getCameraBitmapPath());
        photoRecord.save();
        displayPhotoRecord(photoRecord);
    }

    private void displayPhotoRecord(PhotoRecord photoRecord) {
        MarkerOptions options = new MarkerOptions().position(photoRecord.toLatLng()).title(new Date(photoRecord.time).toString());
        mMap.addMarker(options).setTag(photoRecord.path);
    }

    private void drawAll() {
        if (mLocations.size() <= 1) {
            return;
        }
        LatLng lastLatLng = null;
        for (PositionRecord positionRecord : mLocations) {
            LatLng newPosition = positionRecord.toLatLng();
            if (lastLatLng != null) {
                PolylineOptions polylineOptions = new PolylineOptions().width(10).color(getColor(positionRecord.speed));
                polylineOptions.add(lastLatLng).add(newPosition);
                if (distance(lastLatLng, newPosition) < 1000) {
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

    @OnClick(R.id.paths_fab)
    void fabClick() {
        if (mLocations.size() == 0) {
            return;
        }
        LatLng latLng = mLocations.get(mLocations.size() - 1).toLatLng();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(18)
                .tilt(3)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void goToPosition(Location location) {
        if (mMap == null) {
            return;
        }
        PositionRecord positionRecord = PositionRecord.newPosition(location.getLatitude(), location.getLongitude(), location.getSpeed());
        positionRecord.save();
        Timber.d("SPEED " + positionRecord.speed);
        mLocations.add(positionRecord);
        if (mMarker == null) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(getContext(), R.drawable.ic_location));
            MarkerOptions markerOptions = new MarkerOptions().title("Current position").position(positionRecord.toLatLng()).icon(icon);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTag() == null) {
            return true;
        }
        ImageFragment.newInstance(marker.getTag().toString()).show(getChildFragmentManager(), "TAGA");
        return true;
    }
}

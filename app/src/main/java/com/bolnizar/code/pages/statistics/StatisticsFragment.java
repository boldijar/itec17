package com.bolnizar.code.pages.statistics;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolnizar.code.R;
import com.bolnizar.code.data.model.PositionRecord;
import com.bolnizar.code.view.fragments.BaseFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.dacer.androidcharts.BarView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

import static com.orm.SugarRecord.find;

/**
 * Created by bjz on 4/8/2017.
 */

public class StatisticsFragment extends BaseFragment {
    @BindView(R.id.km_hour_chart)
    BarView barView;
    @BindView(R.id.pie_view)
    PieView mPieView;
    @BindView(R.id.avg_speed_label)
    TextView avgLabel;
    @BindView(R.id.max_speed_label)
    TextView maxLabel;
    ArrayList<String> hours = new ArrayList<>();
    @BindView(R.id.meters_walked_label)
    TextView distanceLabel;
    int maximumWalked = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        //generateLocations();
        initChart();
        initPieChart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    private void initChart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -24);
        long check = calendar.getTimeInMillis();
        long totalDistance = 0;
        float totalSpeed = 0;
        float maxSpeed = 0;

        List<PositionRecord> results = PositionRecord.find(PositionRecord.class
                , "time >= ?"
                , String.valueOf(check));

        Collections.sort(results, (o1, o2) -> {
            if (o1.time == o2.time) {
                return 0;
            } else if (o1.time > o2.time) {
                return -1;
            } else {
                return 1;
            }
        });

        Calendar calendar1 = Calendar.getInstance();
        HashMap<Integer, LatLng> activity = new HashMap<>();
        HashMap<Integer, Integer> activityPerHour = new HashMap<>();

        for (int i = 0; i <= 24; i++) {
            hours.add(i, String.valueOf(i));
            activityPerHour.put(i, 0);
        }


        for (PositionRecord positionRecord : results) {
            calendar1.setTimeInMillis(positionRecord.time);

            int hour = calendar1.get(Calendar.HOUR);

            if (activity.containsKey(hour)) {
                Integer distance = (int) distance(activity.get(hour), positionRecord.toLatLng());
                activityPerHour.put(hour, distance);
                Log.i("HOUR: ", String.valueOf(hour) + " " + distance);
                if(distance > maximumWalked){
                    maximumWalked = (int)distance;
                }
                totalDistance += distance;
                totalSpeed += positionRecord.speed;
                if (positionRecord.speed > maxSpeed) {
                    maxSpeed = positionRecord.speed;
                }
            }
            Log.i("SPEED AVG: ", "" + totalSpeed / (float)activity.size());
            Log.i("MAX SPEED: ", "" + maxSpeed);

            initLabels(totalSpeed / activity.size(), maxSpeed);
            activity.put(hour, positionRecord.toLatLng());
        }


        barView.setBottomTextList(hours);
        barView.setDataList(new ArrayList<Integer>(activityPerHour.values()), maximumWalked);
        distanceLabel.setText("You walked " + String.valueOf(totalDistance) + " meters today!");

    }

    private void initPieChart() {
        PieHelper pieHelper = new PieHelper(24.5f, Color.GREEN);
        PieHelper pieHelper2 = new PieHelper(50.5f, Color.RED);
        PieHelper pieHelper3 = new PieHelper(15.5f, Color.BLUE);

        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();
        pieHelperArrayList.add(pieHelper);
        pieHelperArrayList.add(pieHelper2);
        pieHelperArrayList.add(pieHelper3);

        mPieView.setDate(pieHelperArrayList);
        mPieView.selectedPie(2); //optional
        mPieView.showPercentLabel(true); //optional
    }

    private void initLabels(float avg, float max) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(0, (int) avg);
        animator.setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                avgLabel.setText("" + animation.getAnimatedValue() + "km/h");
            }
        });
        animator.start();

        ValueAnimator animator2 = new ValueAnimator();
        animator2.setObjectValues(0, (int) max);
        animator2.setDuration(1500);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                maxLabel.setText("" + animation.getAnimatedValue() + "km/h");
            }
        });
        animator2.start();
    }


    private float distance(LatLng pos1, LatLng pos2) {
        float[] result = new float[10];
        Location.distanceBetween(pos1.latitude, pos1.longitude,
                pos2.latitude, pos2.longitude, result);
        return result[0];
    }

    private void generateLocations() {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        double longitude = 45.741d;
        double latitude = 21.237d;
        for (int i = 0; i < 100; i++) {
            float speed =(float) Math.abs(random.nextInt()) % 120;
            calendar.add(Calendar.HOUR, random.nextInt() % 24);
            Log.i("GENERATED HOUR: ", String.valueOf(calendar.getTimeInMillis()));
            longitude += random.nextDouble();
            latitude += random.nextDouble();
            PositionRecord positionRecord = PositionRecord.newPositionWithTime(latitude
                    , longitude
                    , speed
                    , calendar.getTimeInMillis());
            positionRecord.save();
        }
    }

}

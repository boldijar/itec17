package com.bolnizar.code.pages.statistics;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bolnizar.code.R;
import com.bolnizar.code.data.model.PositionRecord;
import com.bolnizar.code.view.fragments.BaseFragment;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    @BindView(R.id.meters_walked_label)
    TextView distanceLabel;


    int maximumWalked = 0;
    ArrayList<String> hours = new ArrayList<>();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initChart();
        initPieChart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Your stats");
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
        maximumWalked = 0;

        ArrayList<PositionRecord> results = StatisticsService.getDailyStats(check);
        int resultsNumber = results.size();


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
                activityPerHour.put(hour, activityPerHour.get(hour) + distance);
                Log.i("HOUR: ", String.valueOf(hour) + " " + distance);

                if (distance > maximumWalked) {
                    maximumWalked = distance;
                }
                totalDistance += distance;
                totalSpeed += positionRecord.speed;
                if (positionRecord.speed > maxSpeed) {
                    maxSpeed = positionRecord.speed;
                }
            }
            Log.i("SPEED AVG: ", "" + totalSpeed / (float) activity.size());
            Log.i("MAX SPEED: ", "" + maxSpeed);
            Log.i("MAX WALKED: ", "" + maximumWalked);


            activity.put(hour, positionRecord.toLatLng());
        }

        initLabels(totalSpeed / resultsNumber, maxSpeed);
        barView.setBottomTextList(hours);
        barView.setDataList(new ArrayList<>(activityPerHour.values()), maximumWalked);
        distanceLabel.setText("You traveled " + String.valueOf(totalDistance) + " meters today!");

    }

    private void initPieChart() {
        ArrayList<PositionRecord> results = StatisticsService.getAllStats();
        Float nrOfResults = (float) results.size();
        ArrayList<Integer> colors = new ArrayList<Integer>() {{
            add(R.color.deep_sky_blue);
            add(R.color.red);
            add(R.color.yellow);
            add(R.color.gainsboro);
            add(R.color.colorAccent);
            add(R.color.orange);
            add(R.color.harvard_crimson);
        }};
        HashMap<Integer, Integer> percentFreq = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        for (PositionRecord record : results) {
            calendar.setTimeInMillis(record.time);
            Integer weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            if (percentFreq.containsKey(weekDay)) {
                percentFreq.put(weekDay, percentFreq.get(weekDay) + 1);
            } else {
                percentFreq.put(weekDay, 1);
            }
        }

        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            PieHelper pieHelper;
            if (percentFreq.containsKey(i)) {

                float percent = (percentFreq.get(i) / nrOfResults) * 100;
                Log.i("PERCENT FOR " + i, " " + percent);
                pieHelper = new PieHelper(percent
                       , ContextCompat.getColor(getContext(), colors.get(i-1)));
                pieHelperArrayList.add(pieHelper);
            }
        }

        mPieView.setDate(pieHelperArrayList);
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


}

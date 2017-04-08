package com.bolnizar.code.pages.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bolnizar.code.R;
import com.bolnizar.code.data.model.PositionRecord;
import com.bolnizar.code.view.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.dacer.androidcharts.BarView;

/**
 * Created by bjz on 4/8/2017.
 */

public class StatisticsFragment extends BaseFragment{
    @BindView(R.id.km_hour_chart)
    BarView barView;
    ArrayList<String> hours = new ArrayList<>();
    ArrayList<Integer> activity= new ArrayList<>();
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initChart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    private void initChart() {
        Random random = new Random();
        random.setSeed(2);
        for(int i = 0 ; i <=24 ; i ++){
            hours.add(i, String.valueOf(i));
            activity.add(i,Math.abs(random.nextInt())%100);
        }

        barView.setBottomTextList(hours);
        barView.setDataList(activity,100);

    }

  



}

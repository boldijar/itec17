package com.bolnizar.code.pages.statistics;

import com.bolnizar.code.data.model.PositionRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bjz on 4/8/2017.
 */

public class StatisticsService {
    public static ArrayList<PositionRecord> getDailyStats(long since) {
        List<PositionRecord> results = PositionRecord.find(PositionRecord.class
                , "time >= ?"
                , String.valueOf(since));

        Collections.sort(results, (o1, o2) -> {
            if (o1.time == o2.time) {
                return 0;
            } else if (o1.time > o2.time) {
                return -1;
            } else {
                return 1;
            }
        });

        return new ArrayList<>(results);
    }

    public static ArrayList<PositionRecord> getAllStats() {
        List<PositionRecord> results = PositionRecord.listAll(PositionRecord.class);

        Collections.sort(results, (o1, o2) -> {
            if (o1.time == o2.time) {
                return 0;
            } else if (o1.time > o2.time) {
                return -1;
            } else {
                return 1;
            }
        });

        return new ArrayList<>(results);
    }

}

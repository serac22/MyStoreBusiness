package com.example.mystorebusiness.account.ui.statistics;

import android.content.res.Resources;


;
import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.statistics.charts.ColumnChartActivity;
import com.example.mystorebusiness.account.ui.statistics.charts.PieChartActivity;
import com.example.mystorebusiness.account.ui.statistics.charts.VerticalChartActivity;

import java.util.ArrayList;

public class Chart {

    private String name;
    private Class activityClass;

    private Chart(String name, Class activityClass) {
        this.name = name;
        this.activityClass = activityClass;
    }

    public String getName() {
        return name;
    }

    Class getActivityClass() {
        return activityClass;
    }

    static ArrayList<Chart> createChartList(Resources resources) {
        ArrayList<Chart> chartList = new ArrayList<>();

        chartList.add(new Chart(resources.getString(R.string.pie_chart), PieChartActivity.class));
        chartList.add(new Chart(resources.getString(R.string.vertical_chart), ColumnChartActivity.class));
        chartList.add(new Chart(resources.getString(R.string.column_chart), VerticalChartActivity.class));


        return chartList;
    }

}

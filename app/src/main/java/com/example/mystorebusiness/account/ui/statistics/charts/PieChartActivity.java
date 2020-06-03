package com.example.mystorebusiness.account.ui.statistics.charts;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.mystorebusiness.R;
import com.example.mystorebusiness.account.ui.employees.Employee;
import com.example.mystorebusiness.data.Db_Employee;


import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_common);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(PieChartActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        int id_user = Integer.valueOf(this.getIntent().getIntExtra("user_id",0));
        Db_Employee db= new Db_Employee(this);
        ArrayList<Employee> dataEmployees=db.getAllEmployees("",id_user);
        List<DataEntry> data = new ArrayList<>();

        for (int i = 0; i < dataEmployees.size(); i++) {
            data.add(new ValueDataEntry(dataEmployees.get(i).getName(), dataEmployees.get(i).getSalary()));
        }

        pie.data(data);

        pie.title("Salary expenditure statistics");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Employees")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        anyChartView.setChart(pie);
    }
}

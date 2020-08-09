package com.example.qunltxe.screen.statistical;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qunltxe.R;
import com.example.qunltxe.database.DBDonDatHang;
import com.example.qunltxe.screen.home.TrangChu;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class ThongKe extends AppCompatActivity {

    DBDonDatHang dbDonDatHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thong_ke);
        getSupportActionBar().setTitle("Thống kê dữ liệu");
        drawChart();
    }

    private void drawChart() {

        // Hướng dẫn làm BarChart https://www.youtube.com/watch?v=niLkRACZEMg
        dbDonDatHang = new DBDonDatHang(this);

        // trục Y
        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        final ArrayList<String> yData = dbDonDatHang.queryYData();
        // trục X
        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = dbDonDatHang.queryXData();

        if (yData.isEmpty() || xData.isEmpty()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ThongKe.this);
            alert.setTitle("Thông báo");
            alert.setMessage("Bạn chưa có dữ liệu nào");
            alert.setPositiveButton("OK", null);
            alert.show();
        } else {

            for (int i = 0; i < dbDonDatHang.queryYData().size(); i++) {
                BarEntry newBarEntry = new BarEntry(i, Float.parseFloat(dbDonDatHang.queryYData().get(i)));
                yVals.add(newBarEntry);
            }

            for (int i = 0; i < dbDonDatHang.queryXData().size(); i++) {
                xVals.add(xData.get(i));
            }
        }

        BarChart barChart = findViewById(R.id.barchart);
        BarDataSet dataSet = new BarDataSet(yVals, "Số lượng xe đã bán");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<IBarDataSet> dataSets1 = new ArrayList();
        dataSets1.add(dataSet);
        BarData data = new BarData(dataSets1);

        data.setBarWidth(0.3f);
        data.setValueTextSize(18f);
        data.setValueFormatter(new IntValueFormatter());

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        barChart.getDescription().setEnabled(false);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(true);
        xAxis.isCenterAxisLabelsEnabled();
        xAxis.setGranularityEnabled(true);
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
        barChart.setMaxVisibleValueCount(5);
        barChart.setFitBars(true);
        barChart.animateY(2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.thongke_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuID = item.getItemId();
        switch (menuID) {
            case R.id.menuItemHome:
                backHomePage();
                break;

            default:
                AlertDialog.Builder alert = new AlertDialog.Builder(ThongKe.this);
                alert.setTitle("Lỗi");
                alert.setMessage("Có lỗi xảy ra !");
                alert.setPositiveButton("OK", null);
                alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void backHomePage() {
        Intent intent = new Intent(ThongKe.this, TrangChu.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    public class IntValueFormatter extends ValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return String.valueOf((int) value);
        }
    }
}
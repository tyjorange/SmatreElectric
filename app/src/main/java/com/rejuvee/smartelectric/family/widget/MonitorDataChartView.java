package com.rejuvee.smartelectric.family.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.rejuvee.smartelectric.family.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchengran on 2017/8/30.
 */
public class MonitorDataChartView extends FrameLayout {
    private LineChart mChart;
    private MyMarkerView mv;


    public MonitorDataChartView(Context context) {
        super(context);
        init(context);
    }

    public MonitorDataChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.widget_monitor_chart, this);
        mChart = (LineChart) findViewById(R.id.chart_content);

        mChart.setDrawGridBackground(false);
        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(true);
        mChart.setNoDataText("暂无数据");
        mChart.setNoDataTextColor(R.color.black);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
        mChart.clear();
    }

    public boolean hasData() {
        return mChart.getData() != null;
    }

    /**
     * 初始化表格数据
     *
     * @param minYValue y轴数据最小值
     * @param maxYValue y轴数据库最大值
     * @param data      支持多条数据曲线
     */
    public void setupData(float minYValue, float maxYValue, final List<MonitorData> data) {

        mv.setData(data.get(0).getListData());

        if (data == null)
            return;

        //X轴底部日期数据
        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //Log.d(TAG, "value=" + value);
                int position = (int) value;
                if (position < 0 || position >= data.get(0).listData.size())
                    return "";

                return data.get(0).listData.get((int) value).time;
            }

        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);


        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(maxYValue);
        leftAxis.setAxisMinimum(minYValue);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        // add data
        setData(data);

        //mChart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);
        // modify the legend ...
        //l.setForm(Legend.LegendForm.CIRCLE);
    }

    public void clearData() {
        mChart.clear();
    }

    private void setData(List<MonitorData> data) {
        LineDataSet[] dataSets = new LineDataSet[data.size()];
        for (int i = 0; i < data.size(); i++) {
            MonitorData monitor = data.get(i);
            List<Entry> yVals = new ArrayList<>();
            int index = 0;
            for (MonitorDataItem item : monitor.listData) {
                yVals.add(new Entry(index++, item.value));
            }

            LineDataSet set = new LineDataSet(yVals, monitor.dataName);

            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(monitor.color);
            set.setCircleColor(monitor.color);
            set.setLineWidth(2f);
//            set.setCircleRadius(3f);
            set.setFillAlpha(65);
            set.setFillColor(ColorTemplate.getHoloBlue());
            set.setHighLightColor(Color.rgb(244, 117, 117));
            set.setDrawCircleHole(false);
            dataSets[i] = set;
        }
        // create a data object with the datasets
        LineData lineData = new LineData(dataSets);

        //lineData.setValueTextColor(Color.BLUE);
        //lineData.setValueTextSize(9f);

        // set data
        mChart.setData(lineData);
        List<ILineDataSet> sets = mChart.getData()
                .getDataSets();

        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            set.setMode(LineDataSet.Mode.LINEAR);
            set.setDrawFilled(true);
//            set.setDrawCircles(false);
            set.setDrawCircles(true);
//            set.setCircleRadius(5f);
            set.setDrawValues(false);
        }
    }

    public void updateData() {
        mChart.invalidate();
        invalidate();
    }


    public static class MonitorDataItem {
        private String time;//时间值
        private float value;//对应的检测值

        public MonitorDataItem(String time, float value) {
            this.time = time;
            this.value = value;
        }

        public MonitorDataItem() {

        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    public static class MonitorData {
        String dataName;//数据源名称
        int color;//曲线颜色

        List<MonitorDataItem> listData;

        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public List<MonitorDataItem> getListData() {
            return listData;
        }

        public void setListData(List<MonitorDataItem> listData) {
            this.listData = listData;
        }

    }


}

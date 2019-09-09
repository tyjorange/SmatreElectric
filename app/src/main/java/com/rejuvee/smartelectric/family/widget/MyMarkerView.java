
package com.rejuvee.smartelectric.family.widget;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.rejuvee.smartelectric.family.R;

import java.util.List;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvValue;
    private TextView tvTime;
    private List<MonitorDataChartView.MonitorDataItem> listData;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvValue = (TextView) findViewById(R.id.tv_value);
        tvTime = (TextView) findViewById(R.id.tv_time);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvValue.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            tvValue.setText("" + e.getY());
            tvTime.setText("" + listData.get((int) e.getX()).getTime());
        }

        super.refreshContent(e, highlight);
    }

    public void setData(List<MonitorDataChartView.MonitorDataItem> listData) {
        this.listData = listData;
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}

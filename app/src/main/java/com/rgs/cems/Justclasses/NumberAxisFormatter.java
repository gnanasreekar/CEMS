package com.rgs.cems.Justclasses;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class NumberAxisFormatter implements IAxisValueFormatter, IValueFormatter {

    private DecimalFormat mFormat;

    public NumberAxisFormatter() {
        mFormat = new DecimalFormat("###,###,###,###");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format((int) value);
    }

    @Override
    public String getFormattedValue(float value, Entry entry,
                                    int dataSetIndex, ViewPortHandler viewPortHandler) {
        return mFormat.format((int) value);
    }
}
package com.zq.widget.axis;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/21.
 */

public class XAxis {

    private long minValue;

    private long maxValue;

    private List<Item> items;

    public long getMinValue() {
        return minValue;
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item{

        private long value;

        private String drawText;

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public String getDrawText() {
            return drawText;
        }

        public void setDrawText(String drawText) {
            this.drawText = drawText;
        }
    }
}

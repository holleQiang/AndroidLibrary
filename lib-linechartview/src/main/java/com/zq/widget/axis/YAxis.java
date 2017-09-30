package com.zq.widget.axis;

import java.util.List;

/**
 * Created by zhangqiang on 2017/9/21.
 */

public class YAxis {

    private int minValue;

    private int maxValue;

    private List<Item> items;

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item{

        private int value;

        private String drawText;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
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

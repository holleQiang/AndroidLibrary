package com.zq.widget.ringchart;

/**
 * 弧
 * Created by zhangqiang on 2017/10/18.
 */

public class ArcItem {

    /**
     * 权重
     */
    private int weight;

    /**
     * 颜色
     */
    private int color;

    /**
     * 文字颜色
     */
    private int textColor;

    /**
     * 文字
     */
    private String text;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

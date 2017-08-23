package com.zq.view.wheelview.date;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.zq.view.wheelview.Adapter;
import com.zq.view.wheelview.OnItemSelectListener;
import com.zq.view.wheelview.R;
import com.zq.view.wheelview.TDWheelView;
import com.zq.view.wheelview.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期选择控件
 * Created by zhangqiang on 2017/8/23.
 */

public class DatePickView extends LinearLayout{

    private int minYear = 1900;
    private int maxYear = 2100;

    private TDWheelView yearWheelView;
    private TDWheelView monthWheelView;
    private TDWheelView dayWheelView;

    public DatePickView(Context context) {
        super(context);
        init(context);
    }

    public DatePickView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DatePickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        LayoutInflater.from(context).inflate(R.layout.view_date_picker,this,true);
        yearWheelView  = (TDWheelView) findViewById(R.id.tdw_year);
        monthWheelView = (TDWheelView) findViewById(R.id.tdw_month);
        dayWheelView   = (TDWheelView) findViewById(R.id.tdw_day);
        yearWheelView.setOnItemSelectListener(calculateDaysItemSelectListener);
        monthWheelView.setOnItemSelectListener(calculateDaysItemSelectListener);
        yearWheelView.setAdapter(new YearAdapter());
        monthWheelView.setAdapter(new MonthAdapter());
    }

    private class YearAdapter implements Adapter{

        private List<Integer> yearList = new ArrayList<>();
        private String rightText;

        public YearAdapter() {

            for (int i = minYear; i <= maxYear; i++) {

                yearList.add(i);
            }
            rightText = getResources().getString(R.string.year);
        }

        @Override
        public int getItemCount() {
            return yearList.size();
        }

        @Override
        public String getTextAt(int position) {
            return yearList.get(position) + "";
        }

        @Override
        public String getLeftExtraText(int position) {
            return null;
        }

        @Override
        public String getRightExtraText(int position) {
            return rightText;
        }
    }

    private class MonthAdapter implements Adapter{

        private List<Integer> monthList = new ArrayList<>();
        private String rightText;

        public MonthAdapter() {

            for (int i = 1; i <= 12; i++) {
                monthList.add(i);
            }
            rightText = getResources().getString(R.string.month);
        }

        @Override
        public int getItemCount() {
            return monthList.size();
        }

        @Override
        public String getTextAt(int position) {
            return monthList.get(position) + "";
        }

        @Override
        public String getLeftExtraText(int position) {
            return null;
        }

        @Override
        public String getRightExtraText(int position) {
            return rightText;
        }
    }

    private class DayAdapter implements Adapter{

        private String rightText;
        private List<Integer> dayList = new ArrayList<>();

        public DayAdapter(int year, int month) {

            rightText = getResources().getString(R.string.day);

            int day = DateUtil.getDaysOfMonth(year, month);
            for (int i = 1; i <= day; i++) {
                dayList.add(i);
            }
        }

        @Override
        public int getItemCount() {
            return dayList.size();
        }

        @Override
        public String getTextAt(int position) {
            return dayList.get(position) + "";
        }

        @Override
        public String getLeftExtraText(int position) {
            return null;
        }

        @Override
        public String getRightExtraText(int position) {
            return rightText;
        }
    }

    public int getMinYear() {
        return minYear;
    }

    public void setYearRange(int minYear,int maxYear) {
        this.minYear = minYear;
        this.maxYear = maxYear;
        yearWheelView.setAdapter(new YearAdapter());
    }

    public int getMaxYear() {
        return maxYear;
    }

    private OnItemSelectListener calculateDaysItemSelectListener = new OnItemSelectListener() {
        @Override
        public void onItemSelect(int position, int firstVisiblePosition, int lastVisiblePosition) {

            String yearStr = yearWheelView.getAdapter().getTextAt(yearWheelView.getSelectPosition());
            int year = Integer.valueOf(yearStr);

            String monthStr = monthWheelView.getAdapter().getTextAt(monthWheelView.getSelectPosition());
            int month = Integer.valueOf(monthStr);

            dayWheelView.setAdapter(new DayAdapter(year,month));
        }
    };

    public int getYear(){

        String yearStr = yearWheelView.getAdapter().getTextAt(yearWheelView.getSelectPosition());
        return Integer.valueOf(yearStr);
    }

    public int getMonth(){

        String monthStr = monthWheelView.getAdapter().getTextAt(monthWheelView.getSelectPosition());
        return Integer.valueOf(monthStr);
    }

    public int getDay(){

        String dayStr = dayWheelView.getAdapter().getTextAt(dayWheelView.getSelectPosition());
        return Integer.valueOf(dayStr);
    }

}

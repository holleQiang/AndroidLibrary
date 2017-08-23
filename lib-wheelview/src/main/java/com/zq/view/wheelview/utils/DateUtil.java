package com.zq.view.wheelview.utils;

/**
 * Created by zhangqiang on 2017/8/15.
 */

public class DateUtil {

    /**
     * 获取某个年月的天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year,int month){

        if (month == 2) {

            if (year % 100 == 0) {

                if (year % 400 == 0) {

                    return 29;
                }
                return 28;
            }
            return year % 4 == 0 ? 29 : 28;
        }

        if (month == 1 ||
                month == 3 ||
                month == 5 ||
                month == 7 ||
                month == 8 ||
                month == 10 ||
                month == 12) {
            return 31;
        }
        if(month == 4 ||
                month == 6 ||
                month == 9 ||
                month == 11){
            return 30;
        }
        return 0;
    }
}

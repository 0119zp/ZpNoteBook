package zp.com.zpbase.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import zp.com.zpbase.R;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class ZpDateUtils {

    /**
     * 获取日期字符串
     *
     * @param pattern
     * @param milliseconds
     * @return
     */
    public static String getDateString(String pattern, long milliseconds) {
        if (0 == milliseconds)
            return "";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(milliseconds));
    }

    public static Calendar getDate2Calendar(long milliseconds) {
        Date date = new Date(milliseconds);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Get date ("xxxx年xx月xx日")
     *
     * @param context
     * @param milliseconds
     * @return
     */
    public static String getDate2CHString(Context context, long milliseconds) {
        Calendar calendar = getDate2Calendar(milliseconds);
        return context.getString(R.string.date_ch_template, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * If hasDayOfMonth is true, return {@linkDateUtil #getDate2CHString(Context, long)}, else return ("xxxx年xx月")
     *
     * @param context
     * @param hasDayOfMonth
     * @param milliseconds
     * @return
     */
    public static String getDate2CHString(Context context, boolean hasDayOfMonth, long milliseconds) {
        if (hasDayOfMonth)
            return getDate2CHString(context, milliseconds);

        Calendar calendar = getDate2Calendar(milliseconds);
        return context.getString(R.string.date_ch_no_day_template, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取给定日期的第二天日期
     *
     * @param date
     * @return
     */
    public static Date getNextNDate(Date date, int n) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, n);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果

        return date;
    }

    /**
     * 返回日期字符串
     *
     * @param date
     * @param format 如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 返回日期字符串
     *
     * @param format 如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getDateString(long milliseconds, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(milliseconds));
    }

    /**
     * 根据日期字符串返回日期
     *
     * @param dateStr
     * @param format  如yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDate(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 根据一个日期，返回是星期几的字符串, 如"星期一"
     *
     * @return
     */
    public static String getWeek(Date date) {
        return new SimpleDateFormat("EEEE", Locale.CHINA).format(date);
    }

    /**
     * 返回一周的第几天, 1-7表示周一到周日
     *
     * @param date
     * @return
     */
    public static int getDayForWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 返回给定时间距离当前时间的小时数
     *
     * @param date
     * @return
     */
    public static double getHourDistance(Date date) {
        long curTime = System.currentTimeMillis();
        long time = date.getTime();

        double hour = (time - curTime) * 1.0 / (60 * 60 * 1000);

        return hour;
    }

    /**
     * 获取与当前时间的相隔天数
     *
     * @param dest milliseconds 毫秒数
     * @return
     */
    public static int daysBetween(long dest) {
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTimeInMillis(dest);
        caled.setTimeInMillis(System.currentTimeMillis());
        // 设置时间为0时
        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calst.set(java.util.Calendar.MINUTE, 0);
        calst.set(java.util.Calendar.SECOND, 0);
        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
        caled.set(java.util.Calendar.MINUTE, 0);
        caled.set(java.util.Calendar.SECOND, 0);
        // 得到两个日期相差的天数
        int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
        return days;
    }

    /**
     * 获取n天后第m小时的date<br>
     * 如n=1, m=12时, 返回第二天12:00的Date
     *
     * @param n
     * @param m
     * @return
     */
    public static Date getNDaysMHoursDate(int n, int m) {
        Date d = getNextNDate(new Date(), n);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, m);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    /**
     * 返回给定时间距离当前时间的小时数
     *
     * @param
     * @return
     */
    public static int getHourDistance(long time) {
        long curTime = System.currentTimeMillis();
        int hour = (int) ((curTime - time) * 1.0 / (60 * 60 * 1000));

        return hour;
    }

    /**
     * 返回给定时间距离当前时间的分钟数
     *
     * @param
     * @return
     */
    public static int getMinDistance(long time) {
        long curTime = System.currentTimeMillis();
        int min = (int) ((curTime - time) * 1.0 / (60 * 1000));

        return min;
    }


    /**
     * 返回距离当前时间多少天（不够一天显示小时，不够一小时显示分）
     */
    public static String getTime(long temp) {
        if (daysBetween(temp) == 0) {
            if (getHourDistance(temp) == 0) {
                int mins = getMinDistance(temp);
                if (mins >= 1) {
                    return getMinDistance(temp) + "分钟前";
                } else {
                    return "刚刚";
                }

            } else {
                return getHourDistance(temp) + "小时前";
            }
        } else {
            return daysBetween(temp) + "天前";
        }
    }

    /**
     * 将时间字符串转化为Date类型
     *
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

}

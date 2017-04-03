package com.fatalsignal.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtil {
    private static final String DEFAULT_DATE_PATTERN = "yyyy.MM.dd";

    public static String getFriendlyTimeStr(long ts) {

        String var1 = null;
        String var2 = Locale.getDefault().getLanguage();

        long todayStart = trimTime(System.currentTimeMillis());
        long yestodayStart = trimTime(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        long tsStart = trimTime(ts);

        if (todayStart == tsStart) {
            var1 = "HH:mm";

        } else if (yestodayStart == tsStart) {

            var1 = "昨天 HH:mm";
        } else {
            var1 = "yyyy-MM-dd HH:mm";
        }

        return (new SimpleDateFormat(var1, Locale.ENGLISH)).format(ts);

    }


    public static String getFormatedDate(String ts, String fmt) {
        try {
            long tslong = Long.valueOf(ts);
            if (tslong < 0) {
                return ts;
            }
            return getFormatedDate(tslong, fmt);
        } catch (Exception e) {
        }
        return ts;
    }

    public static String getFormatedDate(long ts) {
        return getFormatedDate(ts, DEFAULT_DATE_PATTERN);
    }

    public static String getFormatedDate(long ts, String fmtStr) {
//            if (ts < 1000000000000L) {
//                ts = ts * 1000;
//            }
        if (fmtStr == null || fmtStr.isEmpty()) {
            fmtStr = DEFAULT_DATE_PATTERN;
        }
        Date date = new Date(ts);
        DateFormat format = new SimpleDateFormat(
                fmtStr);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(date);
    }

    public static String getRelativeTimeStr(String dateStr) {
        String str = "刚刚";
        DateFormat format = new SimpleDateFormat("yyy-mm-dd hh:mm");
        ;
        try {
            Date date = format.parse(dateStr);

            long millsec = System.currentTimeMillis() - date.getTime();

            long years = millsec / (1000 * 60 * 60 * 24);
            years = years / 365;
            if (years > 0) {
                return years + "年前";
            }

            long days = millsec / (1000 * 60 * 60 * 24);

            if (days > 0) {
                return days + "天前";
            }
            long hours = millsec / (1000 * 60 * 60);
            if (hours > 0) {
                return days + "小时前";
            }
            return "刚刚";

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return str;
    }

    public static String getInterval(Date createAt) {
        // 定义最终返回的结果字符串。
        String interval = null;

        long millisecond = new Date().getTime() - createAt.getTime();

        long second = millisecond / 1000;

        if (second <= 0) {
            second = 0;
        }
        //*--------------微博体（标准）
        if (second == 0) {
            interval = "刚刚";
        } else if (second < 30) {
            interval = second + "秒以前";
        } else if (second >= 30 && second < 60) {
            interval = "半分钟前";
        } else if (second >= 60 && second < 60 * 60) {//大于1分钟 小于1小时
            long minute = second / 60;
            interval = minute + "分钟前";
        } else if (second >= 60 * 60 && second < 60 * 60 * 24) {//大于1小时 小于24小时
            long hour = (second / 60) / 60;
            interval = hour + "小时前";
            /*if (hour <= 3) {
            } else {
                interval = "今天" + getFormatTime(createAt, "HH:mm");
            }*/
        } else if (second >= 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {//大于1D 小于2D
            interval = "昨天" + getFormatTime(createAt, "HH:mm");
        } else if (second >= 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 30) {//大于2D小时 小于 7天
            long day = ((second / 60) / 60) / 24;
            interval = day + "天前";
        } else if (second <= 60 * 60 * 24 * 365 && second >= 60 * 60 * 24 * 30) {//大于7天小于365天
            interval = getFormatTime(createAt, "MM-dd HH:mm");
        } else if (second >= 60 * 60 * 24 * 365) {//大于365天
            interval = getFormatTime(createAt, "yyyy-MM-dd HH:mm");
        } else {
            interval = "0";
        }
        return interval;
    }

    public static String getFormatTime(Date date, String Sdf) {
        return (new SimpleDateFormat(Sdf)).format(date);
    }

    public static void main(String[] args) {
        try {
            System.out.println(getInterval(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-03 12:12:12")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String getRelaStrStamp(String timeStamp) {
        try {
            return getRelaStrStamp(Long.valueOf(timeStamp));
        } catch (Exception e) {
            return timeStamp;
        }
    }

    public static String getRelaStrStamp(long timeStamp) {
        Date date = new Date(timeStamp);
        return getInterval(date);
    }


    public static long trimTime(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long trimTime(long ts) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTimeInMillis(ts);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long trimDay(Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
}

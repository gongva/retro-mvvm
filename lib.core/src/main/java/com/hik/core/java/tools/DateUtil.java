package com.hik.core.java.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间相关工具库
 *
 * @author gongwei
 * @time 2019/10/12
 * @mail shmily__vivi@163.com
 */
public class DateUtil {

    private static final long ONE_SECOND = 1000L;
    private static final long ONE_MINUTE = 60 * ONE_SECOND;
    private static final long ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;
    private static final long TWO_DAY = 2 * ONE_DAY;
    private static final long ONE_WEEK = 7 * ONE_DAY;

    public static String[] DAY_OF_WEEK = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    public DateUtil() {
    }

    /**
     * 获取当前系统时间
     *
     * @return timestamp
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前系统时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return df.format(newDate());
    }

    /**
     * 根据Date获取星期几
     *
     * @param date
     * @return "周一", "周二", "周三", "周四", "周五", "周六", "周日"
     */
    public static String getWeekString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DAY_OF_WEEK[calendar.get(7) - 1];
    }

    /**
     * 格式化Date为 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return df.format(date);
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss字符串转换为Date
     *
     * @param datetime
     * @return Date
     */
    public static Date parseDateTime(String datetime) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return df.parse(datetime);
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * 格式化Date为日期 yyyy-MM-dd
     *
     * @param date
     * @return yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return df.format(date);
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * 将yyyy-MM-dd字符串转换为Date
     *
     * @param date
     * @return Date
     */
    public static Date parseDate(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return df.parse(date);
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * 格式化Date为时间 HH:mm:ss
     *
     * @param date
     * @return HH:mm:ss
     */
    public static String formatTime(Date date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            return df.format(date);
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * 将HH:mm:ss字符串转换为Date
     *
     * @param time
     * @return Date
     */
    public static Date parseTime(String time) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            return df.parse(time);
        } catch (Exception var2) {
            return null;
        }
    }

    /**
     * 格式化Date为自定义格式
     *
     * @param date
     * @param pattern
     * @return pattern格式String
     */
    public static String format(Date date, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return format.format(date);
        } catch (Exception var3) {
            return null;
        }
    }

    /**
     * 格式化timestamp为自定义格式
     *
     * @param timestamp
     * @param pattern
     * @return pattern格式String
     */
    public static String format(long timestamp, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
            return format.format(newDate(timestamp));
        } catch (Exception var3) {
            return null;
        }
    }

    /**
     * 将date字符串转换为自定义格式
     *
     * @param date
     * @param pattern
     * @return Date
     */
    public static Date parse(String date, String pattern) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            df.applyPattern(pattern);
            return df.parse(date);
        } catch (Exception var3) {
            return null;
        }
    }

    /**
     * 获取当前Date
     *
     * @return Date
     */
    public static Date newDate() {
        return new Date();
    }

    /**
     * 转换timestamp为Date
     *
     * @param timestamp
     * @return Date
     */
    public static Date newDate(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 转换year, month, dayOfMonth, hourOfDay, minute, second为Date
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hourOfDay
     * @param minute
     * @param second
     * @return Date
     */
    public static Date newDate(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month);
        cal.set(5, dayOfMonth);
        cal.set(11, hourOfDay);
        cal.set(12, minute);
        cal.set(13, second);
        return cal.getTime();
    }

    /**
     * 获取year month dayOfMonth对应的Date
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @return Date
     */
    public static Date newDate(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month);
        cal.set(5, dayOfMonth);
        return cal.getTime();
    }

    /**
     * 从Date中获取年份
     *
     * @param date
     * @return year
     */
    public static int getYear(Date date) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(date);
        return now.get(1);
    }

    /**
     * 从Date中获取月份
     *
     * @param date
     * @return month（真实年份，非month下标）
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal.get(2) + 1;
    }

    /**
     * 从Date中获取日
     *
     * @param date
     * @return dayOfMonth
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal.get(5);
    }

    /**
     * 从Date中获取时
     *
     * @param date
     * @return hour
     */
    public static int getHour(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal.get(11);
    }

    /**
     * 从Date中获取分
     *
     * @param date
     * @return minute
     */
    public static int getMinute(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal.get(12);
    }

    /**
     * 从Date中获取秒
     *
     * @param date
     * @return second
     */
    public static int getSecond(Date date) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        return cal.get(13);
    }

    /**
     * 在date基础上增加year, month, dayOfMonth
     *
     * @param date
     * @param year
     * @param month
     * @param dayOfMonth
     * @return Date
     */
    public static Date add(Date date, int year, int month, int dayOfMonth) {
        return add(date, year, month, dayOfMonth, 0, 0, 0);
    }

    /**
     * 在date基础上增加year, month, dayOfMonth, hourOfDay, minute,  second
     *
     * @param date
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hourOfDay
     * @param minute
     * @param second
     * @return Date
     */
    public static Date add(Date date, int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(1, year);
        cal.add(2, month);
        cal.add(5, dayOfMonth);
        cal.add(11, hourOfDay);
        cal.add(12, minute);
        cal.add(13, second);
        return cal.getTime();
    }

    /**
     * 判断src是否在dst之前
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /**
     * 判断src是否在dst之后
     *
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    /**
     * 判断src是否在beginDate与endDate之间
     *
     * @param beginDate
     * @param endDate
     * @param src
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }

    /**
     * 计算beginDate与endDate相差天数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static long diffDate(Date beginDate, Date endDate) {
        return (beginDate.getTime() - endDate.getTime()) / 86400000L;
    }

    /**
     * 格式化“秒”为“小时:分钟:秒”
     * 如：
     * 12:08:12
     * 00:12:12
     * 00:00:12
     *
     * @param seconds 单位：S
     * @return x小时x分钟x秒
     */
    public static String toTimeString(int seconds) {
        long hours = (long) (seconds / 3600);
        long remainder = (long) (seconds % 3600);
        long minutes = remainder / 60L;
        long secs = remainder % 60L;
        return (hours < 10L ? "0" : "") + hours + ":" + (minutes < 10L ? "0" : "") + minutes + ":" + (secs < 10L ? "0" : "") + secs;
    }

    /**
     * 格式化“秒”为“x小时x分钟x秒”
     * 如：
     * 12秒
     * 12分钟12秒
     * 12小时12分钟12秒
     *
     * @param seconds 单位：S
     * @return x小时x分钟x秒
     */
    public static String parseDurationString(int seconds) {
        if (seconds == 0) {
            return "";
        } else if (seconds < 60) {
            return seconds + "秒";
        } else if (seconds < 60 * 60) {
            return seconds / 60 + "分钟";
        } else {
            String s = (seconds / 60 / 60) + "小时";
            if (seconds / 60 % 60 > 0) {
                s += seconds / 60 % 60 + "分钟";
            }
            if (seconds % 60 > 0) {
                s += seconds % 60 + "秒";
            }
            return s;
        }
    }

    /**
     * 友好的时间显示规则:
     * 1：分钟内   呈现“刚刚”
     * 2：1小时内  呈现“XX分钟前”
     * 3：今天内   呈现“XX：XX”
     * 4：昨天     呈现“昨天XX：XX”
     * 5：今年     呈现“09-15 XX：XX”
     * 6：今年以前  呈现“2018-09-15 XX：XX
     */
    public static String formatAmicable(long timestamp) {
        long delta = getCurrentTimeMillis() - timestamp;
        Date oldDate = new Date(timestamp);
        Date currentDate = new Date();
        if (delta < 1 * ONE_MINUTE) {
            return "刚刚";
        } else if (delta < ONE_HOUR) {
            return delta / ONE_MINUTE + "分钟前";
        } else if (delta < ONE_DAY && currentDate.getDay() == oldDate.getDay()) {
            return format(timestamp, "HH:mm");
        } else if (delta < TWO_DAY) {
            if (new Date(System.currentTimeMillis() - ONE_DAY).getDay() == oldDate.getDay()) {
                return format(timestamp - ONE_DAY, "昨天HH:mm");
            } else {
                return format(timestamp, "MM-dd HH:mm");
            }
        } else if (oldDate.getYear() == currentDate.getYear()) {
            return format(timestamp, "MM-dd HH:mm");
        } else {
            return format(timestamp, "yyyy-MM-dd HH:mm");
        }
    }
}

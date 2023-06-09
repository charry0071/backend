package com.xc.utils;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DateTimeUtil {
    private static final Logger log = LoggerFactory.getLogger(DateTimeUtil.class);


    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static final String YMD_FORMAT = "yyyy-MM-dd";


    public static final String HM_FORMAT = "HH:mm";
    private static final LocalDate[] HOLIDAYS = {
            LocalDate.of(2023, 1, 1),  // 元旦
            LocalDate.of(2023, 2, 10), // 春节
            LocalDate.of(2023, 4, 30), // 解放节
            LocalDate.of(2023, 5, 1),  // 国际劳动节
            LocalDate.of(2023, 9, 2),  // 国庆节
            LocalDate.of(2023, 9, 3),  // 国庆节调休
            LocalDate.of(2023, 9, 4)   // 国庆节调休
    };
    /**
     * 判断股票是否在 T+2.5 日内平仓
     *
     * @param tradeDate 购买股票的日期
     * @return 如果当前日期大于等于交易日期加上 2.5 个交易日，并且没有遇到节假日和周末，则返回 true；否则返回 false。
     */
    public static boolean isSellable(LocalDateTime tradeDate) {
        // 计算两个日期之间的天数差
        LocalDateTime now = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(tradeDate, now) + 1;
        int weekend = 0;
        LocalDateTime currentDate = tradeDate; // 当前日期从开始日期开始
        for (int i = 0; i < days; i++) {
            if(isHolidayOrWeekend(currentDate)){
                weekend++;
            }
            currentDate = currentDate.plusDays(1); // 日期加一天，继续循环
        }
        // 获取小时差
        long diffInHours = ChronoUnit.HOURS.between(tradeDate, now);
        log.info("开始时间"+tradeDate);
        log.info("结束时间"+now);
        log.info("时差"+diffInHours);
        log.info("周末时间"+weekend * 24);
        log.info("持仓时间"+ (diffInHours - (weekend * 24)));
        return diffInHours - (weekend * 24) >= 48;
    }

    /**
     * 判断日期是否是节假日或周末
     *
     * @param date 要判断的日期
     * @return 如果是节假日或周末，则返回 true；否则返回 false。
     */
    private static boolean isHolidayOrWeekend(LocalDateTime date) {
        // 判断是否是周末
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return true;
        }

        // 判断是否是法定节假日
        for (LocalDate holiday : HOLIDAYS) {
            if (date.equals(holiday)) {
                return true;
            }
        }

        return false;
    }

    // 判断是否为工作日（周一至周五）
    private static boolean isWeekday(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return !dayOfWeek.equals(DayOfWeek.SATURDAY) && !dayOfWeek.equals(DayOfWeek.SUNDAY);
    }
    // 判断日期是否为节假日（这里可以根据具体需求自行实现）
    private static boolean isHoliday(LocalDate date) {
        for (LocalDate holiday : HOLIDAYS) {
            if (date.equals(holiday)) {
                return true;
            }
        }
        return false;
    }

    public static Date getCurrentDate() {
        return new Date();
    }


    public static Date strToDate(String dateTimeStr, String formatStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }


    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }

    public static Date strToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String time){
        String stamp = "";
        if (!"".equals(time)) {//时间不为空
            try {
                String res;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = simpleDateFormat.parse(time);
                long ts = date.getTime();
                res = String.valueOf(ts);
                return res;
            } catch (Exception e) {
                System.out.println("参数为空！");
            }
        }else {    //时间为空
            long current_time = System.currentTimeMillis();  //获取当前时间
            stamp = String.valueOf(current_time/1000);
        }
        return stamp;
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*获取当前时间戳*/
    public static String getStampNow() {
        Long startTs = System.currentTimeMillis(); // 当前时间戳
        return  startTs.toString();
    }


    public static Timestamp searchStrToTimestamp(String dateTimeStr) {
        return Timestamp.valueOf(dateTimeStr);
    }


    public static String dateToStr(Date date) {
        if (date == null) {
            return "";
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }


    public static Date longToDate(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String d = format.format(time);

        Date date = null;
        try {
            date = format.parse(d);
        } catch (Exception e) {
            log.error("datetime utils longToDate error");
        }
        return date;
    }


    public static Date doEndTime(Date begintime, int month) {
        Long begintimelong = Long.valueOf(begintime.getTime() / 1000L);
        log.info("计算时间 传入时间 = {} , 时间戳 = {}", dateToStr(begintime), begintimelong);

        Long endtimelong = Long.valueOf(begintimelong.longValue() + (2592000 * month));
        Date endtimedate = longToDate(Long.valueOf(endtimelong.longValue() * 1000L));

        log.info("endtime 时间戳 = {},时间 = {} , 格式化时间={}", new Object[]{endtimelong, endtimedate,
                dateToStr(endtimedate)});

        return endtimedate;
    }


    public static String getCurrentTimeMiao() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }


    public static Date parseToDateByMinute(int minuteTimes) {
        Date nowDate = new Date();
        Long nowtimes = Long.valueOf(nowDate.getTime() / 1000L);

        Long beginTimesLong = Long.valueOf(nowtimes.longValue() - (minuteTimes * 60));
        return longToDate(Long.valueOf(beginTimesLong.longValue() * 1000L));
    }


    public static boolean isCanSell(Date buyDate, int maxMinutes) {
        Long buyDateTimes = Long.valueOf(buyDate.getTime() / 1000L);

        buyDateTimes = Long.valueOf(buyDateTimes.longValue() + (maxMinutes * 60));

        Long nowDateTimes = Long.valueOf((new Date()).getTime() / 1000L);

        if (nowDateTimes.longValue() > buyDateTimes.longValue()) {
            return true;
        }
        return false;
    }

    /*日期年月日是否相同*/
    public static boolean sameDate(Date d1, Date d2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        //fmt.setTimeZone(new TimeZone()); // 如果需要设置时间区域，可以在这里设置
        return fmt.format(d1).equals(fmt.format(d2));
    }

        /**
         * 【参考】https://www.cnblogs.com/zhaoKeju-bokeyuan/p/12125711.html
         * 基于指定日期增加天数
         * @param date
         * @param num 整数往后推，负数往前移
         * @return
         */
    public static Date addDay(Date date,int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, num);
        return cal.getTime();
    }


    public static void main(String[] args) {
        Date date = new Date();
        //LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime localDateTime = LocalDateTime.of(2023, 5, 22, 14, 58);
        boolean result = isSellable(localDateTime);
        log.info(""+result);
    }
}

package com.tools.commonservice.util;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;

public class LocalDateUtil {

    private static final String[] week = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

    /**
     * 将时间戳转为LocalDate
     */
    public static LocalDate getLocalDate(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(DEFAULT_ZONE).toLocalDate();
    }

    /**
     * 指定时区, 将时间戳转为LocalDate
     */
    public static LocalDate getLocalDate(long timestamp, ZoneId zoneId) {
        return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDate();
    }

    /**
     * @return 返回两个日期之间相差的天数
     */
    public static long between(LocalDate date1, LocalDate date2) {
        return date1.toEpochDay() - date2.toEpochDay();
    }

    public static ZoneId getDefaultZone() {
        return DEFAULT_ZONE;
    }

    public static String getChineseWeedDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return week[dayOfWeek.getValue() - 1];
    }


    public void sort(int[][] arr) {
        Arrays.sort(arr, Comparator.comparingInt((int[] o1) -> o1[1]));
    }


}

package com.mountain.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @Auther kejiefu
 * @Date 2018/5/17 0017
 */
public class DateUtils {

    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime  localDateTime
     * @return date
     */
    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 将Date转换成LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 通过时间秒数转换成时间
     *
     * @param time seconds
     * @return date
     */
    public static Date convertDateBySeconds(Integer time) {
        Long current = time.longValue() * 1000;
        return new Date(current);
    }

    /**
     * 通过毫秒数获取秒数
     *
     * @param time  millisecond
     * @return seconds
     */
    public static Integer convertMillisToSeconds(long time) {
        return Integer.valueOf(String.valueOf(time / 1000));
    }

    /**
     * 获取当前的秒数
     *
     * @return seconds
     */
    public static Integer getCurrentTimeSeconds() {
        return Integer.valueOf(String.valueOf(System.currentTimeMillis() / 1000));
    }


}

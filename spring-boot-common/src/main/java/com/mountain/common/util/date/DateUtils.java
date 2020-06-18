package com.mountain.common.util.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * @Auther kejiefu
 * @Date 2018/5/17 0017
 */
public class DateUtils {

    /**
     * 将LocalDateTime转换成Date
     *
     * @param localDateTime localDateTime
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
     * @param time millisecond
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

    // ------------------------------------ Format start ----------------------------------------------

    /**
     * 格式化日期时间<br>
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param localDateTime 被格式化的日期
     * @return 格式化后的字符串
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        return format(localDateTime, DatePattern.NORMAL_DATETIME_PATTERN);
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param localDateTime 被格式化的日期
     * @param format        日期格式，常用格式见： {@link DatePattern}
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime localDateTime, String format) {
        if (null == localDateTime) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(df);
    }

    // ------------------------------------ Format end ----------------------------------------------

    // ------------------------------------ Parse start ----------------------------------------------

    /**
     * 构建LocalDateTime对象<br>
     * 格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateStr 时间字符串（带格式）
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseLocalDateTime(CharSequence dateStr) {
        return parseLocalDateTime(dateStr, DatePattern.NORMAL_DATETIME_PATTERN);
    }

    /**
     * 构建LocalDateTime对象
     *
     * @param dateStr 时间字符串（带格式）
     * @param format  使用{@link DatePattern}定义的格式
     * @return LocalDateTime对象
     */
    public static LocalDateTime parseLocalDateTime(CharSequence dateStr, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        try {
            return LocalDateTime.parse(dateStr, df);
        } catch (DateTimeParseException e) {
            // 在给定日期字符串没有时间部分时，LocalDateTime会报错，此时使用LocalDate中转转换
            return LocalDate.parse(dateStr, df).atStartOfDay();
        }
    }

    // ------------------------------------ Parse end ----------------------------------------------

}

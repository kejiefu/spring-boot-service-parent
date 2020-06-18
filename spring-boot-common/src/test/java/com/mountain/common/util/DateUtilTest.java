package com.mountain.common.util;

import com.mountain.common.util.date.DateUtils;

import java.time.LocalDateTime;

/**
 * @Auther kejiefu
 * @Date 2018/9/20 0020
 */

public class DateUtilTest {

    public static void main(String[] args) {
        System.out.println(DateUtils.getCurrentTimeSeconds());
        System.out.println(DateUtils.convertMillisToSeconds(DateUtils.convertLocalDateTimeToDate(LocalDateTime.now().plusDays(5)).getTime()));
        System.out.println(DateUtils.formatLocalDateTime(LocalDateTime.now()));
        System.out.println(DateUtils.format(LocalDateTime.now(),"yyyy-MM-dd"));
        System.out.println(DateUtils.parseLocalDateTime("2017-09-28 17:07:05"));
        System.out.println(DateUtils.parseLocalDateTime("2017-09-28","yyyy-MM-dd"));
    }

}

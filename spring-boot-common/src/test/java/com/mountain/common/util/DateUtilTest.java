package com.mountain.common.util;

import java.time.LocalDateTime;

/**
 * @Auther kejiefu
 * @Date 2018/9/20 0020
 */

public class DateUtilTest {

    public static void main(String[] args) {
        System.out.println(DateUtils.getCurrentTimeSeconds());
        System.out.println(DateUtils.convertMillisToSeconds(DateUtils.convertLocalDateTimeToDate(LocalDateTime.now().plusDays(5)).getTime()));
    }

}

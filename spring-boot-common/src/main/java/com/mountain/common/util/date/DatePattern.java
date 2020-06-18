package com.mountain.common.util.date;

import java.util.regex.Pattern;

/**
 * 日期格式化类，提供常用的日期格式化对象
 */
public class DatePattern {

    /**
     * 标准日期时间正则，每个字段支持单个数字或2个数字，包括：
     * <pre>
     *     yyyy-MM-dd HH:mm:ss.SSS
     *     yyyy-MM-dd HH:mm:ss
     *     yyyy-MM-dd HH:mm
     *     yyyy-MM-dd
     * </pre>
     *
     * @since 5.3.6
     */
    public static final Pattern REGEX_NORMAL = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}(\\s\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?(.\\d{1,3})?");

    //-------------------------------------------------------------------------------------------------------------------------------- NORMALal
    /**
     * 标准日期格式：yyyy-MM-dd
     */
    public static final String NORMAL_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 标准时间格式：HH:mm:ss
     */
    public static final String NORMAL_TIME_PATTERN = "HH:mm:ss";

    /**
     * 标准日期时间格式，精确到分：yyyy-MM-dd HH:mm
     */
    public static final String NORMAL_DATETIME_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * 标准日期时间格式，精确到秒：yyyy-MM-dd HH:mm:ss
     */
    protected static final String NORMAL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 标准日期时间格式，精确到毫秒：yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String NORMAL_DATETIME_MS_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

}

package com.mountain.common.util;

/**
 * @Auther kejiefu
 * @Date 2018/11/19 0019
 */

public class SnowflakeIdWorkerTest {

    //==============================Test=============================================
    /** 测试 */
    public static void main(String[] args) {
        SnowflakeIdUtils idWorker = new SnowflakeIdUtils(0, 0);
        for (int i = 0; i < 1000; i++) {
            long id = idWorker.nextId();
            System.out.println(Long.toBinaryString(id));
            System.out.println(id);
        }
    }

}

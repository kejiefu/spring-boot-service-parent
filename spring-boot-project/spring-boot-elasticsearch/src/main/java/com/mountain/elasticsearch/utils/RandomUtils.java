package com.mountain.elasticsearch.utils;

import java.util.Random;

/**
 * @Auther kejiefu
 * @Date 2019/8/29 0029
 */
public class RandomUtils {

    /**
     * 生成随机数
     * 8位数生成重复随机的概率是1/2821109907456
     *
     * @param word 要生成几位数的字母
     * @return String
     */
    public static String genRandomString(Integer word) {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer stringBuffer = new StringBuffer("");
        Random r = new Random();
        while (count < word) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                stringBuffer.append(str[i]);
                count++;
            }
        }
        return stringBuffer.toString();
    }

}

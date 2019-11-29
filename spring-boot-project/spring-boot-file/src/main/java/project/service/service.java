package project.service;

import project.face.LimitUtil;

/**
 * @Auther kejiefu
 * @Date 2019/5/31 0031
 */

public class service {

    @LimitUtil(methodName = "bbbb")
    public static void aa(){

    }

    public static void main(String[] args) {
aa();
    }

}

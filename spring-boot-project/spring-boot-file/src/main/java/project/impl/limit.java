package project.impl;

import project.face.LimitUtil;

/**
 * @Auther kejiefu
 * @Date 2019/5/31 0031
 */
public abstract class limit implements LimitUtil {

    @Override
    public String methodName() {
        System.out.println("33333");
        return "33";
    }
}

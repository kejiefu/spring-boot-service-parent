package com.mountain.project.netty.test.netty.rpc;

/**
 * 实现类
 */
public class HelloServiceImpl implements HelloService {
  @Override
  public String hello(String msg) {
    return msg != null ? msg + " -----> I am fine." : "I am fine.";
  }
}
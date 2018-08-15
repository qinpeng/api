package com.csci.demo.utils;

import java.util.Random;

public class StringUtils {

  //获取指定长度随机字符串
  public static String getRandomStr(int length) {
    if (length == 0) {
      return null;
    }
    String src = "abcdefghijklmnopqrstuvwxyz!@#$%&1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] chars = new char[length];
    for (int i = 0; i < length; i++) {
      chars[i] = src.charAt(new Random().nextInt(src.length()));
    }
    return new String(chars);
  }

}

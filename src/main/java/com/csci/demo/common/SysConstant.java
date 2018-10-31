package com.csci.demo.common;

import okhttp3.MediaType;

public class SysConstant {

  public static final String UTF8 = "UTF-8";
  public static final MediaType JSON
      = MediaType.parse("application/json; charset=utf-8");

  public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
}

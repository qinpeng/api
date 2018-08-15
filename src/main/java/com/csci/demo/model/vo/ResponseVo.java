package com.csci.demo.model.vo;

public class ResponseVo {

  private String code;
  private String message;
  private String data;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ResponseVo{" +
        "code='" + code + '\'' +
        ", message='" + message + '\'' +
        ", data='" + data + '\'' +
        '}';
  }
}

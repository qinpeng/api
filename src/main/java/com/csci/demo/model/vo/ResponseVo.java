package com.csci.demo.model.vo;

public class ResponseVo {

  private String code;
  private String message;
  private String respData;
  private String signature;

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

  public String getRespData() {
    return respData;
  }

  public void setRespData(String respData) {
    this.respData = respData;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  @Override
  public String toString() {
    return "ResponseVo{" +
        "code='" + code + '\'' +
        ", message='" + message + '\'' +
        ", respData='" + respData + '\'' +
        ", signature='" + signature + '\'' +
        '}';
  }
}

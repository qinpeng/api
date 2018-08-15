package com.csci.demo.client;

import com.csci.demo.model.vo.ResponseVo;
import com.csci.demo.utils.DesUtils;
import com.csci.demo.utils.HttpUtil;
import com.csci.demo.utils.JsonUtil;
import com.csci.demo.utils.RSAUtils;
import com.csci.demo.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 程序入口
 * 对应接口文档：
 */
public class TestPostClient extends BaseClient {

  public static void main(String[] args) throws Exception {
    //请求uri
    String uri = "/api/v1/demo/test/post";
    //业务数据
    RequestDto requestDto = new RequestDto("user1234",
        "1100000", "6222081991087490987");
    String requestDataJson = JsonUtil.toJsonStr(requestDto);
    //获取随机字符串，作为DES的加密秘钥，字符串长度建议不小于20
    String nonce = StringUtils.getRandomStr(20);
    //DES加密业务数据
    String encryptRequestBody = DesUtils.encrypt(requestDataJson, nonce);
    //RSA加密nonce
    String encryptedNonce = RSAUtils.encryptByPublicKey(nonce, ZZ_PUBLIC_KEY);

    //获取待加签数据
    String signatureSrcData = BaseClient
        .createSignatureSrcData(uri, SEPARATOR, null, encryptRequestBody);
    System.out.println("signatureSrcData = " + signatureSrcData);
    //加签
    String signature = RSAUtils.sign(signatureSrcData, PRIVATE_KEY);

    //发送请求
    String url = BASE_PATH + uri;
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", CONTENT_TYPE);
    headerMap.put("channelCode", CHANNEL_CODE);
    headerMap.put("signature", signature);
    headerMap.put("nonce", encryptedNonce);
    System.out.println("signature = " + signature);
    String response = HttpUtil.post(url, encryptRequestBody, CONTENT_TYPE, null, headerMap);
    //解析返回结果【包括验签与解密】
    ResponseVo responseVo = parseResponse(response);
    System.out.println("返回结果：" + responseVo);
  }

  public static class RequestDto {

    private String userId;
    private String loanAppId;
    private String bankNum;

    public RequestDto(String userId, String loanAppId, String bankNum) {
      this.userId = userId;
      this.loanAppId = loanAppId;
      this.bankNum = bankNum;
    }

    public String getUserId() {
      return userId;
    }

    public String getLoanAppId() {
      return loanAppId;
    }

    public String getBankNum() {
      return bankNum;
    }
  }

}

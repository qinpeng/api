package com.csci.demo.client;

import com.csci.demo.model.vo.ResponseVo;
import com.csci.demo.utils.HttpUtil;
import com.csci.demo.utils.RSAUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * 程序入口
 * 对应接口文档：
 */
public class TestGetClient extends BaseClient {

  public static void main(String[] args) throws Exception {
    //请求uri
    String uri = "/api/v1/demo/test/get";

    //url参数
    Map<String, String> urlParamMap = new HashMap<>();
    urlParamMap.put("loanAppId", "order12345");
    urlParamMap.put("extendUserId", "user12345");

    //组装待加签数据
    String signatureSrcData = createSignatureSrcData(uri, SEPARATOR, urlParamMap, null);
    System.out.println("signatureSrcData = " + signatureSrcData);

    //加签
    String signature = RSAUtils.sign(signatureSrcData, PRIVATE_KEY);

    //组装request header
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("Content-Type", CONTENT_TYPE);
    headerMap.put("channelCode", CHANNEL_CODE);
    headerMap.put("signature", signature);
    System.out.println("signature = " + signature);

    //发送请求
    String url = BASE_PATH + uri;
    String response = HttpUtil.get(url, urlParamMap, headerMap);
    //解析返回结果【解密data节点】
    ResponseVo responseVo = parseResponse(response);
    System.out.println("返回结果：" + responseVo);
  }

}

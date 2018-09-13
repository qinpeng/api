package com.csci.demo.client;

import com.csci.demo.model.vo.ResponseVo;
import com.csci.demo.utils.DesUtils;
import com.csci.demo.utils.HttpUtil;
import com.csci.demo.utils.JsonUtil;
import com.csci.demo.utils.RSAUtils;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.HttpMethod;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 中证HttpClient.
 */
public class CsciClient {

  /**
   * 发送请求前组装加签的原始数据.
   *
   * @param uri requestURI
   * @param separator 分隔符，双方约定一致
   * @param urlParamMap url查询参数
   * @param requestBody 请求报文
   * @return 加签原始数据
   */
  private static String createSignatureSrcData(String uri, String separator,
      Map<String, ?> urlParamMap,
      String requestBody) {
    return new StringBuffer(uri)
        .append(HttpUtil.createOrderedUrlParamFromMap(urlParamMap))
        .append(separator)
        .append(requestBody == null ? "" : requestBody)
        .toString();
  }

  //解析返回结果，并解密业务数据
  private static ResponseVo parseResponse(String httpResponse, String privateKey) throws Exception {
    ResponseVo responseVo = JsonUtil.json2Object(httpResponse, ResponseVo.class);
    //解密业务数据
    String respData = responseVo.getData();
    if (respData != null) {
      String decryptRespData = RSAUtils.decryptByPrivateKey(respData, privateKey);
      responseVo.setData(decryptRespData);
    }
    return responseVo;
  }

  /**
   * 发送post请求. Content-Type : application/json .
   *
   * @param basePath 中证域名
   * @param uri 资源路径
   * @param requestBodyJson request body， 加密前的明文，json字符串
   * @param channelCode 渠道编号，中证提供
   * @param privateKey 本地的RSA私钥【要求长度2048位】
   * @param zzPublicKey 中证的RSA公钥
   * @param separator 分割符，中证提供
   */
  public static ResponseVo postJson(String basePath, String uri, String requestBodyJson,
      String channelCode, String privateKey, String zzPublicKey, String separator)
      throws Exception {
    return executeJson(HttpMethod.POST,
        basePath,
        uri,
        requestBodyJson,
        channelCode,
        privateKey,
        zzPublicKey,
        separator);
  }

  /**
   * 发送post请求. Content-Type : application/json .
   *
   * @param basePath 中证域名
   * @param uri 资源路径
   * @param requestBodyJson request body， 加密前的明文，json字符串
   * @param channelCode 渠道编号，中证提供
   * @param privateKey 本地的RSA私钥【要求长度2048位】
   * @param zzPublicKey 中证的RSA公钥
   * @param separator 分割符，中证提供
   */
  public static ResponseVo executeJson(String  httpMethod,String basePath, String uri, String requestBodyJson,
      String channelCode, String privateKey, String zzPublicKey, String separator)
      throws Exception {
    String encryptRequestBody = null;
    String encryptedNonce = null;
    if(StringUtils.isNotEmpty(requestBodyJson)) {
      //获取随机字符串，作为DES的加密秘钥，字符串长度建议不小于20
      String nonce = RandomStringUtils.randomAlphabetic(20);
      //DES加密业务数据
      encryptRequestBody = DesUtils.encrypt(requestBodyJson, nonce);
      //RSA加密nonce
      encryptedNonce = RSAUtils.encryptByPublicKey(nonce, zzPublicKey);
    }


    //构建待加签数据
    String signatureSrcData = CsciClient
        .createSignatureSrcData(uri, separator, null, encryptRequestBody);
    System.out.println("signatureSrcData = " + signatureSrcData);

    //RSA私钥加签
    String signature = RSAUtils.sign(signatureSrcData, privateKey);

    //发送请求
    final String url = basePath + uri;
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("channelCode", channelCode);
    headerMap.put("signature", signature);
    if(null != encryptedNonce) {
      headerMap.put("nonce", encryptedNonce);
    }
    System.out.println("signature = " + signature);
    String response = HttpUtil.execute(httpMethod,url, encryptRequestBody, null, headerMap);

    //解析返回结果【解密data节点】
    ResponseVo responseVo = parseResponse(response, privateKey);
    return responseVo;
  }
  /**
   * 发送post请求. Content-Type : application/json .
   *
   * @param basePath 中证域名
   * @param uri 资源路径
   * @param requestBodyJson request body， 加密前的明文，json字符串
   * @param channelCode 渠道编号，中证提供
   * @param privateKey 本地的RSA私钥【要求长度2048位】
   * @param zzPublicKey 中证的RSA公钥
   * @param separator 分割符，中证提供
   */
  public static ResponseVo putJson(String basePath, String uri, String requestBodyJson,
      String channelCode, String privateKey, String zzPublicKey, String separator)
      throws Exception {
    return executeJson(HttpMethod.PUT,
        basePath,
        uri,
        requestBodyJson,
        channelCode,
        privateKey,
        zzPublicKey,
        separator);
  }

  /**
   * 发送Get请求.
   *
   * @param basePath 中证域名
   * @param uri 资源路径
   * @param urlParamMap url参数
   * @param channelCode 渠道编号，中证提供
   * @param privateKey 本地的RSA私钥【要求长度2048位】
   * @param separator 分割符，中证提供
   */
  public static ResponseVo get(String basePath, String uri, Map<String, String> urlParamMap,
      String channelCode, String privateKey, String separator)
      throws Exception {

    return executeJson(HttpMethod.GET,
        basePath,
        uri,
        null,
        channelCode,
        privateKey,
        null,
        separator);
  }


}
package com.csci.demo.client;

import com.csci.demo.common.SysConstant;
import com.csci.demo.model.vo.ResponseVo;
import com.csci.demo.utils.DesUtils;
import com.csci.demo.utils.JsonUtil;
import com.csci.demo.utils.RSAUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ben
 * benkris1@126.com
 */
public class HerculesClient {

  private static final Logger logger = LoggerFactory.getLogger(HerculesClient.class);
  public  OkHttpClient okHttpClient;

  public static final String DEFAULT_SEPARATOR = "@";
  public static final long DEFAULT_READ_TIMEOUT = 30 * 1000;
  public static final long DEFAULT_WRITE_TIMEOUT = 30 * 1000;
  public static final long DEFAULT_CONNECT_TIMEOUT = 5 * 1000;

  private String channelCode;
  private String remoteRsaPubKey;
  private String localRsaPrvKey;
  private String separator;
  private String basePath;

  public HerculesClient(String channelCode, String remoteRsaPubKey, String localRsaPrvKey,
      String separator, String basePath, Long readTimeout,
      Long writeTimeout, Long connectTimeout) {
    this.channelCode = channelCode;
    this.remoteRsaPubKey = remoteRsaPubKey;
    this.localRsaPrvKey = localRsaPrvKey;
    this.separator = separator;
    this.basePath = basePath;
    okHttpClient = new OkHttpClient
        .Builder()
        .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
        .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
        .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
        .retryOnConnectionFailure(true)
        .build();
  }

  private ResponseBody executeRaw(String httpMethod, String url, RequestBody requestBody,
      Map<String, String> queryMap,
      Map<String, String> headerMap) throws Exception {

    url = url + createOrderedUrlParamFromMap(queryMap);
    //组装Headers
    Headers.Builder headerBuilder = new Headers.Builder();
    if (headerMap != null && !headerMap.isEmpty()) {
      headerMap.forEach((k, v) -> headerBuilder.add(k, v));
    }
    //组装Request
    Request request
        = new Request.Builder()
        .url(url)
        .method(httpMethod, requestBody)
        .headers(headerBuilder.build())
        .build();

    //发送请求
    Call call = okHttpClient.newCall(request);
    Response response = call.execute();

    //校验返回
    if (!response.isSuccessful()) {
      //解析错误信息
      String repsStr = response.body().string();
      logger.warn(repsStr);
      ResponseVo responseVo = JsonUtil.json2Object(repsStr, ResponseVo.class);
      if (responseVo == null || responseVo.getCode() == null) {
        throw new RuntimeException(
            "HttpClient调用失败"
                + ", httpStatus=" + response.code()
                + ", URL=" + response.request().url()
                + ", 错误信息：" + repsStr);

      } else {
        throw new RuntimeException("HttpClient调用失败"
            + ", URL=" + response.request().url()
            + "，错误信息：" + responseVo.toString());
      }
    }
    ResponseBody body = response.body();
    return body;
  }

  /**
   * 发送post请求. Content-Type : application/json .
   *
   * @param uri 资源路径
   * @param requestBodyJson request body， 加密前的明文，json字符串
   */
  public ResponseBody execute(String httpMethod, String uri, String requestBodyJson,
      Map<String, String> urlParamMap, MediaType mediaType)
      throws Exception {
    String encryptRequestBody = null;
    String encryptedNonce = null;
    if (StringUtils.isNotEmpty(requestBodyJson)) {
      //获取随机字符串，作为DES的加密秘钥，字符串长度建议不小于20
      String nonce = RandomStringUtils.randomAlphabetic(20);
      //DES加密业务数据
      encryptRequestBody = DesUtils.encrypt(requestBodyJson, nonce);
      //RSA加密nonce
      encryptedNonce = RSAUtils.encryptByPublicKey(nonce, remoteRsaPubKey);
    }
    //构建待加签数据
    String signatureSrcData = createSignatureSrcData(uri, separator,
        urlParamMap, encryptRequestBody);

    //RSA私钥加签
    String signature = RSAUtils.sign(signatureSrcData, localRsaPrvKey);

    //发送请求
    final String url = basePath + uri;
    Map<String, String> headerMap = new HashMap<>();
    headerMap.put("channelCode", channelCode);
    headerMap.put("signature", signature);
    if (null != encryptedNonce) {
      headerMap.put("nonce", encryptedNonce);
    }
    RequestBody requestBody = null != encryptRequestBody ? RequestBody
        .create(mediaType, encryptRequestBody) : null;
    return executeRaw(httpMethod, url, requestBody, urlParamMap, headerMap);
  }

  /**
   * 发送post请求. Content-Type : application/json . 返回结果需要解密
   *
   * @param uri 资源路径
   * @param requestBodyJson request body， 加密前的明文，json字符串
   */
  public ResponseVo executeJson(String httpMethod, String uri, String requestBodyJson,
      Map<String, String> urlParamMap)
      throws Exception {
    String response = execute(httpMethod, uri, requestBodyJson, urlParamMap, SysConstant.JSON).string();
    //解析返回结果【解密data节点】
    ResponseVo responseVo = parseResponse(response);
    return responseVo;
  }

  /**
   * 下载接口 接口返回内容不需要解密 .
   */
  public byte[] download(String httpMethod, String uri, String requestBoyJson,Map<String, String> urlParamMap)
      throws Exception {
    return execute(httpMethod, uri, requestBoyJson, urlParamMap, SysConstant.JSON).bytes();
  }

  //解析返回结果，并解密业务数据
  private ResponseVo parseResponse(String httpResponse) throws Exception {
    ResponseVo responseVo = JsonUtil.json2Object(httpResponse, ResponseVo.class);
    //解密业务数据
    String respData = responseVo.getData();
    if (respData != null) {
      String decryptRespData = RSAUtils.decryptByPrivateKey(respData, localRsaPrvKey);
      responseVo.setData(decryptRespData);
    }
    return responseVo;
  }

  /**
   * 根据map组装成url参数，参数的顺序按照key排序.
   *
   * @return url参数，如：?age=18&name=tom
   */
  private static String createOrderedUrlParamFromMap(Map<String, ?> queryMap) {
    if (queryMap == null || queryMap.isEmpty()) {
      return "";
    }
    StringBuffer params = new StringBuffer();
    queryMap.keySet().stream()
        .filter(k -> null != queryMap.get(k))
        .sorted()
        .forEach(k -> params.append("&").append(k.trim()).append("=")
            .append(queryMap.get(k).toString().trim()));
    return "?" + params.subSequence(1, params.length()).toString();
  }

  /**
   * 发送请求前组装加签的原始数据.
   *
   * @param uri requestURI
   * @param separator 分隔符，双方约定一致
   * @param urlParamMap url查询参数
   * @param requestBody 请求报文
   * @return 加签原始数据
   */
  private String createSignatureSrcData(String uri, String separator,
      Map<String, ?> urlParamMap,
      String requestBody) {
    return new StringBuffer(uri)
        .append(createOrderedUrlParamFromMap(urlParamMap))
        .append(separator)
        .append(requestBody == null ? "" : requestBody)
        .toString();
  }

  public static class Builder {

    /**
     * /渠道编号，中证提供
     */
    private String channelCode;

    //中证的RSA公钥，中证提供
    private String remoteRsaPubKey;

    //自己生成的RSA私钥，长度2048
    private String localRsaPrvKey;

    //分隔符，中证提供
    private String separator;

    //服务域名，中证提供
    private String basePath;

    //请求读取超时时间
    private Long readTimeout;
    private Long writeTimeout;
    private Long connectTimeout;


    public Builder channelCode(String channelCode) {
      this.channelCode = channelCode;
      return this;
    }

    public Builder localRsaPrvKey(String localRsaPrvKey) {
      this.localRsaPrvKey = localRsaPrvKey;
      return this;
    }

    public Builder remoteRsaPubKey(String remoteRsaPubKey) {
      this.remoteRsaPubKey = remoteRsaPubKey;
      return this;
    }

    public Builder basePath(String basePath) {
      this.basePath = basePath;
      return this;
    }

    public Builder separator(String separator) {
      this.separator = separator;
      return this;
    }

    public Builder readTimeout(Long readTimeout) {
      this.readTimeout = readTimeout;
      return this;
    }

    public Builder writeTimeout(Long writeTimeout) {
      this.writeTimeout = writeTimeout;
      return this;
    }

    public Builder connectTimeout(Long connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    public HerculesClient build() {
      if (StringUtils.isBlank(channelCode)
          || StringUtils.isBlank(remoteRsaPubKey)
          || StringUtils.isBlank(basePath)
          || StringUtils.isBlank(localRsaPrvKey)) {
        throw new RuntimeException(
            "channelCode,remoteRsaPubKey,basePath ,localRsaPrvKey must not be blank");
      }
      if (null == readTimeout) {
        readTimeout = DEFAULT_READ_TIMEOUT;
      }
      if (null == writeTimeout) {
        writeTimeout = DEFAULT_WRITE_TIMEOUT;
      }
      if (null == connectTimeout) {
        connectTimeout = DEFAULT_CONNECT_TIMEOUT;
      }

      if (StringUtils.isEmpty(separator)) {
        separator = DEFAULT_SEPARATOR;
      }
      HerculesClient herculesClient = new HerculesClient(channelCode, remoteRsaPubKey,
          localRsaPrvKey, separator, basePath, readTimeout, writeTimeout, connectTimeout);
      return herculesClient;
    }
  }
}

package com.csci.demo.utils;

import com.csci.demo.model.vo.ResponseVo;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.HttpMethod;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述：
 * <p>
 * Created by zhukai on 2018/8/14.
 */
public class HttpUtil {

  private static final Charset UTF8 = Charset.forName("UTF-8");
  private static final String MEDIA_TYPE_JSON = "application/json";
  private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);
  private static final OkHttpClient okHttpClient;

  static {
    okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(60000L, TimeUnit.MILLISECONDS)
        .readTimeout(60000L, TimeUnit.MILLISECONDS)
        .writeTimeout(60000L, TimeUnit.MILLISECONDS)
        .build();
  }

  //get请求
  public static String get(String url, Map<String, String> queryMap,
      Map<String, String> headerMap) throws IOException {
    url = url + createOrderedUrlParamFromMap(queryMap);
    return execute(HttpMethod.GET,url, null, headerMap);
  }

  //form表单提交
  public static String postForm(String url, Map<String, String> formParam,
      Map<String, String> queryMap, Map<String, String> headerMap) throws IOException {
    FormBody.Builder requestBodyBuilder = new FormBody.Builder();
    if (formParam != null && !formParam.isEmpty()) {
      formParam.forEach((k, v) -> requestBodyBuilder.add(k, v));
    }
    FormBody formBody = requestBodyBuilder.build();
    url = url + createOrderedUrlParamFromMap(queryMap);
    return execute(HttpMethod.POST,url, formBody, headerMap);
  }

  //post json提交
  public static String postJson(String url, String requestBodyJson,
      Map<String, String> queryMap, Map<String, String> headerMap) throws Exception {

    RequestBody body = getRequestBody(MEDIA_TYPE_JSON, requestBodyJson);
    url = url + createOrderedUrlParamFromMap(queryMap);
    return execute(HttpMethod.POST,url, body, headerMap);
  }

  /**
   * post请求.
   *
   * @param requestBody 请求体
   * @param queryMap url查询参数
   * @param headerMap http header
   */
  public static String post(String url, String requestBody, String mediaType,
      Map<String, String> queryMap, Map<String, String> headerMap) throws Exception {

    RequestBody body = getRequestBody(mediaType, requestBody);
    url = url + createOrderedUrlParamFromMap(queryMap);
    return execute(HttpMethod.POST,url, body, headerMap);
  }

  public static String execute(String httpMethod,String url, String requestBody,
      Map<String, String> queryMap, Map<String, String> headerMap) throws Exception {

    RequestBody body = null != requestBody ? getRequestBody(MEDIA_TYPE_JSON, requestBody) :null;
    url = url + createOrderedUrlParamFromMap(queryMap);
    return execute(httpMethod,url, body, headerMap);
  }

  private static String execute(String httpMethod,String url, RequestBody requestBody,
      Map<String, String> headerMap) throws IOException {
    //组装Headers
    Headers.Builder headerBuilder = new Headers.Builder();
    if (headerMap != null && !headerMap.isEmpty()) {
      headerMap.forEach((k, v) -> headerBuilder.add(k, v));
    }

    //组装Request
    Request request
        = new Request.Builder()
        .url(url)
        .method(httpMethod,requestBody)
        .headers(headerBuilder.build())
        .build();

    //发送请求
    Call call = okHttpClient.newCall(request);
    Response response = call.execute();

    //校验返回
    if (!response.isSuccessful()) {
      //解析错误信息
      String repsStr = response.body().string();
      ResponseVo responseVo = JsonUtil.json2Object(repsStr, ResponseVo.class);
      if (responseVo == null || responseVo.getCode() == null) {
        throw new RuntimeException(
            "HttpClient调用失败"
                + ", httpStatus=" + response.code()
                + ", URL=" + response.request().url()
                + ", 错误信息：" + response.message());
      } else {
        throw new RuntimeException("HttpClient调用失败"
            + ", URL=" + response.request().url()
            + "，错误信息：" + responseVo.getMessage());
      }
    }
    return response.body().string();
  }

  private static RequestBody getRequestBody(String mediaType, String requestBody) {
    return new RequestBody() {
      @Override
      public MediaType contentType() {
        return MediaType.parse(mediaType);
      }

      @Override
      public void writeTo(BufferedSink sink) throws IOException {
        if (requestBody != null && requestBody.length() > 0) {
          sink.writeString(requestBody, UTF8);
        }
        sink.flush();
      }
    };

  }

  /**
   * 根据map组装成url参数，参数的顺序按照key排序.
   *
   * @return url参数，如：?age=18&name=tom
   */
  public static String createOrderedUrlParamFromMap(Map<String, ?> queryMap) {
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

}

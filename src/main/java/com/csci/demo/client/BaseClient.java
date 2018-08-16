package com.csci.demo.client;

import com.csci.demo.model.vo.ResponseVo;
import com.csci.demo.utils.HttpUtil;
import com.csci.demo.utils.JsonUtil;
import com.csci.demo.utils.RSAUtils;
import java.util.Map;

public class BaseClient {

  //分隔符，中证提供
  public static final String SEPARATOR = "@";

  //中证服务器域名
  public static final String BASE_PATH = "http://localhost:8080";

  //渠道编号，中证提供
  public static final String CHANNEL_CODE = "LIAN_LIAN";

  //中证的RSA公钥，一般放在配置里，中证提供
  public static final String ZZ_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5R6I0zL8H9dc7Jf/9GskIedZxl8aDlLXpUcKrStVhfdaXdugXa8eEid9k4T1WnvCqht9IAPLIsyRiBgoSsitRqkwrHDs3HsyLDnAw5jUo8uhKyMK4pCzrkbuAJvXya6RtLEHJUMfrxNJBpVXaZacp3RZ7XGdg5WrlSohKKSMfh8PsLBVwMLxp179SDmPO+I/NVxcP/g2CDPx+wO2hpLEDfRvUZXjgdgUxieC7hzd3ppAkSH7fT06sb2JMO2TirG4IH2Z7pN37/EPYIYprPTjKmPDrEdSaZEdJuOsasIk1DSas12jWUxvkc4STZstiujxGkQB/r/WyQ3z3JliqkKhcwIDAQAB";

  //自己的私钥
  public static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCph/ZpsiGrygYXBMhVNj2xM4JC9IOUAWhrkBUHSzuUyUn1XgbV0Xudmezt1SXb2CjsSSL8jPC5T4ew/J5J70ippsx0PmshbHlQmEebx1DhbqEWsNl/t3WFxgal4LEtjJX+RxwGDoslYqQlNrNCkKTVWoQQQlxf5kjE3myQITK2lc0FzIfI+JmPOKGTSXhSWoU54X5MMjwVnRDgeePK8Dq6nj2eRi4mWPJaoJeVtbj+OA0JtlkWSVFwLmaW/YqUjvFL1rzP6vRm3/ugTDJ4CMrarDBGDU4K5JPo2+TuHl2ebt7hYOEK12ya7qIeOhxtEroPAOLwDpnfbs5sI8JvA/czAgMBAAECggEAFLJtqe++Xpu2bJ7/2i9aDsUsFQKHAc2+MsuPVamgp4R/i/4jtXzyq4X8P/jyu0lDjb1MESsfxG/qL/hXVs1owJWFhneMWrNEiEtfCqylui/oaL3Ef+wk4+UOMLgiyV3NfhKuYCrsX+8P/R8VeX3xMb7wOo/veSHDJo3FET+MUm/6dLAmm/R+1PSdFRif5BfMpGVHLAcCzP879eVI2p9G6uEfCa1aDHFmoiiGy0I8jhfFemXJDjEXjxblsLhgOZ2KWUbh5HWSLSMnEdcf9UIGRuMxljr59r0PEkLo/ZQ7QmfJ7GPEnveKpzJCS0ZnG6HxKJ6ep3OjWWAnP8gTIzoc4QKBgQDd7ZrNFs75WMOa7DZB9BEGkoYInyYdByQEmIAMedpFSYzjUw5IcBOfViT1vtmqqVVP9LLYpE1lw0EjauJQbEMxAkDClqaiZHoN35Jtb6MHl/Fikua2GWDMYspbhCH87xCOqZbSUOI9tEBHXfTSP+E+b2NjAoUIBxPdFHNUWfDw4wKBgQDDjwILtNjPjWhzSdDqxVL5WidJvFXo2uToaNP3QHGT+hLLkOzX7aQ4068VUdnrD7tdgS8IpygddJlobYk7LnqHydGEPlbjwq8APyrp+dcC7344TAJ2vZ3UiSw2n+yLea11/97CM1A5VxX5Tl/l7pNVVpuB2Gkh06LA93eC/4BBcQKBgFLQv9uYj1PtE9FUJEsoUNIrC2lcOlTKvV4Zqm79ab5BXBnYfJP7ftcOxmwN9BwDZJqZrcO6TEeVPVGCKUHppIk9Vf1HSpd+9y0GKRY9epDy2lj2iLGbdsvOccUE4coJTBxwnJ+PH133TaVUIm/y9pLd7jCcWBhOy9LMNt1bUtYRAoGBALb2oa9aME9fug3DTleY7pylIIT4VFhcFDbtfQOjo6ATJGqY2T7c8/zG8NQ9lGk8esxtBRhPbFTCtpThhrd8MN/Cjufve1P03c6ZSwlF20i/0ZPSaau2lGg5PsiMr79+xC/WiQ+g+rnrJH/wOADkSaxaL3w4gYRIgcmQvpwteE3BAoGBAIp23qk2ZoVpAWtpv87poUzl+nlRb7q5Gsa+Da5iW5lRbbdrEb6SbAm+lgNasgRSfdi2aUjcQ+x+Q+jETFNu53HOX7WxHVuRy34wN4ToORssW/7i8BcpkrBu9ltG5VDSQGJMKeNVxR9EYn5Z85wJUBdEA0vFos+tLFUygRHwURsz";

  //自己的公钥，需要提供给中证
  public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqYf2abIhq8oGFwTIVTY9sTOCQvSDlAFoa5AVB0s7lMlJ9V4G1dF7nZns7dUl29go7Eki/IzwuU+HsPyeSe9IqabMdD5rIWx5UJhHm8dQ4W6hFrDZf7d1hcYGpeCxLYyV/kccBg6LJWKkJTazQpCk1VqEEEJcX+ZIxN5skCEytpXNBcyHyPiZjzihk0l4UlqFOeF+TDI8FZ0Q4HnjyvA6up49nkYuJljyWqCXlbW4/jgNCbZZFklRcC5mlv2KlI7xS9a8z+r0Zt/7oEwyeAjK2qwwRg1OCuST6Nvk7h5dnm7e4WDhCtdsmu6iHjocbRK6DwDi8A6Z327ObCPCbwP3MwIDAQAB";

  /**
   * 发送请求前组装加签的原始数据
   *
   * @param uri requestURI
   * @param separator 分隔符，双方约定一致
   * @param urlParamMap url查询参数
   * @param requestBody 请求报文
   * @return 加签原始数据
   */
  public static String createSignatureSrcData(String uri, String separator,
      Map<String, ?> urlParamMap,
      String requestBody) {
    return new StringBuffer(uri)
        .append(HttpUtil.createOrderedUrlParamFromMap(urlParamMap))
        .append(separator)
        .append(requestBody == null ? "" : requestBody)
        .toString();
  }

  //解析返回结果，并解密业务数据
  public static ResponseVo parseResponse(String httpResponse) throws Exception {
    ResponseVo responseVo = JsonUtil.json2Object(httpResponse, ResponseVo.class);
    //解密业务数据
    String respData = responseVo.getData();
    if (respData != null) {
      String decryptRespData = RSAUtils.decryptByPrivateKey(respData, PRIVATE_KEY);
      responseVo.setData(decryptRespData);
    }
    return responseVo;
  }


}
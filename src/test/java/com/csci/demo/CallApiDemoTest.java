package com.csci.demo;

import com.csci.demo.client.HerculesClient;
import com.csci.demo.model.vo.ResponseVo;
import com.csci.demo.utils.JsonUtil;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class CallApiDemoTest {

  //分隔符，中证提供
  public static final String SEPARATOR = "@";

  //中证域名，中证提供
  public static final String BASE_PATH = "http://scpgateway-ls.dev.chinacsci.com";

  //渠道编号，中证提供
  public static final String CHANNEL_CODE = "LIAN_LIAN";

  //中证的RSA公钥，中证提供
  public static final String ZZ_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5R6I0zL8H9dc7Jf/9GskIedZxl8aDlLXpUcKrStVhfdaXdugXa8eEid9k4T1WnvCqht9IAPLIsyRiBgoSsitRqkwrHDs3HsyLDnAw5jUo8uhKyMK4pCzrkbuAJvXya6RtLEHJUMfrxNJBpVXaZacp3RZ7XGdg5WrlSohKKSMfh8PsLBVwMLxp179SDmPO+I/NVxcP/g2CDPx+wO2hpLEDfRvUZXjgdgUxieC7hzd3ppAkSH7fT06sb2JMO2TirG4IH2Z7pN37/EPYIYprPTjKmPDrEdSaZEdJuOsasIk1DSas12jWUxvkc4STZstiujxGkQB/r/WyQ3z3JliqkKhcwIDAQAB";

  //自己生成的RSA私钥，长度2048
  public static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCph/ZpsiGrygYXBMhVNj2xM4JC9IOUAWhrkBUHSzuUyUn1XgbV0Xudmezt1SXb2CjsSSL8jPC5T4ew/J5J70ippsx0PmshbHlQmEebx1DhbqEWsNl/t3WFxgal4LEtjJX+RxwGDoslYqQlNrNCkKTVWoQQQlxf5kjE3myQITK2lc0FzIfI+JmPOKGTSXhSWoU54X5MMjwVnRDgeePK8Dq6nj2eRi4mWPJaoJeVtbj+OA0JtlkWSVFwLmaW/YqUjvFL1rzP6vRm3/ugTDJ4CMrarDBGDU4K5JPo2+TuHl2ebt7hYOEK12ya7qIeOhxtEroPAOLwDpnfbs5sI8JvA/czAgMBAAECggEAFLJtqe++Xpu2bJ7/2i9aDsUsFQKHAc2+MsuPVamgp4R/i/4jtXzyq4X8P/jyu0lDjb1MESsfxG/qL/hXVs1owJWFhneMWrNEiEtfCqylui/oaL3Ef+wk4+UOMLgiyV3NfhKuYCrsX+8P/R8VeX3xMb7wOo/veSHDJo3FET+MUm/6dLAmm/R+1PSdFRif5BfMpGVHLAcCzP879eVI2p9G6uEfCa1aDHFmoiiGy0I8jhfFemXJDjEXjxblsLhgOZ2KWUbh5HWSLSMnEdcf9UIGRuMxljr59r0PEkLo/ZQ7QmfJ7GPEnveKpzJCS0ZnG6HxKJ6ep3OjWWAnP8gTIzoc4QKBgQDd7ZrNFs75WMOa7DZB9BEGkoYInyYdByQEmIAMedpFSYzjUw5IcBOfViT1vtmqqVVP9LLYpE1lw0EjauJQbEMxAkDClqaiZHoN35Jtb6MHl/Fikua2GWDMYspbhCH87xCOqZbSUOI9tEBHXfTSP+E+b2NjAoUIBxPdFHNUWfDw4wKBgQDDjwILtNjPjWhzSdDqxVL5WidJvFXo2uToaNP3QHGT+hLLkOzX7aQ4068VUdnrD7tdgS8IpygddJlobYk7LnqHydGEPlbjwq8APyrp+dcC7344TAJ2vZ3UiSw2n+yLea11/97CM1A5VxX5Tl/l7pNVVpuB2Gkh06LA93eC/4BBcQKBgFLQv9uYj1PtE9FUJEsoUNIrC2lcOlTKvV4Zqm79ab5BXBnYfJP7ftcOxmwN9BwDZJqZrcO6TEeVPVGCKUHppIk9Vf1HSpd+9y0GKRY9epDy2lj2iLGbdsvOccUE4coJTBxwnJ+PH133TaVUIm/y9pLd7jCcWBhOy9LMNt1bUtYRAoGBALb2oa9aME9fug3DTleY7pylIIT4VFhcFDbtfQOjo6ATJGqY2T7c8/zG8NQ9lGk8esxtBRhPbFTCtpThhrd8MN/Cjufve1P03c6ZSwlF20i/0ZPSaau2lGg5PsiMr79+xC/WiQ+g+rnrJH/wOADkSaxaL3w4gYRIgcmQvpwteE3BAoGBAIp23qk2ZoVpAWtpv87poUzl+nlRb7q5Gsa+Da5iW5lRbbdrEb6SbAm+lgNasgRSfdi2aUjcQ+x+Q+jETFNu53HOX7WxHVuRy34wN4ToORssW/7i8BcpkrBu9ltG5VDSQGJMKeNVxR9EYn5Z85wJUBdEA0vFos+tLFUygRHwURsz";
  //自己生成的RSA公钥，长度2048，需要提供给中证
  public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqYf2abIhq8oGFwTIVTY9sTOCQvSDlAFoa5AVB0s7lMlJ9V4G1dF7nZns7dUl29go7Eki/IzwuU+HsPyeSe9IqabMdD5rIWx5UJhHm8dQ4W6hFrDZf7d1hcYGpeCxLYyV/kccBg6LJWKkJTazQpCk1VqEEEJcX+ZIxN5skCEytpXNBcyHyPiZjzihk0l4UlqFOeF+TDI8FZ0Q4HnjyvA6up49nkYuJljyWqCXlbW4/jgNCbZZFklRcC5mlv2KlI7xS9a8z+r0Zt/7oEwyeAjK2qwwRg1OCuST6Nvk7h5dnm7e4WDhCtdsmu6iHjocbRK6DwDi8A6Z327ObCPCbwP3MwIDAQAB";

  private HerculesClient herculesClient = new HerculesClient.Builder()
      .basePath(BASE_PATH)
      .channelCode(CHANNEL_CODE)
      .localRsaPrvKey(PRIVATE_KEY)
      .separator(SEPARATOR)
      .remoteRsaPubKey(ZZ_PUBLIC_KEY)
      .build();

  /**
   * 对应接口：【测试接口1】 接口文档：【DEMO-快速对接文档.html】
   */
  @Test
  public void testGet() throws Exception {
    //请求uri
    String uri = "/api/v1/demo/test/get";

    //url参数
    Map<String, String> urlParamMap = new HashMap<>();
    urlParamMap.put("loanAppId", "order12345");
    urlParamMap.put("extendUserId", "user12345");
    //发送请求
    ResponseVo responseVo = herculesClient.executeJson("GET", uri, null, urlParamMap);
    System.out.println("返回结果：" + responseVo);
    Assert.assertTrue("SUCCESS".equals(responseVo.getCode()));
  }

  /**
   * 对应接口：【测试接口2】 接口文档：【DEMO-快速对接文档.html】
   */
  @Test
  public void testPost() throws Exception {
    //请求uri
    String uri = "/api/v1/demo/test/post";
    //业务数据
    RequestDto requestDto = new RequestDto("user1234",
        "1100000", "6222081991087490987");

    ResponseVo responseVo = herculesClient
        .executeJson("POST", uri, JsonUtil.toJsonStr(requestDto), null);
    System.out.println("返回结果：" + responseVo);
    Assert.assertTrue("SUCCESS".equals(responseVo.getCode()));
  }

  @Test
  public void creditResultQuery() throws Exception {
    //请求uri
    String uri = String.format("/api/mock/v1/credit/%s/query", "123");

    //发送请求
    ResponseVo responseVo = herculesClient.executeJson("GET",uri,null,null);
    System.out.println("返回结果：" + responseVo);

    Assert.assertTrue("SUCCESS".equals(responseVo.getCode()));
  }

  @Test
  public void testGetDoc() throws Exception {
    String uri = String.format("/api/v1/documents/download/%s/%s","14353488","6402330");
    byte[] bytes = herculesClient.download("GET", uri, null);
    FileUtils.writeByteArrayToFile(new File("tst.png"),bytes);
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

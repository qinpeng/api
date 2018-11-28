package com.csci.demo;

import com.csci.demo.client.HerculesClient;
import com.csci.demo.model.vo.DocumentDownloadReqVo;
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
  //public static final String BASE_PATH = "http://localhost:8081";

  //渠道编号，中证提供
  public static final String CHANNEL_CODE = "JUN_TUO";
  //中证的RSA公钥，中证提供
  public static final String ZZ_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDUHEIelz3g/Gqg8VNK0VSS4Jc1\n"
      + "zBAh+02XkQGyT6LeysuXXKoalBR3yVFWMnxF7qkcCSV+R5bakx6IUlR/u1hCb7Qf\n"
      + "OXbdgBMOKbT4Q5XyhiCGr/0GNdzVLxKCiu8e/ee/QOMwIotKYrt0KRveXbKTWE6g\n"
      + "5HI55Y2sD6zvl0JQfQIDAQAB";

  //自己生成的RSA私钥，长度2048
  public static final String PRIVATE_KEY = "MIICXgIBAAKBgQC/7MjGQgc/xU7Hj6OM3Ea7ZUV6FrtV+bKLF7CH0/VCS0vFmtaOJLTrfqds4lLBIHJbQ0C6GWSDGSF1cdzY/r09ppTenazlQjcDz8RjwtfIZrAk6n0plRCC3rfxcxgAeEp2B4wO97RCbELw2BrYWR3NV+WEz6B3m1P30rky/cyJLQIDAQABAoGBAJvViewYnsQz09SLl7N/A9uSzgfkvzV+6m6vzIFtI94xPxPytjVyLohsLUtmtOTlEJVzlPHGPmuIEzfGPRjgNgyYezpU0ePJ5s7Ggrb4ty2WKkyQfFuKNkymbpHQUzCP234kKdz5xO+aZ0406oV07ZV3is2oeeUQ/c2jvuiZsJphAkEA8k7KWEMdYGiwhNKtYjN8F07nK7xB2gWc4Szdet/DB4xr+l2DSsGTD0n+yojFofOz12W5TNHL6dLPJui4G4UhdQJBAMrFKKeNtwacZl3DC3hG3DcsE+a/HY0+d3PjpQgkwurWNYE5c35BFyzKy/+CgN4R07NeLP3xerqi2xpk37xb2dkCQQDhgPJPsiZk0wl0k51JByE1j/kUet/OR+r4pQh6kkSvgb/8AYtuxzhVwkedtiw3zNZSYBlTpOxhcA/Z5jtxYTUBAkEAmG/yKUyzvOeVWYXJOKnk4iFj8MPavpWojdok9mNUmeFiJfz/43mhp43qIPOGV+yE/8KcBmkk/+xw1X3iaaOPuQJARdcAClvpWUZfUadWqHOkhksKLjKm0FtoBs6NnfwwbDVAEC+iAuX6oYSQgmP+6wmeTuagy/j0GC6rWhSskKSoZQ==";
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
    DocumentDownloadReqVo reqVo = new DocumentDownloadReqVo();
    reqVo.setLoanApplyId("14353488");
    reqVo.setDocId("6402330");
    String uri = String.format("/api/v1/documents/download");
    byte[] bytes = herculesClient.download("POST", uri, JsonUtil.toJsonStr(reqVo),null);
    FileUtils.writeByteArrayToFile(new File("tst.png"),bytes);
  }


  @Test
  public void listDoc() throws Exception {
    String uri = String.format("/api/mock/v1/documents/lists");
    ResponseVo responseVo = herculesClient.executeJson("POST", uri, "{\"loanApplyId\":\"1\",\"docType\":\"IDENTITY_CARD_FRONT\"}",null);
    System.out.println(responseVo);
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

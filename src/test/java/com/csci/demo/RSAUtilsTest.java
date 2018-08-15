package com.csci.demo;

import com.csci.demo.utils.RSAUtils;
import java.util.Map;
import org.junit.Test;

public class RSAUtilsTest {

  private static final String UTF8 = "UTF-8";
  static String privateKey = null;
  static String publicKey = null;

  static {
    try {
      Map<String, String> keyPair = RSAUtils.genKeyPair();
      privateKey = keyPair.get(RSAUtils.PRIVATE_KEY);
      publicKey = keyPair.get(RSAUtils.PUBLIC_KEY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //公私钥生成
  @Test
  public void testGenerateKey() throws Exception {
    Map<String, String> keyPair = RSAUtils.genKeyPair();
    privateKey = keyPair.get(RSAUtils.PRIVATE_KEY);
    publicKey = keyPair.get(RSAUtils.PUBLIC_KEY);
    System.out.println("公钥：" + publicKey);
    System.out.println("私钥：" + privateKey);

  }

  @Test
  public void testSign() throws Exception {
    String src = "这是待加签文本";
    String sign = RSAUtils.sign(src, privateKey);
    System.out.println("签名：" + sign);
    System.out.println("sign = " + sign.length());
    boolean verify = RSAUtils.verify(src, publicKey, sign);
    System.out.println("验签结果：" + verify);
  }

  @Test
  public void testEncryptAndDecrypt() throws Exception {
    String src = "我是原始的业务数据，需要加密传输";
    System.out.println("原始数据：" + src);
    //加密
    String encodeEncryptStr = RSAUtils.encryptByPublicKey(src, publicKey);
    System.out.println("加密后：" + encodeEncryptStr);
    //解密
    String decryptStr = RSAUtils.decryptByPrivateKey(encodeEncryptStr, privateKey);
    System.out.println("解密后：" + decryptStr);
  }


}
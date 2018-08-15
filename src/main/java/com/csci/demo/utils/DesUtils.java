package com.csci.demo.utils;

import com.csci.demo.common.SysConstant;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author zhukai created at 2016/10/18
 */
public class DesUtils {

  private final static String DES = "DES";

  public static void main(String[] args) throws Exception {
    String key = "nonce key";
    System.out.println("key=      " + key);

    String data = "123456";
    //加密
    String encryptDataString = encrypt(data, key);
    System.err.println("加密后：" + encryptDataString);
    //解密
    String decryptedDataString = decrypt(encryptDataString, key);
    System.err.println("解密后：" + decryptedDataString);
  }

  private static byte[] stringToByteWithMd5(String key) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("md5");
    digest.update(key.getBytes(SysConstant.UTF8));
    byte[] bytes = digest.digest();
    return bytes;
  }

  /**
   * Description 根据键值进行加密
   *
   * @param key 加密键byte数组
   */
  public static String encrypt(String data, String key) throws Exception {
    byte[] bt = encrypt(data.getBytes(SysConstant.UTF8), stringToByteWithMd5(key));
    return Base64Utils.encode(bt);
  }

  /**
   * Description 根据键值进行解密
   *
   * @param key 加密键byte数组
   */
  public static String decrypt(String data, String key) throws Exception {
    if (data == null) {
      return null;
    }
    byte[] bt = decrypt(Base64Utils.decode(data), stringToByteWithMd5(key));
    return new String(bt, SysConstant.UTF8);
  }

  /**
   * Description 根据键值进行加密
   *
   * @param key 加密键byte数组
   */
  private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
    // 生成一个可信任的随机数源
    SecureRandom sr = new SecureRandom();

    // 从原始密钥数据创建DESKeySpec对象
    DESKeySpec dks = new DESKeySpec(key);

    // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
    SecretKey securekey = keyFactory.generateSecret(dks);

    // Cipher对象实际完成加密操作
    Cipher cipher = Cipher.getInstance(DES);

    // 用密钥初始化Cipher对象
    cipher.init(Cipher.ENCRYPT_MODE, securekey);
    return cipher.doFinal(data);
  }


  /**
   * Description 根据键值进行解密
   *
   * @param key 加密键byte数组
   */
  private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
    // 生成一个可信任的随机数源
    SecureRandom sr = new SecureRandom();

    // 从原始密钥数据创建DESKeySpec对象
    DESKeySpec dks = new DESKeySpec(key);

    // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
    SecretKey securekey = keyFactory.generateSecret(dks);

    // Cipher对象实际完成解密操作
    Cipher cipher = Cipher.getInstance(DES);

    // 用密钥初始化Cipher对象
    cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

    return cipher.doFinal(data);
  }
}

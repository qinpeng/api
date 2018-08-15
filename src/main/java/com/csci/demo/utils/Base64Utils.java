package com.csci.demo.utils;

import com.csci.demo.common.SysConstant;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class Base64Utils {

  /**
   * 文件读取缓冲区大小
   */
  private static final int CACHE_SIZE = 1024;

  /** */

  public static void main(String[] args) throws Exception {
    String source = "这是个测试字符串";
    String base64String = Base64.encodeBase64String(source.getBytes(SysConstant.UTF8));
    System.out.println("解码:" + base64String);

    System.out.println(StringUtils.center("解码", 60, "="));
    System.out.println(new String(Base64.decodeBase64(base64String)));
  }

  /** */

  /**
   * <p>
   * BASE64字符串解码为二进制数据
   * </p>
   */
  public static byte[] decode(String base64) throws Exception {
    return Base64.decodeBase64(base64.getBytes(SysConstant.UTF8));
  }

  /**
   * 作用二进制数据编码为BASE64字符串 备注：据RFC 822规定，每76个字符，会加上一个回车换行，即返回的字符串过长会自动换行，若无需换行，解开注解
   */
  public static String encode(byte[] bytes) throws Exception {
    return Base64.encodeBase64String(bytes);
  }

  /** */
  /**
   * <p>
   * 将文件编码为BASE64字符串
   * </p>
   * <p>
   * 大文件慎用，可能会导致内存溢出
   * </p>
   *
   * @param filePath 文件绝对路径
   */
  public static String encodeFile(String filePath) throws Exception {
    byte[] bytes = fileToByte(filePath);
    return encode(bytes);
  }

  /** */
  /**
   * <p>
   * BASE64字符串转回文件
   * </p>
   *
   * @param filePath 文件绝对路径
   * @param base64 编码字符串
   */
  public static void decodeToFile(String filePath, String base64) throws Exception {
    byte[] bytes = decode(base64);
    byteArrayToFile(bytes, filePath);
  }

  /** */
  /**
   * <p>
   * 文件转换为二进制数组
   * </p>
   *
   * @param filePath 文件路径
   */
  public static byte[] fileToByte(String filePath) throws Exception {
    byte[] data = new byte[0];
    File file = new File(filePath);
    if (file.exists()) {
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
      byte[] cache = new byte[CACHE_SIZE];
      int nRead = 0;
      while ((nRead = in.read(cache)) != -1) {
        out.write(cache, 0, nRead);
        out.flush();
      }
      out.close();
      in.close();
      data = out.toByteArray();
    }
    return data;
  }

  /** */
  /**
   * <p>
   * 二进制数据写文件
   * </p>
   *
   * @param bytes 二进制数据
   * @param filePath 文件生成目录
   */
  public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
    InputStream in = new ByteArrayInputStream(bytes);
    File destFile = new File(filePath);
    if (!destFile.getParentFile().exists()) {
      destFile.getParentFile().mkdirs();
    }
    destFile.createNewFile();
    OutputStream out = new FileOutputStream(destFile);
    byte[] cache = new byte[CACHE_SIZE];
    int nRead = 0;
    while ((nRead = in.read(cache)) != -1) {
      out.write(cache, 0, nRead);
      out.flush();
    }
    out.close();
    in.close();
  }


}

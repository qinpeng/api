# 中证资产端客户端工具

该项目是辅助开发者调用中证资产端API.该项目是基于`Java8`开发

## 开始使用

创建`HerculesClient`对象

```java
HerculesClient herculesClient = new HerculesClient.Builder()
      .basePath(BASE_PATH)
      .channelCode(CHANNEL_CODE)
      .localRsaPrvKey(PRIVATE_KEY)
      .remoteRsaPubKey(ZZ_PUBLIC_KEY)
      .build();
```

调用接口

```java
   //请求uri
    String uri = "/api/v1/demo/test/post";
    //业务数据
    RequestDto requestDto = new RequestDto("user1234",
        "1100000", "6222081991087490987");

    ResponseVo responseVo = herculesClient
        .executeJson("POST", uri, JsonUtil.toJsonStr(requestDto), null);
```



## 本地生成RSA秘钥

```java
 Map<String, String> keyPair = RSAUtils.genKeyPair();
 String privateKey = keyPair.get(RSAUtils.PRIVATE_KEY);
 String publicKey = keyPair.get(RSAUtils.PUBLIC_KEY);
 System.out.println("公钥：" + publicKey);
 System.out.println("私钥：" + privateKey);
```



## 更多例子

请参考`/test/java` 目录下的例子
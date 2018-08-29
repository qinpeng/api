# 域名

| 环境 | 域名                                       |
| ---- | ------------------------------------------ |
| 测试 | http://scpgateway-ls.dev.chinacsci.com/api |
| 线上 | http://scpgateway-ls.chinacsci.com/api     |



# 接口报文格式

#### 响应报文结构

```json
{
  "code": "SUCCESS", //SUCCESS, ERROR
  "message": "", //empty if result is SUCCESS
  "data": "response business data, type is JsonObject",
  "signature": "signature of Content, type is String"
}
```

# 接口报文参数

## Response
返回参数

| 字段名    | 类型       | 是否必填 | 字段说明                                                |
| --------- | ---------- | -------- | ------------------------------------------------------- |
| code      | String     | 是        | 业务逻辑是否成功的标志。成功：`SUCCESS`， 失败：`ERROR` |
| message   | String     | 是        | 错误信息                                                |
| data      | JsonObject | 是        | 返回的业务数据。**字段明细详见各个接口**。              |

# Header
| 名称        | 类型   | 说明                                                       |
| ----------- | ------ | ---------------------------------------------------------- |
| ContentType | String | 对于POST请求，接口若没特殊说明则默认采用`application/json` |
| channelCode | String | 通道码,用来识别不同渠道,由中证提供                         |
| signature   | String | 签名。具体计算签名算法下面会介绍                           |

# 编码

编码统一都采用 `UTF-8`

# 请求加密与解密

为了保证通信过程数据不被篡改和泄漏，所有和服务器通信数据必须经过加解密流程，下面会一一介绍
  加密/解密流程图：
![流程图](http://o6wkmqikd.bkt.clouddn.com/scp_api_encryt%20%282%29.png)

step1：本地生成RSA 公钥/私钥(pub_key/prv_key),密钥长度为2048字节。把公钥发给中证对接客户经理，自己保留好私钥(解密返回数据使用)

step2：去向中证对接人客户经理开通API通道并索要服务器RSA 公钥和`channelCode`。

step3: 参考文档，完成接口测试后进行相应开发。

step4:完成后发布上线

[加密/解密 DEMO](#https://github.com/zhukaicsci/api.git)

## 加密

### 生成nonce

获取随机字符串，作为DES的加密秘钥，字符串长度不小于20。主要用于加密请求body，如果请求没有body(如GET请求)则不需要生成。

### 加密body业务数据

使用DES对称加密算法根据nonce 加密body业务数据，请求没有body可以忽略。

### 生成signature

signature 主要作用是防止数据被篡改 生成算法如下

```html
RSA(URI+所有请求参数进行排序+@+加密后的body,l_prv_key)
```

**文档中url{}带问号（如{?status}）为请求参数，要参与排序，@为分割URL和body符号，如果没有body此符号需要追加在字符串末尾**

将url的所有**请求参数**按照**字母序**进行排序（如果参数包含中文，中文保持原文即可，无需对其单独转码），并使用&连接， 请求body不参与排序。见如下实例：

```shell
请求url为：
/api/v1/demo/test/get?loanAppId=order12345&extendUserId=user12345

本地生成私钥  "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCph/ZpsiGrygYXBMhVNj2xM4JC9IOUAWhrkBUHSzuUyUn1XgbV0Xudmezt1SXb2CjsSSL8jPC5T4ew/J5J70ippsx0PmshbHlQmEebx1DhbqEWsNl/t3WFxgal4LEtjJX+RxwGDoslYqQlNrNCkKTVWoQQQlxf5kjE3myQITK2lc0FzIfI+JmPOKGTSXhSWoU54X5MMjwVnRDgeePK8Dq6nj2eRi4mWPJaoJeVtbj+OA0JtlkWSVFwLmaW/YqUjvFL1rzP6vRm3/ugTDJ4CMrarDBGDU4K5JPo2+TuHl2ebt7hYOEK12ya7qIeOhxtEroPAOLwDpnfbs5sI8JvA/czAgMBAAECggEAFLJtqe++Xpu2bJ7/2i9aDsUsFQKHAc2+MsuPVamgp4R/i/4jtXzyq4X8P/jyu0lDjb1MESsfxG/qL/hXVs1owJWFhneMWrNEiEtfCqylui/oaL3Ef+wk4+UOMLgiyV3NfhKuYCrsX+8P/R8VeX3xMb7wOo/veSHDJo3FET+MUm/6dLAmm/R+1PSdFRif5BfMpGVHLAcCzP879eVI2p9G6uEfCa1aDHFmoiiGy0I8jhfFemXJDjEXjxblsLhgOZ2KWUbh5HWSLSMnEdcf9UIGRuMxljr59r0PEkLo/ZQ7QmfJ7GPEnveKpzJCS0ZnG6HxKJ6ep3OjWWAnP8gTIzoc4QKBgQDd7ZrNFs75WMOa7DZB9BEGkoYInyYdByQEmIAMedpFSYzjUw5IcBOfViT1vtmqqVVP9LLYpE1lw0EjauJQbEMxAkDClqaiZHoN35Jtb6MHl/Fikua2GWDMYspbhCH87xCOqZbSUOI9tEBHXfTSP+E+b2NjAoUIBxPdFHNUWfDw4wKBgQDDjwILtNjPjWhzSdDqxVL5WidJvFXo2uToaNP3QHGT+hLLkOzX7aQ4068VUdnrD7tdgS8IpygddJlobYk7LnqHydGEPlbjwq8APyrp+dcC7344TAJ2vZ3UiSw2n+yLea11/97CM1A5VxX5Tl/l7pNVVpuB2Gkh06LA93eC/4BBcQKBgFLQv9uYj1PtE9FUJEsoUNIrC2lcOlTKvV4Zqm79ab5BXBnYfJP7ftcOxmwN9BwDZJqZrcO6TEeVPVGCKUHppIk9Vf1HSpd+9y0GKRY9epDy2lj2iLGbdsvOccUE4coJTBxwnJ+PH133TaVUIm/y9pLd7jCcWBhOy9LMNt1bUtYRAoGBALb2oa9aME9fug3DTleY7pylIIT4VFhcFDbtfQOjo6ATJGqY2T7c8/zG8NQ9lGk8esxtBRhPbFTCtpThhrd8MN/Cjufve1P03c6ZSwlF20i/0ZPSaau2lGg5PsiMr79+xC/WiQ+g+rnrJH/wOADkSaxaL3w4gYRIgcmQvpwteE3BAoGBAIp23qk2ZoVpAWtpv87poUzl+nlRb7q5Gsa+Da5iW5lRbbdrEb6SbAm+lgNasgRSfdi2aUjcQ+x+Q+jETFNu53HOX7WxHVuRy34wN4ToORssW/7i8BcpkrBu9ltG5VDSQGJMKeNVxR9EYn5Z85wJUBdEA0vFos+tLFUygRHwURsz";

则参与排序的参数如下：
extendUserId=user12345
loanAppId=order12345
排序连接后的结果为 extendUserId=user12345&loanAppId=order12345
拼装后的结果为 /api/v1/demo/test/get?extendUserId=user12345&loanAppId=order12345@
加密后生成的sigature 为：Q+u7WM+eDXCUjhoSHfPcVI3tqbr5eJF1mHhoWPCVuU98tD8F0te0oSh1rPoYyC4MYzVvvsZROA0tVJnoXMNDtEujXR31TqAPcIMAlX/MTh3MPj/haF5qL8y/NE2pPwG8cSO1610TjNVO2Mf05T4upOT00RheZuzqq0LeNgzBpzIUaN+0OplV1tSFLIJIT3uYzgz8++pyhrE/S2qXbwJ2UWrlGbF/N5kNx0xqhA76zP5Ro97PZyAmZi2C1j3FMmv+wKKn18f2RFE6LiwIP73ycBKq3R/AqmwqR2q6n8VlvnXM+Q/VEaCGqo9IXmCc7RY54rQlhtJiiHtzOvvIMyULaA==
```

### 加密nonce

如果请求有body数据为了防止body数据被篡改，服务器需要验证http header 里面的nonce数据是否合法。

```shell
RAS(nonce,中证提供的公钥(r_pub_key))
```

### 一个完整的例子

原始的请求

```shell
请求URI
/api/v1/demo/test/post
请求body
{"userId":"user1234","loanAppId":"1100000","bankNum":"6222081991087490987"}
```

随机生成nonce

```shell
t&DDSH8tdNWhM&Pf7G$f
```

加密后的nonce

```shell
HtPgqBYgcORsvrqKcpSR8L+/iYv+vYQnaFDq8X5arKc0k/AHyLJsRToavKHo8Q4EQJBC7Xv/9+N1HW3hyWbJIrBIr7Nvf3ss4oUk+UR8vlGDplCIRyiD+BFgsPFqQW2gw1gEkqrl3HdQFS05DdhFmP5XvuDKhH6/47ep95PII16fb8qEynwGAZTetoWVRbGNt62rs/0B5b8X+C3oaLKLOh/3iKJQKCRHAaUMfTbeMTLrQF7tbCcUBKt6m2I7S34yYvBnJ/JGgTZrbi/v52UpdBygrAZdkDCqQ/RB0iE4d8/pP/ui/MqqqSrHxMOQp3kllLr7L5fxfVGJRFfGRb/0hQ==
```

DES 加密后的body为:

```shell
VDxF3L+q0Ve8gTX2Sb/uBWX7bG7z9JfVM21gwlgMM4kX7N/9sFjVIskfwDMvx2RCz/2EUThT9cE5Tlc2MEzf4ldgNH2lTw3mWPPEYpbw/MA=
```

生成签名数据:

```shell
待加密数据
/api/v1/demo/test/post@VDxF3L+q0Ve8gTX2Sb/uBWX7bG7z9JfVM21gwlgMM4kX7N/9sFjVIskfwDMvx2RCz/2EUThT9cE5Tlc2MEzf4ldgNH2lTw3mWPPEYpbw/MA=
加密后数据
fuRDt+IQ0e83DB+0YZU7Ih/4pqSYxpqrEprNpyx742rTGJjKKVVbByY38II+3iJXKrw+Fk8buRw72ND+XUhIAaDzmSTgk6AOrFyufB7GO9orTTiiDWXxPL1Wxk4WnlVOdu10R7LF+5a8lMgxbRFTka0UvedNINHn2CXW/xOKEb9zjJBzQ63GG6uU2ZNGtQLYan7fSxnvsHOUXUitgzD1Y1XosEmCFrIrM2V3nYtEQCAcRRhGwvV9tTF5w+fhDHjyeDVwB5QwDFQO4YkXZgnVSK4N5y3677OIU2oMw1XaIUDDjbi9IzcB6S/BXusSOBFZ3zcsp2q0BegnK47+qlRX+g==
```

组装请求

```shell
Request url：
https://{{host}}/api/v1/demo/test/post

Header:
signature: fuRDt+IQ0e83DB+0YZU7Ih/4pqSYxpqrEprNpyx742rTGJjKKVVbByY38II+3iJXKrw+Fk8buRw72ND+XUhIAaDzmSTgk6AOrFyufB7GO9orTTiiDWXxPL1Wxk4WnlVOdu10R7LF+5a8lMgxbRFTka0UvedNINHn2CXW/xOKEb9zjJBzQ63GG6uU2ZNGtQLYan7fSxnvsHOUXUitgzD1Y1XosEmCFrIrM2V3nYtEQCAcRRhGwvV9tTF5w+fhDHjyeDVwB5QwDFQO4YkXZgnVSK4N5y3677OIU2oMw1XaIUDDjbi9IzcB6S/BXusSOBFZ3zcsp2q0BegnK47+qlRX+g==

nonce:HtPgqBYgcORsvrqKcpSR8L+/iYv+vYQnaFDq8X5arKc0k/AHyLJsRToavKHo8Q4EQJBC7Xv/9+N1HW3hyWbJIrBIr7Nvf3ss4oUk+UR8vlGDplCIRyiD+BFgsPFqQW2gw1gEkqrl3HdQFS05DdhFmP5XvuDKhH6/47ep95PII16fb8qEynwGAZTetoWVRbGNt62rs/0B5b8X+C3oaLKLOh/3iKJQKCRHAaUMfTbeMTLrQF7tbCcUBKt6m2I7S34yYvBnJ/JGgTZrbi/v52UpdBygrAZdkDCqQ/RB0iE4d8/pP/ui/MqqqSrHxMOQp3kllLr7L5fxfVGJRFfGRb/0hQ==

Content-type:application/json
channelCode:LIAN_LIAN
Method: POST
```



## 解密

请求返回body数据是经过服务器加过密的 加密算法为

```shell
RAS(body,l_pub_key(用户提供给中证本地生成的公钥))
```

所以解密需要使用用户本地保存的私钥来解密

```shell
decryptRSA(body,l_prv_key)
```



# 测试接口1

### 请求资源名URI
`/v1/demo/test/get`

### 请求方法

`GET`

### 请求参数

| 字段名称            | 类型       | 是否必填 | 字段说明                                         |
| ------------------- | ---------- | -------- | ------------------------------------------------ |
| loanAppId    | String     | 是       | 贷款申请ID                            |
| extendTradeId     | String     | 是       | 外部交易ID                                       |

### 返回data字段明细

| 字段名          | 类型   | 是否必填 | 字段说明     |
| --------------- | ------ | -------- | ------------ |
| content | String | 是        | 内容 |



### 请求报文示例

无

### 返回报文示例

```json
{
    "code": "SUCCESS",
    "message": null,
    "data": {
        "content": "测试成功"
    }
}
```



# 测试接口2

### 请求资源名URI

`/v1/demo/test/post`

### 请求方法

`POST`

### 请求字段明细

| 字段名称     | 类型       | 是否必填 | 字段说明                           |
| ------------ | ---------- | -------- | ---------------------------------- |
| loanAppId    | String     | 是       | 贷款申请ID                         |
| personalInfo | JsonObject | 是       | [个人信息](#personalInfo 个人信息) |

#### personalInfo 个人信息

| 字段名称 | 类型   | 是否必填 | 字段说明 |
| -------- | ------ | -------- | -------- |
| name     | String | 是       | 姓名     |
| age      | Int    | 是       | 年龄     |

### 返回data字段明细

| 字段名  | 类型   | 是否必填 | 字段说明 |
| ------- | ------ | -------- | -------- |
| content | String | 是       | 内容     |



### 请求报文示例

```json
{
    "loanAppId": "xxxxxx",
    "personalInfo": {
        "name": "xxx",
        "age": 35
    }
}
```



### 返回报文示例

```json
{
    "code": "SUCCESS",
    "message": null,
    "data": {
        "content": "测试成功"
    }
}
```


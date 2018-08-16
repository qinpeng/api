package com.csci.demo.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {

  private static ObjectMapper objectMapper = null;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static String toJsonStr(Object object) {
    String result = null;
    try {
      result = objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static <T> T json2Object(String jsonStr, Class<T> tClass, Class<?>... elementClass) {
    JavaType javaType = objectMapper.getTypeFactory().constructParametricType(tClass, elementClass);
    T t = null;
    try {
      t = objectMapper.readValue(jsonStr, javaType);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return t;
  }

  public static <T> T json2Object(String jsonStr, Class<T> tClass) {
    T t = null;
    try {
      t = objectMapper.readValue(jsonStr, tClass);
    } catch (IOException e) {
    }
    return t;
  }

  public static Map<String, Object> json2map(String jsonStr) {
    Map<String, Object> tempMap = null;
    try {
      tempMap = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return tempMap;
  }

    /*public static JSONObject parseObject(String jsonText){
        return JSONObject.parseObject(jsonText);
    }*/

  public static <T> List<T> json2Array(String jsonStr, Class<T> tClass) {
    List<T> t = null;
    CollectionType javaType = objectMapper.getTypeFactory()
        .constructCollectionType(List.class, tClass);
    try {
      t = objectMapper.readValue(jsonStr, javaType);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return t;
  }

  /**
   * 获取多重嵌套json内部的值
   *
   * @param key 由外到内每个json对应的key
   */
  public static String getValue(String jsonStr, String... key) {
    String result = null;
    JsonNode tmpNode = null;
    try {
      tmpNode = objectMapper.readTree(jsonStr);
    } catch (IOException e) {
      e.printStackTrace();
    }
    int length = key.length;

    for (int i = 0; i < length; i++) {
      String ele = key[i];
      tmpNode = tmpNode.get(ele);
      if (null == tmpNode || i == length - 1) {
        break;
      }
    }
    if (!tmpNode.isValueNode()) {
      try {
        result = objectMapper.writeValueAsString(tmpNode);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        result = null;
      }
    } else {
      result = tmpNode.asText(null);
    }

    return result;
  }

    /*public static void main(String [] args){
//        String result ="{\"corpus\":13000,\"sumAmt\":100000,\"payerName\":\"张三\",\"borrowDate\":1493793932592,\"externalId\":\"12312312\",\"interestDetails\":[{\"borrowDays\":\"150\",\"interest\":0.1000000000000000055511151231257827021181583404541015625,\"penaltyInterest\":0.200000000000000011102230246251565404236316680908203125,\"interestDate\":\"120\",\"description\":\"备注0\"},{\"borrowDays\":\"151\",\"interest\":1.100000000000000088817841970012523233890533447265625,\"penaltyInterest\":1.1999999999999999555910790149937383830547332763671875,\"interestDate\":\"121\",\"description\":\"备注1\"}]}";
        String result ="{\"channelId\":1234,\"nonce\":100000,\"signature\":\"张三\",\"bizObj\":{\"borrowDays\":\"150\",\"interest\":0.1000000000000000055511151231257827021181583404541015625,\"penaltyInterest\":0.200000000000000011102230246251565404236316680908203125,\"interestDate\":\"120\",\"description\":\"备注0\"}}";
//        String paymentPlans = JsonUtil.getValue(result,"interestDetails");
        RequestVo<InterestDetail> requestVo = JsonUtil.json2Object(result, RequestVo.class, InterestDetail.class);
        System.out.println(requestVo.getBizObj().toString());
//        JsonUtil.json2Array(paymentPlans,InterestDetail.class);
////        System.out.println("safdsafsaf="+String.valueOf(null));
//
//        RepaymentPlanVo repaymentPlanVo = new RepaymentPlanVo();
//        repaymentPlanVo.setExternalId("12312312");
//        repaymentPlanVo.setBorrowDate(new Date());
//        repaymentPlanVo.setSumAmt(new BigDecimal(100000));
//        repaymentPlanVo.setCorpus(new BigDecimal(13000));
//        repaymentPlanVo.setPayerName("张三");
//        List<InterestDetail> interestDetails = new ArrayList<>();
//        for(int i=0;i<2;i++) {
//            InterestDetail interestDetail = new InterestDetail();
//            interestDetail.setBorrowDays("15"+i);
//            interestDetail.setPenaltyInterest(new BigDecimal(0.2+i));
//            interestDetail.setDescription("备注"+i);
//            interestDetail.setInterest(new BigDecimal(0.1+i));
//            interestDetail.setInterestDate("12"+i);
//            interestDetails.add(interestDetail);
//        }
//        repaymentPlanVo.setInterestDetails(interestDetails);
//        String jsonStr = JsonUtil.toJsonStr(repaymentPlanVo);
//        System.out.println(">>>>>>>>>>json string of repaymentPlanVo="+jsonStr);
//        RepaymentPlanVo repaymentPlanVo1 = JsonUtil.json2Object(result, RepaymentPlanVo.class);
//        System.out.println("==========="+repaymentPlanVo.toString()+"============");
    }*/

}
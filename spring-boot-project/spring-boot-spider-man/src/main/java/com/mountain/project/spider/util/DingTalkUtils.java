package com.mountain.project.spider.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Slf4j
public class DingTalkUtils {

    private static final String URL = "https://oapi.dingtalk.com/robot/send";

    private final static String SECRET = "SEC727e40bba472e46c3dc8dec3e4d7d04178194b9691f8355df176980d8f69c55a";

    private final static String ACCESS_TOKEN = "c3b6ba5c564d811afb9a1f30108c4cbb2a0956da9be90742ef6af9e15f1b8b00";

    public static String sign(long timestamp) {
        String stringToSign = timestamp + "\n" + SECRET;
        String sign = null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
            sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (Exception e) {
            log.info("获取钉钉机器人签名失败");
        }
        return sign;
    }

    public static void notification(String message) {
        try {
            RestTemplate restTemplate = SpringContextUtils.getBean("restTemplate");

            long timestamp = System.currentTimeMillis();
            String sign = sign(timestamp);
            String url = URL + "?access_token=" + ACCESS_TOKEN + "&timestamp=" + timestamp + "&sign=" + sign;

            //headers
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            //body
            Map<String, String> content = new HashMap<>();
            content.put("content", message);
            JSONObject param = new JSONObject();
            param.put("msgtype", "text");
            param.put("text", JSON.toJSON(content));

            HttpEntity<JSONObject> requestEntity = new HttpEntity<>(param, requestHeaders);
            ResponseEntity responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            HttpStatus httpStatus = responseEntity.getStatusCode();
            if (HttpStatus.OK.equals(httpStatus)) {
                log.info("成功发送钉钉通知");
            } else {
                log.error("发送钉钉通知失败");
            }
        } catch (Exception ex) {
            log.error("DingTalkUtils.notification:", ex);
        }
    }
}

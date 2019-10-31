package com.trabricks.web.notice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.common.collect.Maps;
import com.trabricks.web.notice.properties.FirebaseProperties;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author eomjeongjae
 * @since 2019/10/31
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(FirebaseProperties.class)
public class DefaultFirebaseMessageServiceImpl implements FirebaseMessageService {

  private final FirebaseProperties firebaseProperties;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Async
  public void sendMessage(FirebaseMessage firebaseMessage) {
    try {
      HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(
          buildNotificationMessage(firebaseMessage), getHeaders());
      ResponseEntity<String> responseEntity = restTemplate.postForEntity(
          firebaseProperties.getBaseUrl() + firebaseProperties.getFcmSendEndpoint(), httpEntity,
          String.class);
      log.info("statusCode: {}", responseEntity.getStatusCode());
      log.info("headers: {}", responseEntity.getHeaders());
      log.info("body: {}", responseEntity.getBody());
    } catch (HttpClientErrorException e) {
      log.info("statusCode: {}", e.getStatusCode());
      log.info("body: {}", e.getResponseBodyAsString());
      log.error("Firebase send message client error", e);
    } catch (HttpServerErrorException e) {
      log.info("statusCode: {}", e.getStatusCode());
      log.info("body: {}", e.getResponseBodyAsString());
      log.error("Firebase send message server error", e);
    } catch (Exception e) {
      log.error("Firebase send message error", e);
    }
  }

  private HttpHeaders getHeaders() throws IOException {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + getAccessToken());
    headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
    return headers;
  }

  private String getAccessToken() throws IOException {
    ClassPathResource classPathResource = new ClassPathResource(
        firebaseProperties.getPrivateKeyPath());
    GoogleCredential googleCredential = GoogleCredential
        .fromStream(classPathResource.getInputStream())
        .createScoped(firebaseProperties.getScopes());
    googleCredential.refreshToken();
    return googleCredential.getAccessToken();
  }

  private Map<String, Object> buildNotificationMessage(FirebaseMessage firebaseMessage)
      throws JsonProcessingException {
    Map<String, Object> data = Maps.newHashMap();
    data.put("large_icon", firebaseProperties.getHost() + firebaseMessage.getPushImageLink());
    data.put("title", firebaseMessage.getTitle());
    data.put("body", firebaseMessage.getBody());
    data.put("uri", firebaseMessage.getLink());

    Map<String, Object> androidData = Maps.newHashMap();
    androidData.put("customNotification", "true");
    androidData.put("icon", "ic_notify");
    androidData.put("color", "#f1545f");
    androidData.put("sound", "default");
    androidData.put("channel", "default");

    Map<String, Object> android = Maps.newHashMap();
    android.put("data", androidData);
    android.put("priority", "high");

    Map<String, Object> apnsPayloadAps = Maps.newHashMap();
    apnsPayloadAps.put("alert", "새로운 알림이 있습니다.");
    apnsPayloadAps.put("mutable-content", 1);
    apnsPayloadAps.put("sound", "default");

    Map<String, Object> apnsPayload = Maps.newHashMap();
    apnsPayload.put("aps", apnsPayloadAps);

    Map<String, Object> apns = Maps.newHashMap();
    apns.put("payload", apnsPayload);

    Map<String, Object> message = Maps.newHashMap();
    message.put("data", data);
    message.put("android", android);
    message.put("apns", apns);
    message.put("token", firebaseMessage.getToken());

    Map<String, Object> cloudMessage = Maps.newHashMap();
    cloudMessage.put(firebaseProperties.getMessageKey(), message);
    log.info("cloudMessageJson: {}", objectMapper.writeValueAsString(cloudMessage));
    return cloudMessage;
  }

}

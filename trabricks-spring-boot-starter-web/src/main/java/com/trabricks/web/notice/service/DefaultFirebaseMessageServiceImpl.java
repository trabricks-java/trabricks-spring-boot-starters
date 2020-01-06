package com.trabricks.web.notice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.collect.Maps;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.trabricks.web.notice.properties.FirebaseProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.PostConstruct;
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

  // TODO: Firebase SDK 작업 추가 예정.
  @PostConstruct
  public void init() {
    log.info("DefaultFirebaseMessageServiceImpl init");
    try {
      InputStream serviceAccount =
          new ClassPathResource(firebaseProperties.getPrivateKeyPath())
              .getInputStream();

      FirebaseOptions options = new FirebaseOptions.Builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .setDatabaseUrl(firebaseProperties.getDatabaseUrl())
          .build();

      FirebaseApp.initializeApp(options);
    } catch (IOException e) {
      log.error("Error FirebaseApp initializeApp", e);
    }
  }

  @Override
  public void sendMessage(Message message) {
    try {
      // Response is a message ID string
      String response = FirebaseMessaging.getInstance().send(message);
      log.info("response: {}", response);
    } catch (FirebaseMessagingException e) {
      log.error("Firebase send message error", e);
    }
  }

  @Override
  public void sendMessage(FirebaseMessage firebaseMessage) {
    Map<String, Object> cloudMessage = buildNotificationMessage(firebaseMessage);
    try {
      log.info("cloudMessageJson: {}", objectMapper.writeValueAsString(cloudMessage));
    } catch (Exception e) {
      // only log
    }
    sendMessage(cloudMessage);
  }

  @Async
  @Override
  public void sendMessage(Map<String, Object> firebaseMessage) {
    try {
      HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(
          firebaseMessage, getHeaders());
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

  private Map<String, Object> buildNotificationMessage(FirebaseMessage firebaseMessage) {
    Map<String, Object> notification = Maps.newHashMap();
    notification.put("body", firebaseMessage.getBody());
    notification.put("title", firebaseMessage.getTitle());

    Map<String, Object> data = Maps.newHashMap();
    data.put("link", firebaseMessage.getLink());

    Map<String, Object> message = Maps.newHashMap();
    message.put("token", firebaseMessage.getToken());
    message.put("notification", notification);
    message.put("data", data);

    Map<String, Object> cloudMessage = Maps.newHashMap();
    cloudMessage.put("message", message);

    return cloudMessage;
  }

}

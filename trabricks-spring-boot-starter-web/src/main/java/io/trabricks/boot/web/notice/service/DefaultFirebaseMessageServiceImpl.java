package io.trabricks.boot.web.notice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import io.trabricks.boot.web.notice.properties.FirebaseProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.CollectionUtils;

/**
 * @author eomjeongjae
 * @since 2019/10/31
 */
@EnableAsync
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(FirebaseProperties.class)
public class DefaultFirebaseMessageServiceImpl implements FirebaseMessageService {

  private final FirebaseProperties firebaseProperties;
  private final ObjectMapper objectMapper;

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

      if (CollectionUtils.isEmpty(FirebaseApp.getApps())) {
        FirebaseApp.initializeApp(options);
      }
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
  public void sendMessage(List<Message> messages) {
    try {
      // Response is a message ID string
      BatchResponse response = FirebaseMessaging.getInstance().sendAll(messages);
      log.info("successCount: {}", response.getSuccessCount());
      log.info("failureCount: {}", response.getFailureCount());

      response.getResponses()
          .forEach(sendResponse -> {
            log.info("messageId: {}", sendResponse.getMessageId());
            log.info("isSuccessful: {}", sendResponse.isSuccessful());
            log.info("exception: {}", sendResponse.getException());
          });
    } catch (FirebaseMessagingException e) {
      log.error("Firebase send message error", e);
    }
  }

}

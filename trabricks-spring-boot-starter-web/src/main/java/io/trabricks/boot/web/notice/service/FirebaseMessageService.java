package io.trabricks.boot.web.notice.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */

public interface FirebaseMessageService {

  @Async
  CompletableFuture<String> sendMessage(Message message);

  @Async
  CompletableFuture<BatchResponse> sendMessage(List<Message> messages);

}

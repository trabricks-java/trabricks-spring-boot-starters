package io.trabricks.boot.web.notice.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;

/**
 * The interface Firebase message service.
 *
 * @author eomjeongjae
 * @since 2019 -09-24
 */
public interface FirebaseMessageService {

  /**
   * Send message completable future.
   *
   * @param message the message
   * @return the completable future
   */
  @Async
  CompletableFuture<String> sendMessage(Message message);

  /**
   * Send message completable future.
   *
   * @param messages the messages
   * @return the completable future
   */
  @Async
  CompletableFuture<BatchResponse> sendMessage(List<Message> messages);

}

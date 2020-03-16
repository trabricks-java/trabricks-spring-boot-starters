package io.trabricks.boot.web.notice.service;

import com.google.firebase.messaging.Message;
import java.util.List;
import org.springframework.scheduling.annotation.Async;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */

public interface FirebaseMessageService {

  @Async
  void sendMessage(List<Message> messages);

  @Async
  void sendMessage(Message message);

}

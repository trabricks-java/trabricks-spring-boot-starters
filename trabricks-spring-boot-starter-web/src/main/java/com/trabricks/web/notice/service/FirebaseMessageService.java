package com.trabricks.web.notice.service;

import com.google.firebase.messaging.Message;
import java.util.List;
import java.util.Map;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */

public interface FirebaseMessageService {

  void sendMessage(List<Message> messages);

  void sendMessage(Message message);

  void sendMessage(FirebaseMessage firebaseMessage);

  void sendMessage(Map<String, Object> firebaseMessage);

}

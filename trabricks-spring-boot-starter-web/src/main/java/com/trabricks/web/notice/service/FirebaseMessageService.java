package com.trabricks.web.notice.service;

import java.util.Map;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */

public interface FirebaseMessageService {

  void sendMessage(FirebaseMessage firebaseMessage);

  void sendMessage(Map<String, Object> firebaseMessage);

}

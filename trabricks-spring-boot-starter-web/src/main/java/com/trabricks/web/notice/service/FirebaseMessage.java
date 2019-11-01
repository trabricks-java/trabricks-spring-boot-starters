package com.trabricks.web.notice.service;

/**
 * @author eomjeongjae
 * @since 2019-09-24
 */
public interface FirebaseMessage {

  String getTitle();

  String getBody();

  String getLink();

  String getPushImageLink();

  String getToken();

}
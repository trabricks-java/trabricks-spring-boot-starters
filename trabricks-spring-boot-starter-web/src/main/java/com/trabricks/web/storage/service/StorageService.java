package com.trabricks.web.storage.service;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author eomjeongjae
 * @since 2019-09-23
 */
public interface StorageService {

  void init();

  void store(MultipartFile file, String serverFilename);

  Stream<Path> loadAll();

  Path load(String filename);

  Resource loadAsResource(String filename);

  void deleteAll();

}

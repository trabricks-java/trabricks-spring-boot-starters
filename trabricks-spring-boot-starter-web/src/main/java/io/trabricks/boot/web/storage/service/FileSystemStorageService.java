package io.trabricks.boot.web.storage.service;

import io.trabricks.boot.web.storage.exceptions.StorageException;
import io.trabricks.boot.web.storage.exceptions.StorageFileNotFoundException;
import io.trabricks.boot.web.storage.properties.StorageProperties;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type File system storage service.
 *
 * @author eomjeongjae
 * @since 2019 /10/15
 */
@Slf4j
public class FileSystemStorageService implements StorageService {

  private final Path rootLocation;
  private final StorageProperties storageProperties;

  /**
   * Instantiates a new File system storage service.
   *
   * @param properties the properties
   */
  @Autowired
  public FileSystemStorageService(StorageProperties properties) {
    this.rootLocation = Paths.get(properties.getLocation());
    this.storageProperties = properties;
  }

  @Override
  public void store(MultipartFile file, Path path) {
    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    try {
      if (file.isEmpty()) {
        throw new StorageException("Failed to store empty file " + filename);
      }
      if (filename.contains("..")) {
        // This is a security check
        throw new StorageException(
            "Cannot store file with relative path outside current directory "
                + filename);
      }

      Path target = this.rootLocation.resolve(path);
      log.info("target: {}", target);
      log.info("Files.isDirectory(target): {}", Files.isDirectory(target));
      if (Files.isDirectory(target)) {
        throw new StorageException(
            "The path is not a file " + target);
      }

      createDirectory(target.getParent());
      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, target,
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      throw new StorageException("Failed to store file " + filename, e);
    }
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.rootLocation, 1)
          .filter(path -> !path.equals(this.rootLocation))
          .map(this.rootLocation::relativize);
    } catch (IOException e) {
      throw new StorageException("Failed to read stored files", e);
    }
  }

  @Override
  public Path load(String filename) {
    return rootLocation.resolve(filename);
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = load(filename);
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new StorageFileNotFoundException(
            "Could not read file: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new StorageFileNotFoundException("Could not read file: " + filename, e);
    }
  }

  @Override
  public void deleteAll() {
    // nothing
    throw new UnsupportedOperationException();
  }

  @Override
  public void init() {
    try {
      Files.createDirectories(rootLocation);
    } catch (IOException e) {
      throw new StorageException("Could not initialize storage", e);
    }
  }

  private void createDirectory(Path path) throws IOException {
    try {
      Files.createDirectories(path);
    } catch (FileAlreadyExistsException e) {
      log.error("File already exists: {}", e.getMessage());
    }
  }

}

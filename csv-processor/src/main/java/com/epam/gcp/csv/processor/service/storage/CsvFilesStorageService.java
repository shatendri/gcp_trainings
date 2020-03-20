package com.epam.gcp.csv.processor.service.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class CsvFilesStorageService implements StorageService {

  private static final String CSV_FILES_SUB_DIR = "csv_files_for_processing";

  private final ResourceLoader resourceLoader;

  public CsvFilesStorageService(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public Path store(InputStream inputStream, String pathToFileInBucket) throws IOException {
    Path pathToFile =
        Paths.get(
            getStorageRootDirectory(),
            CSV_FILES_SUB_DIR,
            pathToFileInBucket
        );

    Files.createDirectories(pathToFile.getParent())
        .resolve(pathToFile.getFileName());

    Files.copy(inputStream, pathToFile);

    return pathToFile;
  }

  private String getStorageRootDirectory() throws IOException {
    return resourceLoader.getResource("/").getFile().getAbsolutePath();
  }
}

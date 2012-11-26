package com.xtremelabs.robolectric.res;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStream;

public class RawResourceLoader {

  private ResourceExtractor resourceExtractor;
  private File resourceDir;

  public RawResourceLoader(ResourceExtractor resourceExtractor, File resourceDir) {
    this.resourceExtractor = resourceExtractor;
    this.resourceDir = resourceDir;
  }

  public InputStream getValue(int resourceId) {
    String resourceFileName = resourceExtractor.getResourceName(resourceId);
    String resourceName = resourceFileName.substring("/raw".length());

    File rawResourceDir = new File(resourceDir, "raw");

    try {
      InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourceFileName);
      if (resourceAsStream == null) {
        File[] files = rawResourceDir.listFiles(new ResourceFilenameFilter(resourceName));
        if (files != null && files.length > 0) {
          return new FileInputStream(files[0]);
        }
      }
    }
    catch (FileNotFoundException e) {
      return null;
    }
    return null;
  }

  private static class ResourceFilenameFilter implements FilenameFilter {

    private final String resourceName;

    public ResourceFilenameFilter(String resourceName) {
      this.resourceName = resourceName;
    }

    @Override
    public boolean accept(File dir, String name) {
      String fileBaseName = getFileBasename(name);
      return fileBaseName.equals(resourceName);
    }

    private String getFileBasename(String name) {
      String fileBaseName;
      int dotIndex = name.indexOf(".");
      if (dotIndex >= 0) {
        fileBaseName = name.substring(0, dotIndex);
      }
      else {
        fileBaseName = name;
      }
      return fileBaseName;
    }
  }
}

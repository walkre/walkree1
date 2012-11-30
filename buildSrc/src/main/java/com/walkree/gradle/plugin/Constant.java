package com.walkree.gradle.plugin;

/**
 * This class defines all the constants used by the plugin.
 */
public class Constant {
  public static final String PROJECT_ROOT_SYMBOL = "//";
  public static final String TARGET_SEPARATOR = ":";
  public static final String JOB_SEPARATOR = "~";
  public static final String BUILD_PATH= "build";
  public static final String JAVA_BUILD_PATH = BUILD_PATH + "/java";
  public static final String JAVA_CLASS_PATH = JAVA_BUILD_PATH + "/class";
  public static final String JAVA_LIB_PATH = JAVA_BUILD_PATH + "/lib";
  public static final String JAVA_CACHE_PATH = JAVA_BUILD_PATH + "/cache";
  public static final String JAVA_SOURCE_COMPATIBILITY = "1.6";
  public static final String JAVA_TARGET_COMPATIBILITY = "1.6";
}

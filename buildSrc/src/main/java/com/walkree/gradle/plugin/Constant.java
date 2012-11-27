package com.walkree.gradle.plugin;

/**
 * This class defines all the constants used by the plugin.
 */
public class Constant {
  public static final String PROJECT_ROOT_SYMBOL = "//";
  public static final String TARGET_SEPARATOR = ":";
  public static final String JOB_SEPARATOR = "?";
  public static final String DISTPATH = "dist";
  public static final String JAVA_DISTPATH = DISTPATH + "/java";
  public static final String JAVA_CLASSPATH = JAVA_DISTPATH + "/class";
  public static final String JAVA_LIBPATH = JAVA_DISTPATH + "/lib";
  public static final String JAVA_SOURCE_COMPATIBILITY = "1.6";
  public static final String JAVA_TARGET_COMPATIBILITY = "1.6";
  public static final String JAVA_CACHEPATH = JAVA_DISTPATH + "/cache";
}

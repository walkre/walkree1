package com.walkree.gradle.plugin.java;

import java.io.File;
import java.util.concurrent.Callable;

import org.gradle.api.file.FileCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;
import com.walkree.gradle.plugin.java.JavaCompileJob;

/**
 * This class represents Java libraries.
 */
public class JavaLibraryTarget extends Target {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(JavaLibraryTarget.class);

  /**
   * Create a Java library target in a given package.
   * @param   pkg   The given package.
   * @param   name  The name of the target.
   */
  public JavaLibraryTarget(Package pkg, String name) {
    super(pkg, name);
  }

  @Override
  public void initialize() {
    JavaCompileJob compile =
        new JavaCompileJob(this, "JavaCompile")
            .setSourceFiles(getSourceFiles())
            .setClasspath(getClasspath())
            .setDestinationDirectory(getDestinationDirectory())
            .setDependencyCacheDirectory(getDependencyCacheDirectory())
            .initialize();

    addJob(compile);
  }

  // Return the Java classpath represented by a set of files (directories).
  protected FileCollection getClasspath() {
    // Find all jar files in the JAVA_LIB_PATH directory. We use a callback here
    // because some jar files may not be available during configuration. For
    // example, some jar files may be produced by a target on which the current
    // target depends.
    Callable<FileCollection> getJars = new Callable<FileCollection>() {
      public FileCollection call() throws Exception {
        return getEnvironment().fileTree(Constant.JAVA_LIB_PATH);
      }
    };

    FileCollection jars = getEnvironment().files(getJars);
    File classDir = getEnvironment().file(Constant.JAVA_CLASS_PATH);
    return getEnvironment().files(classDir, jars);
  }

  // Return the destination directory.
  protected File getDestinationDirectory() {
    return getEnvironment().file(Constant.JAVA_CLASS_PATH);
  }

  // Return the directory for caching dependencies.
  protected File getDependencyCacheDirectory() {
    return getEnvironment().file(Constant.JAVA_CACHE_PATH);
  }
}

package com.walkree.gradle.plugin.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.gradle.api.file.FileCollection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.Constant;
import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;
import com.walkree.gradle.plugin.common.CopyJob;

/**
 * This class represents third party Java libraries.
 */
public class ThirdPartyJavaLibraryTarget extends Target {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(ThirdPartyJavaLibraryTarget.class);

  // The value of the jar file specified by the user.
  private Collection<String> mJarValues;

  /**
   * Create a third party Java library target in a given package.
   * @param   pkg   The given package.
   * @param   name  The name of the target.
   */
  public ThirdPartyJavaLibraryTarget(Package pkg, String name) {
    super(pkg, name);

    mJarValues = new ArrayList<String>();
  }

  @Override
  public void initialize() {
    CopyJob copyJars =
        new CopyJob(this, "CopyJars")
            .setFrom(getJarFiles())
            .setDestinationDirectory(getDestinationDirectory())
            .initialize();

    addJob(copyJars);
  }

  // Return the jar files that needs to be installed.
  protected FileCollection getJarFiles() {
    Collection<File> files = new ArrayList<File>();
    for (String jarValue : mJarValues) {
      File file = new File(getHomeDirectory(), jarValue);
      files.add(file);
    }

    return getEnvironment().files(files);
  }

  // Return the destination directory.
  protected File getDestinationDirectory() {
    return getEnvironment().file(Constant.JAVA_LIB_PATH);
  }

  /**
   * Configure the jars of this target. This function is invoked through the
   * build file specified by the user.
   * @param   values  The list of {@link String} jar values.
   */
  public void setJars(String[] values) {
    for (String value : values) {
      mJarValues.add(value);
    }
  }
}

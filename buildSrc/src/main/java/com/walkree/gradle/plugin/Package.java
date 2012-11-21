package com.walkree.gradle.plugin;

import java.io.File;
import java.lang.Iterable;
import java.util.HashMap;
import java.util.Map;

import groovy.lang.Closure;

import org.gradle.api.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.walkree.gradle.plugin.cpp.CppExecutableTarget;
import com.walkree.gradle.plugin.cpp.CppLibraryTarget;

public class Package {
  private static final Logger LOGGER = LoggerFactory.getLogger(Package.class);
  private Project project;
  private BuildSystem buildSystem;
  private File buildFile;
  private String packageRootDir;
  private String name;
  private Map<String, Target> targetMap;

  public Package(Project project, BuildSystem buildSystem, File buildFile) {
    this.project = project;
    this.buildSystem = buildSystem;
    this.buildFile = buildFile;
    packageRootDir = buildFile.getParentFile().getAbsolutePath();
    name = project.relativePath(packageRootDir);
    targetMap = new HashMap<String, Target>();
  }

  public Target findTarget(String targetName) {
    return targetMap.get(targetName);
  }

  public Project getProject() {
    return project;
  }

  public BuildSystem getBuildSystem() {
    return buildSystem;
  }

  public String getPackageRootDir() {
    return packageRootDir;
  }

  public String getName() {
    return name;
  }

  public Iterable<Target> getTargets() {
    return targetMap.values();
  }

  public void addTarget(Target target, Closure closure) {
    getProject().configure(target, closure);
    String name = target.getName();
    if (name == Target.DEFAULT_NAME) {
      LOGGER.warn("Must specify a name for BUILD targets in package '{}'\n",
          getPackageRootDir());
    }
    if (targetMap.containsKey(name)) {
      LOGGER.warn("Duplicted name '{}' in package '{}'\n", name, getPackageRootDir());
    }
    targetMap.put(name, target);
  }

  public void configure() {
    for (Target target : targetMap.values()) {
      target.configure();
    }
  }

  public String toString() {
    return getName();
  }

  /*
   * The following methods map to the target contructs in the BUILD file.
   * Thus, they do not follow the normal java code convention.
   */
  public void cpp_executable(Closure closure) {
    addTarget(new CppExecutableTarget(this), closure);
  }
  public void cpp_library(Closure closure) {
    addTarget(new CppLibraryTarget(this), closure);
  }
}

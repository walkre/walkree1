package com.walkree.gradle.plugin.cpp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Task;
import org.gradle.api.file.ConfigurableFileTree;
import org.gradle.api.tasks.Copy;
import org.gradle.plugins.binaries.model.Library;
import org.gradle.plugins.cpp.CppCompile;

import groovy.lang.Closure;

import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.cpp.clang.ClangLibraryCompileSpec;

public class CppLibraryTarget extends CppTarget {
  private static NamedDomainObjectContainer libraries;
  private Task task;
  private String[] exports;
  private String outputFileName;
  private Library library;

  public CppLibraryTarget(Package pkg) {
    super(pkg);
    if (libraries == null) {
      libraries = (NamedDomainObjectContainer)
          getProject().getExtensions().getByName("libraries");
    }
  }

  public String[] getExports() {
    return exports;
  }

  public void setExports(String[] exports) {
    this.exports = exports;
  }

  @Override
  public void configure() {
    Task task = null;
    if (getSources() != null && getSources().length > 0) {
      CppCompile compileTask = createCompileTask();      
      Library lib = (Library) libraries.create(getName());
      this.library = lib;
      lib.getSourceSets().add(createCppSourceSet());
      ClangLibraryCompileSpec spec = (ClangLibraryCompileSpec) lib.getSpec();
      setCompileSpec(spec);
      spec.configure(compileTask);      
      task = compileTask;
    }
    if (getExports() != null && getExports().length > 0) {
      Task exportTask = createExportTask();
      if (task != null) {
        // export files before compiling
        task.dependsOn(exportTask);
      } else {
        // export only task, e.g., all code goes to the header file
        task = exportTask;
      }
    }
    if (task == null) {
      throw new RuntimeException(String.format("Empty target: '%s'", this));
    }
    this.task = task;
  }

  @Override
  public String getTaskName() {
    return getName();
  }

  @Override
  public Task getTask() {
    return task;
  }

  @Override
  public Type getType() {
    return Type.CPP_LIBRARY;
  }

  public Library getLibrary() {
    return library;
  }  

  private Task createExportTask() {
    Map<String, Object> args = new HashMap<String, Object>();
    args.put("type", Copy.class);
    String name = String.format("%sExportFiles", getName());
    Copy task = (Copy) getProject().task(args, name);
    ConfigurableFileTree exportFileTree = getProject().fileTree(getPackage().getPackageRootDir());
    exportFileTree.include(getExports());
    task.from(exportFileTree.getFiles());
    task.setDestinationDir(getProject().file(String.format(
          "%s/exported/%s", getProject().getBuildDir(), getPackage().getName())));
    return task;
  }
}

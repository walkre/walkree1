package com.walkree.gradle.plugin.cpp.clang;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.tasks.DefaultTaskDependency;
import org.gradle.api.internal.tasks.compile.Compiler;
import org.gradle.api.tasks.TaskDependency;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.plugins.binaries.model.Binary;
import org.gradle.plugins.binaries.model.Library;
import org.gradle.plugins.binaries.model.NativeDependencySet;
import org.gradle.plugins.binaries.model.internal.CompileTaskAware;
import org.gradle.plugins.cpp.CppCompile;
import org.gradle.plugins.cpp.CppSourceSet;
import org.gradle.plugins.cpp.internal.CppCompileSpec;
import org.gradle.util.DeprecationLogger;

import groovy.lang.Closure;

public class ClangCompileSpec implements CppCompileSpec, CompileTaskAware {
  private Binary binary;
  private Project project;
  private final Compiler<? super ClangCompileSpec> compiler;
  private String outputFileName;
  private Task task;
  private List<Closure> settings = new LinkedList<Closure>();

  private final ConfigurableFileCollection includes;
  private final ConfigurableFileCollection source;
  private final ConfigurableFileCollection libs;

  public ClangCompileSpec(Binary binary, Compiler<? super ClangCompileSpec> compiler,
      Project project) {
    this.binary = binary;
    this.project = project;
    this.compiler = compiler;
    includes = project.files();
    source = project.files();
    libs = project.files();
 }

  @Override
  public void configure(CppCompile task) {
    this.task = task;
    task.setSpec(this);
    task.setCompiler(compiler);

    task.getOutputs().file(getOutputFile());

    for (CppSourceSet srcSet : binary.getSourceSets().withType(CppSourceSet.class)) {
      from(srcSet);      
    }
    
    // TODO(yunjing): use global constants for exported headers
    includes.from(project.getBuildDir() + "/exported/src/cpp"); 
  }

  @Override
  public File getOutputFile() {
    // TODO(yunjing): use global config
    return project.file("build/binaries/" + getOutputFileName());
  }

  public String getOutputFileName() {
    if (outputFileName == null) {
      outputFileName = OperatingSystem.current().getExecutableName(getName());
    }
    return outputFileName;
  }

  @Override
  public String getName() {
    return binary.getName();
  }

  @Override
  public TaskDependency getBuildDependencies() {
    return new DefaultTaskDependency().add(task);
  }

  @Override
  public File getWorkDir() {
    // TODO(yunjing): use global config
    return project.file("build/work/" + getName());
  }

  @Override
  public List<Closure> getSettings() {
    return settings;
  }

  public Iterable<File> getIncludeRoots() {
    return includes;
  }

  public Iterable<File> getSource() {
    return source;
  }

  public Iterable<File> getLibs() {
    return libs;
  }

  @Override
  public void libs(Iterable<Library> libs) {
    for (Library lib : libs) {
      addLib(lib);
    }    
  }

  public void addLib(Library lib) {
    libs.from(lib.getSpec().getOutputFile());
  }

  @Override
  public void from(CppSourceSet sourceSet) {
    // add src root dirs as header dir for unexported headers    
    includes.from(sourceSet.getSource().getSrcDirs());    
    source(sourceSet.getSource());
    libs(sourceSet.getLibs());
  }

  private void source(FileCollection files) {
    source.from(files);
    task.getInputs().source(files);
  }

  @Deprecated
  public void compile() {
    DeprecationLogger.nagUserOfDiscontinuedMethod("CompileSpec.compile()");
    compiler.execute(this);
  }
}

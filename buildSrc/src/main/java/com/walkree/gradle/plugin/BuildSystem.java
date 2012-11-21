package com.walkree.gradle.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gradle.api.file.FileCollection;
import org.gradle.api.Project;
import org.gradle.api.specs.Spec;

public class BuildSystem {
  private Project project;
  private Map<String, Package> pkgMap; // package root dir -> package
  private DependencyManager depManager;

  public BuildSystem(Project project) {
    this.project = project;
    pkgMap = new HashMap<String, Package>();
    depManager = new DependencyManager(this);
  }

  public void configure() {
    loadBuildFiles();
    configurePackages();
    resolveDependencies();
  }

  public Project getProject() {
    return project;
  }

  public Package findPackage(String packageName) {
    return pkgMap.get(packageName);
  }

  public List<Target> getDependencyTargets(Target target) {
    return depManager.getDependencyTargets(target);
  }

  /*
   * Load all the BUILD files in the src directory, one for each package.
   * A package maps to a directory with a BUILD file. Packages can be nested.
   */
  private void loadBuildFiles() {
    // Find all the BUILD files
    FileCollection buildFiles = project.fileTree(project.getRootDir());
    buildFiles = buildFiles.filter(new Spec<File>() {
      @Override
      public boolean isSatisfiedBy(File file) {
        // The per-project BUILD file must be named 'BUILD'        
        return file.isFile() && file.getName().equals("BUILD");
      }
    });
    for (File file: buildFiles.getFiles()) {
      Package pkg = new Package(project, this, file);
      Map<String, Object> options = new HashMap<String, Object>();      
      options.put("from", file);
      options.put("to", pkg);
      getProject().apply(options);
      pkgMap.put(pkg.getName(), pkg);
    }
  }
  
  private void configurePackages() {
    for (Package pkg : pkgMap.values()) {
      pkg.configure();
    }
  }

  private void resolveDependencies() {
    for (Package pkg : pkgMap.values()) {
      for (Target target : pkg.getTargets()) {
        depManager.resolveDependency(target);
      }
    }
  }
}

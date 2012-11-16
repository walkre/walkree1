package com.walkree.gradle.plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Plugin;
import org.gradle.api.file.FileCollection;
import org.gradle.api.Project;
import org.gradle.api.specs.Spec;
import org.gradle.plugins.cpp.CppPlugin;

//import com.walkree.gradle.plugin.cpp.clang.ClangCompileSpecFactory;

/**
 * Gradle plugin that supports the walkree internal BUILD system.
 *
 * @author Yunjing Xu (yunjing@walkree.com)
 */
public class WalkreePlugin implements Plugin<Project> {
  private Project project;
  private Map<File, Package> pkgMap; // BUILD file -> package

  public WalkreePlugin() {
    pkgMap = new HashMap<File, Package>();
  }

  @Override
  public void apply(Project project) {
    this.project = project;
    this.project.getPlugins().apply(CppPlugin.class);
    //ClangCompileSpecFactory.configure(getProject());

    loadBuildFiles();
  }

  public Project getProject() {
    return project;
  }

  /*
   * Load all the BUILD files in the src directory, one for each package.
   * A package maps to a directory with a BUILD file. Packages can be nested.
   * TODO(yunjing): use lazy loading instead of preloading the whole world.
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
      System.out.println(file);
      Package pkg = new Package(project, file);
      Map<String, Object> options = new HashMap<String, Object>();
      options.put("from", file);
      options.put("to", pkg);
      project.apply(options);
      pkgMap.put(file, pkg);
    }
  }
}

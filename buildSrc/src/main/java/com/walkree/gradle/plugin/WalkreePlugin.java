package com.walkree.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.plugins.cpp.CppPlugin;

//import com.walkree.gradle.plugin.cpp.clang.ClangCompileSpecFactory;

/**
 * Gradle plugin that supports the walkree internal BUILD system.
 *
 * @author Yunjing Xu (yunjing@walkree.com)
 */
public class WalkreePlugin implements Plugin<Project> {
  private Project project;
  private BuildSystem buildSystem;

  @Override
  public void apply(Project project) {
    this.project = project;
    this.project.getPlugins().apply(CppPlugin.class);
    //ClangCompileSpecFactory.configure(getProject());

    this.buildSystem = new BuildSystem(project);
    this.buildSystem.configure();
  }
}

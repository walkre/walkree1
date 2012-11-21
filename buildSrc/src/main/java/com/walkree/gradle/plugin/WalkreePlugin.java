package com.walkree.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import com.walkree.gradle.plugin.cpp.clang.ClangCompilerPlugin;

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
    System.out.println(project.getClass());
    this.project = project;    
    this.project.getPlugins().apply(ClangCompilerPlugin.class);

    this.buildSystem = new BuildSystem(project);
    this.buildSystem.configure();
  }
}

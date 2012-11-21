package com.walkree.gradle.plugin.cpp.clang;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.internal.Factory;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.plugins.binaries.BinariesPlugin;
import org.gradle.plugins.binaries.model.CompilerRegistry;
import org.gradle.plugins.binaries.model.internal.DefaultCompilerRegistry;
import org.gradle.plugins.cpp.CppExtension;
import org.gradle.process.internal.DefaultExecAction;
import org.gradle.process.internal.ExecAction;

public class ClangCompilerPlugin implements Plugin<Project> {

  public void apply(final Project project) {
    project.getPlugins().apply(BinariesPlugin.class);
    project.getExtensions().create("cpp", CppExtension.class, project);

    project.getExtensions().getByType(CompilerRegistry.class).add(new ClangCompilerAdapter(
        OperatingSystem.current(),
        new Factory<ExecAction>() {
          public ExecAction create() {
            ProjectInternal projectInternal = (ProjectInternal) project;
            return new DefaultExecAction(projectInternal.getFileResolver());
          }
        }));

    project.getExtensions().getByType(DefaultCompilerRegistry.class).setSpecFactory(
        new ClangCompileSpecFactory(project));
  }
}
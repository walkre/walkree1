package com.walkree.gradle.plugin.cpp.clang;

import org.gradle.api.Project;
import org.gradle.api.internal.tasks.compile.Compiler;
import org.gradle.plugins.binaries.model.Binary;
import org.gradle.plugins.binaries.model.Library;
import org.gradle.plugins.binaries.model.internal.BinaryCompileSpec;
import org.gradle.plugins.binaries.model.internal.BinaryCompileSpecFactory;

public class ClangCompileSpecFactory implements BinaryCompileSpecFactory {
  private Project project;

  public ClangCompileSpecFactory(Project project) {
    this.project = project;
  }

  public BinaryCompileSpec create(Binary binary, Compiler<?> compiler) {    
    Compiler<? super ClangCompileSpec> typed = (Compiler<? super ClangCompileSpec>) compiler;
    if (binary instanceof Library) {
      return new ClangLibraryCompileSpec(binary, typed, project);
    }
    return new ClangCompileSpec(binary, typed, project);
  }
}
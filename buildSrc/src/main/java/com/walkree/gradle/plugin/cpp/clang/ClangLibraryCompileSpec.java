package com.walkree.gradle.plugin.cpp.clang;

import org.gradle.api.Project;
import org.gradle.api.internal.tasks.compile.Compiler;
import org.gradle.plugins.binaries.model.Binary;
import org.gradle.plugins.binaries.model.LibraryCompileSpec;

public class ClangLibraryCompileSpec extends ClangCompileSpec implements LibraryCompileSpec {
  private String outputFileName;

  public ClangLibraryCompileSpec(Binary binary, Compiler<? super ClangCompileSpec> compiler,
      Project project) {
    super(binary, compiler, project);
  }

  @Override
  public String getOutputFileName() {
    if (outputFileName == null) {
      outputFileName = getName() + ".a";
    }
    return outputFileName;
  }

  @Override
  public String getInstallName() {
    return getOutputFileName();
  }

  @Override
  public void setInstallName(String installName) {
    outputFileName = installName;
  }
}
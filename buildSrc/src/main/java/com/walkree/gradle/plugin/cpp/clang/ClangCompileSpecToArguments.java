package com.walkree.gradle.plugin.cpp.clang;

import java.io.File;

import org.gradle.api.internal.tasks.compile.ArgCollector;
import org.gradle.api.internal.tasks.compile.CompileSpecToArguments;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.plugins.binaries.model.LibraryCompileSpec;

public class ClangCompileSpecToArguments implements CompileSpecToArguments<ClangCompileSpec> {
  public void collectArguments(ClangCompileSpec spec, ArgCollector collector) {
    collector.args("-Wall");
    collector.args("-Werror");
    collector.args("-o", spec.getOutputFile().getAbsolutePath());
    if (spec instanceof LibraryCompileSpec) {
      LibraryCompileSpec librarySpec = (LibraryCompileSpec) spec;
      //collector.args("-c");
      collector.args("-shared");
      if (!OperatingSystem.current().isWindows()) {
        collector.args("-fPIC");
        if (OperatingSystem.current().isMacOsX()) {
          collector.args("-Wl,-install_name," + librarySpec.getInstallName());
        } else {
          collector.args("-Wl,-soname," + librarySpec.getInstallName());
        }
      }
    } else {
      // executables
      collector.args("-o", spec.getOutputFile().getAbsolutePath());
    }
    
    for (File file : spec.getIncludeRoots()) {
      collector.args("-I");
      collector.args(file.getAbsolutePath());     
    }

    for (File file : spec.getSource()) {
      collector.args(file.getAbsolutePath());
    }

    for (File file : spec.getLibs()) {
      collector.args(file.getAbsolutePath());
    }
  }
}
package com.walkree.gradle.plugin.cpp.clang;

import java.io.File;

import org.gradle.api.internal.tasks.compile.ArgWriter;
import org.gradle.api.tasks.WorkResult;
import org.gradle.internal.Factory;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.plugins.binaries.model.LibraryCompileSpec;
import org.gradle.plugins.cpp.compiler.internal.CommandLineCppCompiler;
import org.gradle.plugins.cpp.compiler.internal.CommandLineCppCompilerArgumentsToOptionFile;
import org.gradle.plugins.cpp.gpp.internal.GppCompiler;
import org.gradle.process.internal.ExecAction;

public class ClangCompiler extends CommandLineCppCompiler<ClangCompileSpec> {
  private final Factory<ExecAction> execActionFactory;

  public ClangCompiler(File executable, Factory<ExecAction> execActionFactory,
      boolean useCommandFile) {
    super(executable, execActionFactory, useCommandFile ? viaCommandFile() : withoutCommandFile());
    this.execActionFactory = execActionFactory;
  }

  public WorkResult execute(ClangCompileSpec spec) {
    WorkResult result = super.execute(spec);
    if (spec instanceof LibraryCompileSpec) {
      // Do static link on Linux for production
      // Must use ar to generate .a files
      if (OperatingSystem.current().isLinux()) {
        ExecAction ar = execActionFactory.create();
        // workdir must exist otherwise the compilation setup would fail first
        ar.workingDir(spec.getWorkDir());
      }
    }
    return result;
  }

  private static ClangCompileSpecToArguments withoutCommandFile() {
    return new ClangCompileSpecToArguments();
  }

  private static CommandLineCppCompilerArgumentsToOptionFile<ClangCompileSpec> viaCommandFile() {
    return new CommandLineCppCompilerArgumentsToOptionFile<ClangCompileSpec>(
        ArgWriter.unixStyleFactory(), new ClangCompileSpecToArguments()
    );
  }
}

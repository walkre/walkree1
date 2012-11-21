package com.walkree.gradle.plugin.cpp.clang;

import java.io.File;

import org.gradle.api.Transformer;
import org.gradle.api.internal.tasks.compile.Compiler;
import org.gradle.internal.Factory;
import org.gradle.internal.os.OperatingSystem;
import org.gradle.plugins.binaries.model.Binary;
import org.gradle.plugins.cpp.compiler.internal.CommandLineCppCompilerAdapter;
import org.gradle.process.internal.ExecAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClangCompilerAdapter extends CommandLineCppCompilerAdapter<ClangCompileSpec> {
  public static final String EXECUTABLE = "clang++";
  public static final String NAME = "clang";
  private static final Logger LOGGER = LoggerFactory.getLogger(ClangCompilerAdapter.class);
  private final Transformer<String, File> versionDeterminer;
  private boolean determinedVersion;
  private String version;

  public ClangCompilerAdapter(OperatingSystem operatingSystem,
      Factory<ExecAction> execActionFactory) {
    // TODO(yunjing): use GppVersionDeterminer
    this(operatingSystem, execActionFactory, null);
  }

  private ClangCompilerAdapter(OperatingSystem operatingSystem,
      Factory<ExecAction> execActionFactory, Transformer<String, File> versionDeterminer) {
    super(EXECUTABLE, operatingSystem, execActionFactory);
    this.versionDeterminer = versionDeterminer;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String toString() {
    return String.format("Clang (%s)", getOperatingSystem().getExecutableName(EXECUTABLE));
  }

  @Override
  public boolean isAvailable() {
    String version = getVersion();
        return version != null;
  }

  @Override
  public Compiler<ClangCompileSpec> createCompiler(Binary binary) {
    String version = getVersion();
    if (version == null) {
      throw new IllegalStateException("Cannot create clang compiler when it is not available");
    }
        
    String[] components = version.split("\\.");

    int majorVersion;
    try {
      majorVersion = Integer.valueOf(components[0]);
    } catch (NumberFormatException e) {
      throw new IllegalStateException(String.format(
          "Unable to determine major clang version from version number %s.", version), e);
    }

    return new ClangCompiler(getExecutable(), getExecActionFactory(), majorVersion >= 3);
  }

  private String getVersion() {
    if (!determinedVersion) {
      determinedVersion = true;
      version = determineVersion(getExecutable());
      if (version == null) {
        LOGGER.info("Did not find {} on system", EXECUTABLE);
      } else {
        LOGGER.info("Found {} with version {}", EXECUTABLE, version);
      }
    }
    return version;
  }

  private String determineVersion(File executable) {
    //return executable == null ? null : versionDeterminer.transform(executable);
    return executable == null ? null : "3";
  }
}
package com.walkree.gradle.plugin.cpp;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.plugins.binaries.model.Executable;
import org.gradle.plugins.cpp.CppCompile;
import org.gradle.plugins.cpp.gpp.GppCompileSpec;

import com.walkree.gradle.plugin.Package;

public class CppExecutableTarget extends CppTarget {
  private static NamedDomainObjectContainer executables;

  public CppExecutableTarget(Package pkg) {
    super(pkg);
    if (executables == null) {
      executables = (NamedDomainObjectContainer)
          getProject().getExtensions().getByName("executables");
    }
  }

  @Override
  public void configure() {
    CppCompile task = createCompileTask();

    Executable exe = (Executable) executables.create(getName());
    exe.getSourceSets().add(createCppSourceSet());
    GppCompileSpec spec = (GppCompileSpec) exe.getSpec();
    spec.configure(task);
  }

  @Override
  public String getTaskName() {
    return getName();
  }
}

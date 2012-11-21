package com.walkree.gradle.plugin.cpp;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Task;
import org.gradle.plugins.binaries.model.Executable;
import org.gradle.plugins.cpp.CppCompile;
import org.gradle.plugins.cpp.gpp.GppCompileSpec;

import com.walkree.gradle.plugin.Package;

public class CppExecutableTarget extends CppTarget {
  private static NamedDomainObjectContainer executables;
  private Task task;
  private Executable executable;

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
    this.executable = exe;
    exe.getSourceSets().add(createCppSourceSet());
    GppCompileSpec spec = (GppCompileSpec) exe.getSpec();
    setCompileSpec(spec);
    spec.configure(task);
    setIncludeRoots(spec);
    this.task = task;
  }  

  @Override
  public String getTaskName() {
    return getName();
  }

  @Override
  public Task getTask() {
    return this.task;
  }

  @Override
  public Type getType() {
    return Type.CPP_EXECUTABLE;
  }

  public Executable getExecutable() {
    return executable;
  }
}

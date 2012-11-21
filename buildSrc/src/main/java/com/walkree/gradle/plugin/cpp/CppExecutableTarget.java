package com.walkree.gradle.plugin.cpp;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Task;
import org.gradle.plugins.binaries.model.Executable;
import org.gradle.plugins.cpp.CppCompile;

import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.cpp.clang.ClangCompileSpec;

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
    if (getSources() == null || getSources().length == 0) {
      throw new RuntimeException(String.format("Empty target: '%s'", this));
    }
    CppCompile task = createCompileTask();    
    Executable exe = (Executable) executables.create(getName());
    this.executable = exe;
    exe.getSourceSets().add(createCppSourceSet());
    ClangCompileSpec spec = (ClangCompileSpec) exe.getSpec();
    setCompileSpec(spec);    
    spec.configure(task);    
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

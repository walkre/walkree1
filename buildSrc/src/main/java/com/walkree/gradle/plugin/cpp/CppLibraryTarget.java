package com.walkree.gradle.plugin.cpp;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.plugins.binaries.model.Library;
import org.gradle.plugins.cpp.CppCompile;
import org.gradle.plugins.cpp.gpp.GppLibraryCompileSpec;

import com.walkree.gradle.plugin.Package;

public class CppLibraryTarget extends CppTarget {
  private static NamedDomainObjectContainer libraries;

  public CppLibraryTarget(Package pkg) {
    super(pkg);
    if (libraries == null) {
      libraries = (NamedDomainObjectContainer)
          getProject().getExtensions().getByName("libraries");
    }
  }

  @Override
  public void configure() {
    CppCompile task = createCompileTask();

    Library lib = (Library) libraries.create(getName());
    lib.getSourceSets().add(createCppSourceSet());
    GppLibraryCompileSpec spec = (GppLibraryCompileSpec) lib.getSpec();
    spec.configure(task);
  }

  @Override
  public String getTaskName() {
    return getName();
  }
}

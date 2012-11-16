package com.walkree.gradle.plugin.cpp;

import java.util.HashMap;
import java.util.Map;

import org.gradle.api.file.SourceDirectorySet;
import org.gradle.plugins.cpp.CppCompile;
import org.gradle.plugins.cpp.CppExtension;
import org.gradle.plugins.cpp.CppSourceSet;

import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;

//import com.walkree.gradle.plugin.cpp.clang.ClangCompileSpec;

public abstract class CppTarget extends Target {
  private static CppExtension cppExt;

  public CppTarget(Package pkg) {
    super(pkg);
    if (cppExt == null) {
      cppExt = getProject().getExtensions().getByType(CppExtension.class);
    }
  }

  public CppCompile createCompileTask() {
    Map<String, Object> args = new HashMap<String, Object>();
    args.put("type", CppCompile.class);
    CppCompile task = (CppCompile) getProject().task(args, getTaskName());
    return task;
  }

  public CppSourceSet createCppSourceSet() {
    CppSourceSet cppSrcSet = cppExt.getSourceSets().create(getName());
    SourceDirectorySet srcDirSet = cppSrcSet.getSource();
    srcDirSet.srcDir(getPackage().getPackageRootDir());
    srcDirSet.include(getSources());
    return cppSrcSet;
  }

  public abstract String getTaskName();
}

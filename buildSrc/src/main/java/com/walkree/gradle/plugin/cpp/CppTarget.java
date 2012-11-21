package com.walkree.gradle.plugin.cpp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gradle.api.file.SourceDirectorySet;
import org.gradle.plugins.binaries.model.Library;
import org.gradle.plugins.cpp.CppCompile;
import org.gradle.plugins.cpp.CppExtension;
import org.gradle.plugins.cpp.CppSourceSet;
import org.gradle.plugins.cpp.gpp.GppCompileSpec;

import com.walkree.gradle.plugin.Package;
import com.walkree.gradle.plugin.Target;

//import com.walkree.gradle.plugin.cpp.clang.ClangCompileSpec;

public abstract class CppTarget extends Target {
  private static CppExtension cppExt;
  private GppCompileSpec compileSpec;

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
    // Only include the package root dir as source code basedir
    srcDirSet.setSrcDirs(Arrays.asList(getPackage().getPackageRootDir()));
    srcDirSet.include(getSources());
    return cppSrcSet;
  }  

  @Override
  public boolean isCompatible(Target depTarget) {
    if (depTarget.getType() != Type.CPP_LIBRARY) {
      return false;
    }
    return true;
  }

  @Override
  public void dependsOn(Target depTarget) {
    CppLibraryTarget cppLibTarget = (CppLibraryTarget) depTarget;
    getCompileSpec().libs(Arrays.asList(new Library[]{cppLibTarget.getLibrary()}));
  }

  protected void setIncludeRoots(GppCompileSpec spec) {
    // TODO(yunjing): use global constants.
    // This step could be included in our own CompileSpec
    spec.includes(Arrays.asList(getProject().file(
        getProject().getBuildDir() + "/exported/src/cpp")));
  }

  protected GppCompileSpec getCompileSpec() {
    return compileSpec;    
  }

  protected void setCompileSpec(GppCompileSpec spec) {
    compileSpec = spec;
  }

  public abstract String getTaskName();  
}
package com.walkree.gradle.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependencyManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(DependencyManager.class);
  private BuildSystem buildSystem;
  // target depends on a list of targets
  private Map<Target, List<Target>> depMap;

  public DependencyManager(BuildSystem buildSystem) {
    this.buildSystem = buildSystem;
    depMap = new HashMap<Target, List<Target>>();
  }

  // target depends on depTarget
  public void addDependency(Target target, Target depTarget) {
    List<Target> depList = depMap.get(target);
    if (depList == null) {
      depList = new ArrayList<Target>();
      depMap.put(target, depList);
    }
    if (depTarget != null) {
      depList.add(depTarget);      
    }
  }

  public List<Target> getDependencyTargets(Target target) {
    return depMap.get(target);    
  }

  public void resolveDependency(Target target) {
    String[] depStrs = target.getDependencies();
    if (depStrs == null || depStrs.length == 0) {
      addDependency(target, null);
      return;
    }
    for (String dep : depStrs) {
      // a dependency string should be in the form pkg_path:target_name
      // pkg_path is optional
      String[] parts = dep.split(Constant.TARGET_SEPARATOR);
      if (parts.length != 2) {        
        throw new RuntimeException(String.format(
            "Dependency syntax error: '%s' for '%s'", dep, target));
      }
      String packageName = parts[0];
      String targetName = parts[1];
      Package pkg = null;
      if (packageName.length() == 0) {
        // reference a target in its own package
        pkg = target.getPackage();
      } else {
        pkg = buildSystem.findPackage(packageName);
      }
      if (pkg == null) {
        throw new RuntimeException(String.format(
            "Package doesn't exist: '%s' for '%s'", packageName, target));
      }
      Target depTarget = pkg.findTarget(targetName);
      if (depTarget == null) {
        throw new RuntimeException(String.format(
            "Dependency doesn't exist: '%s' for '%s'", dep, target));
      }
      if (!target.isCompatible(depTarget)) {
        throw new RuntimeException(String.format(
            "Incompatible dependency: %s target '%s' depends on %s target '%s'",
            target.getType().name(), target, depTarget.getType().name(), depTarget));
      }
      target.dependsOn(depTarget);
      target.getTask().dependsOn(depTarget.getTask());
      addDependency(target, depTarget);
      LOGGER.info("Target '{}' depends on target '{}'", target, depTarget);
    }    
  }
}
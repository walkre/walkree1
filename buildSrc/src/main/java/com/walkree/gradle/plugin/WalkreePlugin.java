package com.walkree.gradle.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * The entry point of the plugin.
 */
public class WalkreePlugin implements Plugin<Project> {
  /**
   * Apply the plugin to the given Gradle {@link Project} instance.
   * @param   project The given Gradle project instance.
   */
  @Override
  public void apply(Project project) {
    Environment env = new Environment(project);
    env.configure();
  }
}

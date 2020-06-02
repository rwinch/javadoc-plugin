/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.build.optional;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.attributes.Usage;
import org.gradle.api.plugins.JavaPlugin;

/**
 * A {@code Plugin} that adds support for Maven-style optional dependencies. Creates a new
 * {@code optional} configuration. The {@code optional} configuration is part of the
 * project's compile and runtime classpath's but does not affect the classpath of
 * dependent projects.
 *
 * @author Andy Wilkinson
 */
public class OptionalDependenciesPlugin implements Plugin<Project> {

	/**
	 * Name of the {@code optional} configuration.
	 */
	public static final String OPTIONAL_CONFIGURATION_NAME = "optional";

	@Override
	public void apply(Project project) {
		ConfigurationContainer configurations = project.getConfigurations();
		Configuration optional = configurations.create(OPTIONAL_CONFIGURATION_NAME);
		optional.attributes((attributes) -> attributes.attribute(Usage.USAGE_ATTRIBUTE,
				project.getObjects().named(Usage.class, Usage.JAVA_RUNTIME)));
		project.getPlugins().withType(JavaPlugin.class, (javaPlugin) -> {
			configurations.getByName(JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME).extendsFrom(optional);
			configurations.getByName(JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME).extendsFrom(optional);
		});
	}

}

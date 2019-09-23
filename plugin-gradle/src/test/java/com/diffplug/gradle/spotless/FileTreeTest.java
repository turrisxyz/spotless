/*
 * Copyright 2016 DiffPlug
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diffplug.gradle.spotless;

import static com.diffplug.gradle.spotless.FormatExtension.relativize;

import java.io.File;
import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileTree;
import org.junit.Before;
import org.junit.Test;

import com.diffplug.spotless.ResourceHarness;
import com.diffplug.spotless.TestProvisioner;

public class FileTreeTest extends ResourceHarness {
	private ConfigurableFileTree fileTree;

	@Before
	public void fileTree() throws IOException {
		Project project = TestProvisioner.gradleProject(rootFolder());
		fileTree = project.fileTree(rootFolder());
		fileTree.exclude("userHome"); // somehow we're getting userHome\native\19\windows-amd64\native-platform.dll
	}

	@Test
	public void absolutePathDoesntWork() throws IOException {
		File someFile = setFile("someFolder/someFile").toContent("");
		File someFolder = someFile.getParentFile();
		fileTree.exclude(someFolder.getAbsolutePath());
		Assertions.assertThat(fileTree).containsExactlyInAnyOrder(someFile);
	}

	@Test
	public void relativePathDoes() throws IOException {
		File someFile = setFile("someFolder/someFile").toContent("");
		File someFolder = someFile.getParentFile();
		fileTree.exclude(relativize(rootFolder(), someFolder));
		Assertions.assertThat(fileTree).containsExactlyInAnyOrder();
	}
}

package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ManifestVersionProviderTest {

	@Test
	void nominal() throws Exception {
		var provider = new ManifestVersionProvider();

		var actual = provider.getVersion();

		/*
		 * JFileTreePrettyPrinter 0.1.1-SNAPSHOT
		 * A lightweight and flexible Java library with a native CLI to pretty-print directory structures - ideal for documentation, project overviews, or CLI tools.
		 * Repository: https://github.com/ComputerDaddyGuy/JFileTreePrettyPrinter
		 * Commit: 23cf1139157ffa046378fb602fe7750a107c6bc1 (2025-11-04T22:27:16+01:00)
		 * Built on: 2025-11-06T22:07:30+01:00
		 */
		assertThat(actual).satisfiesExactly(
			line1 -> assertThat(line1).isNotBlank(),
			line2 -> assertThat(line2).isNotBlank(),
			line3 -> assertThat(line3).startsWith("Repository:"),
			line4 -> assertThat(line4).startsWith("Commit:"),
			line5 -> assertThat(line5).startsWith("Built on:")
		);
	}

}

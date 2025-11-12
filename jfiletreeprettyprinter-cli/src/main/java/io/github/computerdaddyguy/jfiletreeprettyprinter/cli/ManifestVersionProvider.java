package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import java.io.InputStream;
import java.util.jar.Manifest;
import picocli.CommandLine.IVersionProvider;

class ManifestVersionProvider implements IVersionProvider {

	@Override
	public String[] getVersion() throws Exception {
		String implVersion = null;
		String projectDesc = null;
		String commitId = null;
		String commitTime = null;
		String scmUrl = null;
		String buildTime = null;

		try (InputStream is = getClass().getResourceAsStream("/META-INF/MANIFEST.MF")) {
			if (is != null) {
				Manifest manifest = new Manifest(is);
				implVersion = manifest.getMainAttributes().getValue("Implementation-Version");
				projectDesc = manifest.getMainAttributes().getValue("Project-Desc");
				commitId = manifest.getMainAttributes().getValue("Commit-Id");
				commitTime = manifest.getMainAttributes().getValue("Commit-Time");
				scmUrl = manifest.getMainAttributes().getValue("Scm-Url");
				buildTime = manifest.getMainAttributes().getValue("Build-Time");
			}
		}

		return new String[] {
			"JFileTreePrettyPrinter " + valueOrUnknown(implVersion),
			valueOrUnknown(projectDesc),
			"Repository: " + valueOrUnknown(scmUrl),
			"Commit: " + valueOrUnknown(commitId) + " (" + valueOrUnknown(commitTime) + ")",
			"Built on: " + valueOrUnknown(buildTime)
		};
	}

	private static final String valueOrUnknown(String value) {
		return value != null ? value : "unknown";
	}

}
package io.github.computerdaddyguy.jfiletreeprettyprinter.renderer.file;

import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

@NullMarked
class DefaultEmojiMappingBuilder {

	private String directoryEmoji = "📂";
	private String defaultFileEmoji = "📄";
	private Map<String, String> exactFileNamesEmojis = buildDefaultExactFileNamesEmojis();
	private Map<String, String> fileExtensionsEmojis = buildDefaultFileExtensionsEmojis();

	EmojiMapping build() {
		return new DefaultEmojiMapping(directoryEmoji, defaultFileEmoji, exactFileNamesEmojis, fileExtensionsEmojis);
	}

	private Map<String, String> buildDefaultExactFileNamesEmojis() {
		Map<String, String> map = new HashMap<>();

		// ---------- Applications ----------

		// ---------- Archives ----------

		// ---------- Code ----------

		// Code - default
		map.put(".gitignore", "🙈");
		map.put("dockerfile", "🐳");
		map.put("jenkinsfile", "🤵");

		// Code - github
		map.put("readme", "📖");
		map.put("readme.md", "📖");
		map.put("roadmap", "🗺️");
		map.put("roadmap.md", "🗺️");
		map.put("licence", "🗺️");
		map.put("licence.md", "🗺️");
		map.put("changelog", "🗺️");
		map.put("changelog.md", "🗺️");
		map.put("security", "🛡️");
		map.put("security.md", "🛡️");

		// Code - build tools
		map.put("pom.xml", "🏗️");
		map.put("build.gradle", "🏗️");
		map.put("package.json", "🏗️");

		// ---------- Data ----------

		// ---------- Doc ----------

		// ---------- Media ----------

		return map;
	}

	private Map<String, String> buildDefaultFileExtensionsEmojis() {
		Map<String, String> map = new HashMap<>();

		// ---------- Applications ----------

		// Applications - executables
		map.put("exe", "⚙️");
		map.put("msi", "📦");
		map.put("apk", "📱");
		map.put("ipa", "📱");
		map.put("app", "🖥️");

		// Applications - libs
		map.put("dll", "🧩");
		map.put("lib", "🧩");
		map.put("so", "🧩");

		// ---------- Archives ----------
		map.put("7z", "📦");
		map.put("gz", "📦");
		map.put("img", "💿");
		map.put("iso", "💿");
		map.put("tar", "📦");
		map.put("rar", "📦");
		map.put("zip", "📦");

		// ---------- Code ----------
		// Code - build tools

		// Code - lang
		map.put("java", "☕");
		map.put("class", "☕");
		map.put("jar", "📦");
		map.put("py", "🐍");
		map.put("js", "🟨");
		map.put("ts", "🔷");
		map.put("c", "💠");
		map.put("cpp", "💠");
		map.put("cs", "💠");
		map.put("css", "🎨");
		map.put("scss", "🎨");
		map.put("less", "🎨");
		map.put("html", "🌐");
		map.put("htm", "🌐");
		map.put("htmx", "🌐");
		map.put("php", "🐘");
		map.put("sql", "🗄️");
		map.put("vue", "🟩");

		// Code - scripting
		map.put("sh", "📜");
		map.put("bash", "📜");
		map.put("bat", "📜");

		// ---------- Data ----------
		map.put("cfg", "⚙️");
		map.put("conf", "⚙️");
		map.put("csv", "📊");
		map.put("ini", "⚙️");
		map.put("properties", "⚙️");
		map.put("json", "📝");
		map.put("ods", "📊");
		map.put("xls", "📊");
		map.put("xlsx", "📊");
		map.put("xml", "📝");
		map.put("yaml", "📝");
		map.put("yml", "📝");

		// ---------- Doc ----------
		map.put("doc", "📃");
		map.put("docx", "📃");
		map.put("epub", "📚");
		map.put("md", "📖");
		map.put("odt", "📃");
		map.put("pdf", "📕");
		map.put("rtf", "📃");
		map.put("txt", "📄");

		// ---------- Media ----------
		// Media - Audio
		map.put("aac", "🎶");
		map.put("flac", "🎶");
		map.put("midi", "🎹");
		map.put("mp3", "🎵");
		map.put("ogg", "🎶");
		map.put("wav", "🎵");

		// Media - Images
		map.put("bmp", "🖼️");
		map.put("gif", "🎞️");
		map.put("jpeg", "🖼️");
		map.put("jpg", "🖼️");
		map.put("png", "🖼️");
		map.put("svg", "✒️");
		map.put("ico", "🔲");

		// Media - Video
		map.put("avi", "🎬");
		map.put("mkv", "🎬");
		map.put("mov", "🎬");
		map.put("mp4", "🎬");
		map.put("webm", "🎬");
		map.put("wmv", "🎬");

		// ---------- System ----------
		map.put("bak", "💾");
		map.put("log", "📜");
		map.put("tmp", "🗑️");
		map.put("key", "🔑");
		map.put("pem", "🔐");
		map.put("crt", "🔐");
		map.put("pub", "🔓");

		return map;

	}

}
package io.github.computerdaddyguy.jfiletreeprettyprinter.cli;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ChildLimit.DynamicLimit;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ChildLimit.DynamicLimitItem;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.ExternalOptions;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.Filter;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.LineExtension;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.Matcher.NameGlob;
import io.github.computerdaddyguy.jfiletreeprettyprinter.cli.options.model.Matcher.PathGlob;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecordUtilsTest {

	@Test
	void nominal() {

		var dynLimit1 = new DynamicLimitItem(1, new NameGlob("*.java"));
		var dynLimit2 = new DynamicLimitItem(1, new PathGlob("pictures"));
		var childLimit = new DynamicLimit(List.of(dynLimit1, dynLimit2));
		var maxDepth = 3;
		var filter = new Filter(
			new NameGlob("folderOk"),
			new NameGlob("fileOk.*")
		);
		var lineExt1 = new LineExtension("This is java file", new NameGlob("*.java"));
		var lineExt2 = new LineExtension("This is php file", new NameGlob("*.php"));
		var lineExtensions = List.of(lineExt1, lineExt2);
		var rec = new ExternalOptions(Boolean.TRUE, Boolean.TRUE, maxDepth, childLimit, filter, lineExtensions);

		var expected = """
			ExternalOptions
			  emojis: true
			  compactDirectories: true
			  maxDepth: 3
			  childLimit -> DynamicLimit
			    limits: [
			      [0] -> DynamicLimitItem
			        limit: 1
			        matcher -> NameGlob
			          glob: "*.java"
			      [1] -> DynamicLimitItem
			        limit: 1
			        matcher -> PathGlob
			          glob: "pictures"
			    ]
			  filter -> Filter
			    dir -> NameGlob
			      glob: "folderOk"
			    file -> NameGlob
			      glob: "fileOk.*"
			  lineExtensions: [
			    [0] -> LineExtension
			      extension: "This is java file"
			      matcher -> NameGlob
			        glob: "*.java"
			    [1] -> LineExtension
			      extension: "This is php file"
			      matcher -> NameGlob
			        glob: "*.php"
			  ]""";

		assertThat(RecordUtils.toTextBlock(rec)).isEqualToNormalizingNewlines(expected);
	}

}
